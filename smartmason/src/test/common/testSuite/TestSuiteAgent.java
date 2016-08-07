/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */

package test.common.testSuite;

import jade.core.Agent;
import jade.core.AID;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.*;
import jade.core.AgentManager;
import jade.core.ContainerID;

import jade.wrapper.*;
import jade.proto.AchieveREInitiator;
import jade.proto.states.ReplySender;

import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.content.onto.basic.Result;
import jade.content.onto.basic.Action;

import test.common.*;
import test.common.behaviours.ListProcessor;
import test.common.testSuite.gui.*;
import test.common.testerAgentControlOntology.*;
import test.common.xml.*;
import test.common.Logger;


import jade.util.leap.List;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;

import java.util.Hashtable;
import java.util.Vector;

import jade.domain.FIPAAgentManagement.*;
import jade.domain.JADEAgentManagement.*;
import jade.domain.introspection.*;
import jade.domain.mobility.*;
import jade.domain.FIPANames;

import jade.gui.AgentTreeModel;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.*;

import jade.tools.ToolAgent;

import java.awt.event.*;


/**
 * @author Giovanni Caire - TiLab
 * @author Elisabetta Cortese - TiLab
 * @author Alessandro Negri - AOTLab UniPR
 * @author Matteo Bisi - UniPR
 * @author Yuri Ferrari - UniPR
 * @author Rossano Vitulli - UniPR
 * @version $Date: December 2003
 */
public class TestSuiteAgent extends GuiAgent {
	
	private static ACLMessage    AMSRequest = new ACLMessage(ACLMessage.REQUEST);
	
	public static JadeController mainController;
	
	public static final String   MAIN_SERVICES = "jade.core.mobility.AgentMobilityService;jade.core.event.NotificationService;jade.core.replication.MainReplicationService;jade.core.replication.AddressNotificationService;jade.core.messaging.PersistentDeliveryService;jade.core.messaging.TopicManagementService";
	public static final String   TEST_PLATFORM_NAME = "TestPlatform";
	
	private static final String  NAME = "test-suite";
	private static final String  TESTER_NAME = "tester";
	
	// Gui event types
	public static final int      LOAD_EVENT = 0;
	public static final int      RUN_EVENT = 2;
	public static final int      DEBUG_EVENT = 3;
	public static final int      CONFIGURE_EVENT = 4;
	public static final int      STEP_EVENT = 5;
	public static final int      GO_EVENT = 6;
	public static final int      EXIT_EVENT = 7;
	public static final int      SELECT_EVENT = 8;
	public static final int      RUNALL_EVENT = 9;
	public static final int      TEST_CLICK_EVENT = 10;
	
	// Maps a functionality with the results of the its tests.
	// Used in RUN_ALL to print the final report
	private Hashtable groupTestsResults;
	private Hashtable runAllTestsResults = new Hashtable();
	private Hashtable runAllSummaries = new Hashtable();
	private boolean runAllSuccessful;
	private boolean firstGroup;
	
	private Codec                codec;
	
	private TestSuiteGui         myGui;
	
	
	// Used to set elapsed time in the GUI
	private long                 startTime;
	javax.swing.Timer timer = new javax.swing.Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			updateElapsedTime();
		} 
	} );
	
	private String xmlFileName = "test/testerList.xml";
	private FunctionalityDescriptor[] allFunc;
	// The functionality that is currently loaded
	private FunctionalityDescriptor currentFunc;
	
	protected void setup() {
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			xmlFileName = (String) args[0];
		}
		System.out.println("Loading functionality list form file "+xmlFileName);
		allFunc = XMLManager.getFunctionalities(xmlFileName);
		
		// Register language and ontology used to control the
		// execution of the tester agents
		codec = new SLCodec();
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(TesterAgentControlOntology.getInstance());
		
		// Create the GUI
		myGui = new TestSuiteGui(this, xmlFileName);
		myGui.showCorrect();
		
		// Add the behaviour listening for test completion notifications from
		// tester agents.
		CyclicReceiver rec = new CyclicReceiver(this);
		addBehaviour(rec);    
		
		// if (runAutomatic) then: set the logger to text file, runall.
		// at the end it will also EXIT (see AllTesterExecutor.onEnd)
		if (System.getProperty("test.common.testSuite.TestSuiteAgent.runAutomatic") != null) {
			Logger.getLogger().setFileLogger(Logger.TXT_LOGGER);
			onGuiEvent(new GuiEvent(this, RUNALL_EVENT));
		}
	} 
	
	protected void takeDown() {
		Logger.getLogger().closeLogger();
		if (myGui != null) {
			myGui.dispose();
		}
	} 
	
	public TestResult getTestResult(String testName, String funcName) {
		Hashtable map = null;
		if (funcName != null) {
			map = (Hashtable) runAllTestsResults.get(funcName);
		}
		else {
			map = groupTestsResults;
		}
		if (map != null) {
			TestResult tr = (TestResult) map.get(testName);
			return tr;
		}
		else {
			return null;
		}
	}
	
	protected void onGuiEvent(GuiEvent ev) {
		Logger lg = Logger.getLogger();
		
		switch (ev.getType()) {
		case LOAD_EVENT:      
			groupTestsResults = new Hashtable();
			myGui.clearFailedTestsMessage();
			
			final FunctionalityDescriptor fd = (FunctionalityDescriptor) ev.getParameter(0);
			// The user pressed "Open". If no tester is currently loaded, just load
			// the indicated one. Otherwise kill the currently loaded tester before
			if (myGui.getStatus() == TestSuiteGui.IDLE_STATE) {
				loadTester(fd);
			} 
			else {
				// Add a behaviour that makes the currently loaded tester agent exit and,
				// on completion, loads the newly specified tester agent.
				
				addBehaviour(new Requester(this, new Exit()) {
					public int onEnd() {
						waitABit();
						loadTester(fd);
						return 0;
					} 
				});
			}
			break;
		case RUN_EVENT:
			// The user pressed "Run"
			// Add a behaviour that makes the currently loaded tester agent execute
			// its test group and, on completion, sets the GUI status back to READY
			
			groupTestsResults.clear();
			startTime = System.currentTimeMillis();
			timer.start();
			
			if (lg.getLoggerType() == Logger.HTML_LOGGER) {
				lg.addIndex("Single Group Execution", currentFunc.toString());
				lg.addBodyHeading(currentFunc.toString());
				lg.incBlockCounter();
			} 
			
			addBehaviour(new Requester(this, new Execute(false)) {
				public int onEnd() {
					timer.stop();
					updateElapsedTime();
					myGui.setStatus(TestSuiteGui.READY_STATE);
					myGui.getStatusLabel().setText("Done");
					myGui.getStatusLabel().setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/idle_buttons.gif")));
					return 0;
				} 
			});
			myGui.clearFailedTestsMessage();
			
			break;
		case RUNALL_EVENT:
			// The user pressed "Run All"
			// Add a behaviour that execute all test groups      
			final ArrayList l = new ArrayList();
			if (allFunc != null) {
				for (int i = 0; i < allFunc.length; i++) {
					l.add(i, allFunc[i]);
				} 
				// Add a behaviour that executes all testers in sequence. If there is
				// a tester currently loaded, kill it before.
				if (myGui.getStatus() == TestSuiteGui.IDLE_STATE) {
					addBehaviour(new AllTesterExecutor(this, l));
				} 
				else {
					addBehaviour(new Requester(this, new Exit()) {
						public int onEnd() {
							waitABit();
							addBehaviour(new AllTesterExecutor(myAgent, l));
							return 0;
						} 
					});
				} 
			} 
			break;
		case DEBUG_EVENT:
			// The user pressed "Debug"
			// Add a behaviour that makes the currently loaded tester agent execute
			// its test group in debug-mode and, on completion, sets the GUI status back to READY
			
			groupTestsResults.clear();
			
			myGui.getStatusLabel().setText("Debug Mode, press Next or Run to continue");
			myGui.getStatusLabel().setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/blink_buttons.gif")));
			
			if (lg.getLoggerType() == Logger.HTML_LOGGER) {
				lg.addIndex("Debug Mode", currentFunc.toString());
				lg.addBodyHeading(currentFunc.toString());
				lg.incBlockCounter();
			} 
			
			addBehaviour(new Requester(this, new Execute(true)) {
				public int onEnd() {
					myGui.getStatusLabel().setText("Done");
					myGui.getStatusLabel().setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/idle_buttons.gif")));
					myGui.setStatus(TestSuiteGui.READY_STATE);
					return 0;
				} 
			});
			break;
		case CONFIGURE_EVENT:
			// The user pressed "Configure"
			// Add a behaviour that makes the currently loaded tester agent show
			// the test group configuration gui and, on completion, sets the GUI status back to READY
			addBehaviour(new Requester(this, new Configure()) {
				public int onEnd() {
					myGui.setStatus(TestSuiteGui.READY_STATE);
					return 0;
				} 
			});
			break;
		case SELECT_EVENT:
			// The user pressed "Select"
			// Add a behaviour that makes the currently loaded tester agent show
			// the test selection gui and, on completion, sets the GUI status back to READY
			addBehaviour(new Requester(this, new SelectTests()) {
				public int onEnd() {
					myGui.setStatus(TestSuiteGui.READY_STATE);
					return 0;
				} 
			});
			break;
		case STEP_EVENT:
			// The user pressed "Step"
			// Add a behaviour that makes the currently loaded tester agent resume
			// the execution of its test group in debug-mode
			myGui.getStatusLabel().setText("Sigle test execution...");
			myGui.getStatusLabel().setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/progress_buttons.gif")));
			
			addBehaviour(new Requester(this, new Resume(true)));
			break;
		case GO_EVENT:
			// The user pressed "Run" while the currently loaded tester agent is executing his test group
			// Add a behaviour that makes the currently loaded tester agent resume
			// the execution of its test group in normal-mode
			addBehaviour(new Requester(this, new Resume(false)));
			break;
			
			/*case TEST_CLICK_EVENT:
			 String  s = (String) ev.getParameter(0);
			 Boolean b = (Boolean) ev.getParameter(1);
			 if (b.booleanValue() == true) {
			 myGui.printFailedTestsMessage(s);
			 } 
			 else {
			 myGui.clearFailedTestsMessage();
			 }
			 break;*/
			
		case EXIT_EVENT:
			// The user pressed "Exit". If no tester is currently loaded, just exit.
			// Otherwise kill the currently loaded tester before
			if (myGui.getStatus() == TestSuiteGui.IDLE_STATE) {
				doDelete();
			} 
			else {
				// Add a behaviour that makes the currently loaded tester agent exit and,
				// on completion, exit.
				addBehaviour(new Requester(this, new Exit()) {
					public int onEnd() {
						doDelete();
						return 0;
					} 
				});
			} 
			break;
		}
	} 
	
	private void loadTester(FunctionalityDescriptor fd) {
		Logger.getLogger().log("Loading functionality: "+fd);
		try {
			TestUtility.createAgent(this, TESTER_NAME, fd.getTesterClassName(),
					new String[] {"true", getLocalName()}, getAMS(), null);
			if (myGui.getStatus() != TestSuiteGui.RUNALL_STATE) {
				myGui.setStatus(TestSuiteGui.READY_STATE);
			}
			currentFunc = fd;
			
			// If we are in RUNALL state --> add the newly loaded functinality 
			// to the history
			if (myGui.getStatus() == TestSuiteGui.RUNALL_STATE) {
				myGui.addToHistory(fd);
			}
			myGui.getTestsTree().showTestsHierarchy(fd.getTestsListFile(), fd.getName(), true);
		} 
		catch (Exception e) {
			System.out.println("Error loading tester agent. ");
			e.printStackTrace();
			Logger.getLogger().log("Error loading tester agent. ");
			Logger.getLogger().logStackTrace(e);
			if (myGui.getStatus() != TestSuiteGui.RUNALL_STATE) {
				myGui.setStatus(TestSuiteGui.IDLE_STATE);
			}
		} 
	} 
	
	/**
	 * INNER CLASS: AllTesterExecutor
	 * This behaviour executes a sequence of TesterAgents one by one.
	 * On completion, print a report and, as no tester is alive,
	 * set the GUI status to IDLE.
	 */
	class AllTesterExecutor extends ListProcessor {
		
		public AllTesterExecutor(Agent a, ArrayList l) {
			super(a, l);
		}
		
		public void onStart() {
			myGui.setStatus(TestSuiteGui.RUNALL_STATE);
			myGui.startProgressBar(getNonSkippedCount());
			
			runAllTestsResults.clear();      			
			runAllSummaries.clear();
			firstGroup = true;
			runAllSuccessful = true;
			
			startTime = System.currentTimeMillis();
			timer.start();      
		} 
		
		private int getNonSkippedCount() {
			int cnt = 0;
			Iterator it = items.iterator();
			while (it.hasNext()) {
				FunctionalityDescriptor fd = (FunctionalityDescriptor) it.next();
				if (!fd.getSkip()) {
					cnt++;
				}
			}
			return cnt;
		}
		
		// take tester to run and its order in the list
		protected void processItem(Object item, int index) {
			FunctionalityDescriptor fDsc = (FunctionalityDescriptor) item;
			if (!fDsc.getSkip()) {
				myAgent.addBehaviour(new SingleTesterExecutor(myAgent, fDsc, this));
				pause();
				myGui.resetAll(false);
			} 
		} 
		
		public int onEnd() {
			
			int numPassed = 0, numSkipped = 0, numFailed = 0;
			Logger lg = Logger.getLogger();
			lg.log(" ");
			lg.log(" ");
			lg.log("************ FINAL REPORT ****************");
			lg.log(" ");
			
			for (int i = 0; i < items.size(); i++) {
				FunctionalityDescriptor fDsc = (FunctionalityDescriptor) items.get(i);
				
				ExecResult res = (ExecResult) runAllSummaries.get(fDsc);
				if (!fDsc.getSkip()) {
					lg.log("Functionality:  "+fDsc.getName());
					if (res != null) {
						numPassed += res.getPassed();
						numSkipped += res.getSkipped();
						numFailed += res.getFailed();
						lg.log("Passed:  "+res.getPassed());
						lg.log("Failed:  "+res.getFailed());
						lg.log("Skipped: "+res.getSkipped());
					} 
					else {
						lg.log("No result available");
					} 
					lg.log("-----------------------------------------------");
				} 
			} 
			timer.stop();
			updateElapsedTime();
			
			myGui.getTestsTree().showTestsHierarchy(xmlFileName, null, true);
			
			myGui.setStatus(TestSuiteGui.IDLE_STATE);
			
			lg.log("******************************************");
			String finalReport = "Final Report: "+numPassed+" passed, "+numSkipped+" skipped, "+numFailed+" failed of "+(numPassed+numSkipped+numFailed)+" executed";
			lg.log(finalReport);
			lg.log("******************************************");
			
			myGui.getStatusLabel().setText(finalReport);
			
			String gifIcon;
			if (numFailed > 0) {
				gifIcon = "failed_buttons.gif";
			} 
			else if (numSkipped > 0) {
				gifIcon = "skipped_buttons.gif";
			} 
			else {
				gifIcon = "passed_buttons.gif";
			}
			myGui.getStatusLabel().setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/"+gifIcon)));
			
			lg.addBodyCloser();
			
			// if (runAutomatic) then ask the platform to exit and exit myself
			if (System.getProperty("test.common.testSuite.TestSuiteAgent.runAutomatic") != null) {
				try {
					TestUtility.requestAMSAction(myAgent, getAMS(), new jade.domain.JADEAgentManagement.ShutdownPlatform());
					onGuiEvent(new GuiEvent(this, EXIT_EVENT));
				} catch (Exception any) {
					any.printStackTrace();
					System.exit(0);
				}
			}
			
			
			
			return 0;
		} 
	}
	
	/**
	 * INNER CLASS: SingleTesterExecutor
	 */
	class SingleTesterExecutor extends SequentialBehaviour {
		
		private FunctionalityDescriptor func = null;
		private ListProcessor lpToBeResumed;
		
		public SingleTesterExecutor(Agent a, FunctionalityDescriptor f, ListProcessor l) {
			super(a);
			func = f;
			lpToBeResumed = l;
			
			// 1st Step: Load the tester to run
			addSubBehaviour(new OneShotBehaviour(myAgent) {
				public void action() {
					Logger lg = Logger.getLogger();
					if (lg.getLoggerType() == Logger.HTML_LOGGER) {
						String title = "";
						if (firstGroup) {
							firstGroup = false;
							title = "Multiple Groups Execution";
						} 
						
						lg.addIndex(title, func.getName());
						lg.addBodyHeading(func.getName());
						lg.incBlockCounter();
					} 
					loadTester(func);
				} 
			});
			
			// 2nd Step: Run the tester
			addSubBehaviour(new Requester(myAgent, new Execute(false)) {
				public void onStart() {
					super.onStart();
					groupTestsResults = new Hashtable();
				}
				
				public int onEnd() {
					runAllTestsResults.put(currentFunc.getName(), groupTestsResults);
					return super.onEnd();
				}
			} );
			
			// 3th Step: kill the tester
			addSubBehaviour(new Requester(myAgent, new Exit()) {
				public int onEnd() {
					waitABit();
					return 0;
				} 
			});
		}
		public int onEnd() {
			Logger.getLogger().log("Test terminated");
			lpToBeResumed.resume();
			myGui.updateProgressBar(runAllSuccessful);
			return 0;
		} 
	}
	
	/**
	 INNER CLASS: CyclicReceiver
	 Handle notifications about single tests
	 */
	class CyclicReceiver extends CyclicBehaviour {
		
		private MessageTemplate templ = MessageTemplate.and(
				MessageTemplate.MatchPerformative(ACLMessage.INFORM), 
				MessageTemplate.MatchConversationId(TesterAgent.TEST_NOTIFICATION));
		
		public CyclicReceiver(Agent a) {
			super(a);
		}
		
		public void action() {
			ACLMessage      msg = myAgent.receive(templ);
			
			if (msg != null) {
				if (myGui.getStatus() == myGui.STEPPING_STATE) {
					myGui.setStatus(myGui.DEBUGGING_STATE);
					myGui.getStatusLabel().setText("Debug Mode, press Next or Run to continue");
					myGui.getStatusLabel().setIcon(new javax.swing.ImageIcon(getClass().getResource("/test/common/testSuite/gui/images/blink_buttons.gif")));
				} 
				
				try {
					Predicate p = (Predicate) getContentManager().extractContent(msg);
					if (p instanceof TestResult) {
						TestResult tr = (TestResult) p;
						groupTestsResults.put(tr.getName(), tr);
						switch (tr.getResult()) {
						case Test.TEST_PASSED:
							myGui.showPassed(tr.getName());
							break;
						case Test.TEST_FAILED:
							myGui.showFailed(tr.getName());
							runAllSuccessful = false;
							break;
						case Test.TEST_SKIPPED:
							myGui.showSkipped(tr.getName());
						}
					}
					else if (p instanceof NumberOfTests) {
						NumberOfTests not = (NumberOfTests) p;
						myGui.setTotal(not.getN());
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			} 
			else {
				block();
			} 
		} 
	}
	
	/**
	 * Inner class Requester
	 */
	class Requester extends AchieveREInitiator {
		private AgentAction requestedAction;
		
		Requester(Agent a, AgentAction act) {
			super(a, null);
			requestedAction = act;
		}
		
		protected Vector prepareRequests(ACLMessage request) {
			Vector v = new Vector();
			try {
				AID        tester = new AID(TESTER_NAME, AID.ISLOCALNAME);
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(tester);
				msg.setLanguage(codec.getName());
				msg.setOntology(TesterAgentControlOntology.getInstance().getName());
				Action aa = new Action(tester, requestedAction);
				getContentManager().fillContent(msg, aa);
				v.addElement(msg);
			} 
			catch (Exception e) {
				Logger.getLogger().logStackTrace(e);
			} 
			return v;
		} 
		
		// Store results
		protected void handleInform(ACLMessage inform) {
			if (requestedAction instanceof Execute) {
				try {
					Result     res = (Result) getContentManager().extractContent(inform);
					ArrayList  lRes = (ArrayList) res.getItems();
					ExecResult exRes = (ExecResult) lRes.get(0);
					runAllSummaries.put(currentFunc, exRes);
				} 
				catch (Exception e) {
					Logger.getLogger().logStackTrace(e);
				} 
			} 
		} 
		
		protected void handleRefuse(ACLMessage refuse) {
			Logger.getLogger().log("Tester agent action refused. Message is:");
			Logger.getLogger().log(refuse);
		} 
		
		protected void handleNotUnderstood(ACLMessage notUnderstood) {
			Logger.getLogger().log("Tester agent action not understood. Message is:");
			Logger.getLogger().log(notUnderstood);
		} 
		
		protected void handleFailure(ACLMessage failure) {
			Logger.getLogger().log("Tester agent action failed. Message is:");
			Logger.getLogger().log(failure);
		} 
	}    // END of inner class Requester
	
	
	// Main method that allows launching the TestSuiteAgent as a
	// stand-alone program
	public static void main(String[] args) {
		try {
			// Arguments
			Profile      p = new ProfileImpl(null, Test.DEFAULT_PORT, null);
			StringBuffer argsAsString = new StringBuffer();
			if (args != null) {
				for (int i = 0; i < args.length; ++i) {
					if (args[i].startsWith("-") && i < args.length-1) {
						argsAsString.append(' ');
						argsAsString.append(args[i]);
						p.setParameter(args[i].substring(1), args[i+1]);
						++i;
						argsAsString.append(' ');
						argsAsString.append(args[i]);
					} 
				} 
			} 
			// Launch the Main container in a separated process
			mainController = TestUtility.launchJadeInstance("Main", null, "-gui -nomtp -local-port "+Test.DEFAULT_PORT+" -services "+MAIN_SERVICES+" -name "+TEST_PLATFORM_NAME+argsAsString+" -jade_domain_df_autocleanup true", null);
			
			// Get a hold on JADE runtime
			Runtime rt = Runtime.instance();
			
			// Exit the JVM when there are no more containers around
			rt.setCloseVM(true);
			
			p.setParameter(Profile.MAIN, "false");
			p.setParameter(Profile.SERVICES, "jade.core.event.NotificationService;jade.core.mobility.AgentMobilityService;jade.core.replication.AddressNotificationService;jade.core.messaging.TopicManagementService");
			p.setSpecifiers(Profile.MTPS, new ArrayList());
			
			ContainerController  mc = rt.createAgentContainer(p);
			
			AgentController testSuite = mc.createNewAgent(NAME, TestSuiteAgent.class.getName(), null);
			testSuite.start();
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
	} 
	
	/**
	 * Utility method to compute elapsed time and update the GUI for current TestGroup execution
	 */
	private void updateElapsedTime() {
		myGui.setElapsedTime(System.currentTimeMillis() - startTime);
	} 
	
	/**
	 * Utility method to wait one sec without annoying exceptions to catch
	 */
	private static void waitABit() {
		try {
			// jjj pac
			Thread.sleep(100);
		} 
		catch (Exception e) {
			Logger.getLogger().logStackTrace(e);
		} 
	} 
}
