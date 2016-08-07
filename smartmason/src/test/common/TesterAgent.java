/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

package test.common;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.util.leap.List;
import jade.util.leap.ArrayList;
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.content.onto.basic.*;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.proto.AchieveREResponder;
import jade.proto.states.ReplySender;
import test.common.testSuite.gui.SelectTestsDlg;
import test.common.testSuite.gui.InsertArgumentsDlg;
import test.common.testerAgentControlOntology.*;
import test.common.Logger;
import test.common.xml.TestDescriptor;

/**
   Base class for agents performing tests within the JADE 
   test suite framework.
   A <code>TesterAgent</code> is in charge of testing a given 
   functionality within the system under test and does that by 
   performing a group of tests implemented 
   as an instance of the <code>TestGroup</code> class.
   Each test in the group focuses on a specific case
   within the addressed functionality and is implemented as an instance
   of the Test class.
   <br>
   A TesterAgent just keeps all the tests included in its TestGroup 
   and executes them in sequence so that in general implementing a 
   TesterAgent for a given functionality is as simple as redefining 
   the <code>getTestGroup()</code> methods so that it returns the
   proper TestGroup.
   The tests in a <code>TestGroup</code> are described into an xml file.
   The sample code below shows how a TesterAgent in charge of testing 
   the FFF functionality looks like.
   
	 <pr><hr><blockquote><pre>
	 public class FFFTesterAgent extends TesterAgent {
	 	protected TestGroup getTestGroup() {
	 		return new TestGroup(<xml file name>) {
	 			protecetd void initialize(Agent a) throws TestException {
	 				// Put here all initialization operations common to all 
	 				// tests in the group
	 			}
	 			
	 			protected void shutdown(Agent a) {
	 				// Put here all clean-up operations common to all 
	 				// tests in the group
	 			}
	 		};
	 	}
	 }				
	 </pre></blockquote><hr>
	 
	 @author Giovanni Caire -TILAB
 */
public abstract class TesterAgent extends Agent {
  public static final String TEST_NOTIFICATION = "test-notification";
  
  private Codec codec = new SLCodec();
  private Ontology onto = TesterAgentControlOntology.getInstance();  
  
	private TestGroup theTestGroup = null;
	
	private boolean remoteControlMode = false;
	private AID remoteControllerAID = null;
	private ACLMessage exitNotification = null;
	
	protected void setup() {	
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(onto);
		
		// Get the execution mode (passed as agent parameter)
		Object[] args = getArguments();
		try {
			if ("true".equalsIgnoreCase((String) args[0])) {
				remoteControlMode = true;
			}
			String c = (String) args[1];
			remoteControllerAID = new AID(c, AID.ISLOCALNAME);
			System.out.println("TesterAgent "+getLocalName()+" running in controlled mode: controller is "+c);
		}
		catch (Exception e) {
			// Just do nothing --> stand-alone execution mode
			System.out.println("TesterAgent "+getLocalName()+" running in stand-alone mode");
		}

		// Load the TestGroup to execute
		theTestGroup = getTestGroup();

		theTestGroup.setArguments(theTestGroup.getArgumentsSpecification());

		if (remoteControlMode) {
			// REMOTE CONTROL EXECUTION MODE
			// Add the behaviour that handles commands from a remote 
			// controller agent (usually the JADE TestSuiteAgent)
			addBehaviour(new Controller(this));
			
			// Notify the remote controller about the number of tests in the group
			notifyController(new NumberOfTests(theTestGroup.size()));
		}
		else {
			// STAND-ALONE EXECUTION MODE
			// Directly add the behaviour that executes the test group
			addBehaviour(new TestGroupExecutor(this, theTestGroup) {
				public int onEnd() {
					myAgent.doDelete();
					return 0;
				}
			} );
		}
	}	
		
	protected void takeDown() {
		if (remoteControlMode) {
			// Send a notification to the remote controller agent
			// (usually the JADE TestSuiteAgent)
			if (exitNotification != null) {
				send(exitNotification);
			}
		}
		else {
			// Otherwise notify the user via stdout
			Logger.getLogger().log("Exit...");
      Logger.getLogger().closeLogger();
		}
	}
	
	/**
	   Subclasses should implement this method to define the test
	   group to be executed by this TesterAgent
	 */
	protected abstract TestGroup getTestGroup();
  
	// This is package-scoped since it is also called by the TestGroupExecutor
  void notifyController(Predicate p) {
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);    
    msg.addReceiver(remoteControllerAID);
    msg.setLanguage(codec.getName());
    msg.setOntology(onto.getName());
    msg.setConversationId(TEST_NOTIFICATION);
    try {
    	getContentManager().fillContent(msg, p);
	    send(msg);
    }
    catch (Exception e) {
    	e.printStackTrace();
    }
  }

	/**
	   Inner class Controller. This is the behaviour that handles 
	   execution control commands from a remote controller Agent 
	   (usually the JADE TestSuiteAgent)
	 */
	class Controller extends AchieveREResponder {
		private TestGroupExecutor executor;
		private Action aa;
		private AgentAction requestedAction;
		
		Controller(Agent a) {
			super(a, MessageTemplate.MatchOntology(TesterAgentControlOntology.getInstance().getName()));
		}
		
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
			try {
				aa = (Action) myAgent.getContentManager().extractContent(request);
				requestedAction = (AgentAction) aa.getAction();				
			}
			catch (Exception e) {
				throw new NotUnderstoodException(e.getMessage());
			}
			return null;
		}
		
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {			
			if (requestedAction instanceof Execute) {
				// EXECUTE action: add a behaviour that executes the test group. This 
				// behaviour is also in charge of sending back the notification when
				// the execution is completed --> return null!
				final ACLMessage req = request;
				theTestGroup.reset();
				executor = new TestGroupExecutor(TesterAgent.this, theTestGroup) {
					public int onEnd() {
						executor = null;
						ACLMessage notification = createInform(aa, req, passedCnt, failedCnt, skippedCnt);
						ReplySender.adjustReply(myAgent, notification, req);
						myAgent.send(notification);
						return 0;
					}
				};
				executor.setDebugMode(((Execute) requestedAction).getDebugMode());
				myAgent.addBehaviour(executor);
				return null;
			}
			else if (requestedAction instanceof Configure) {
				// CONFIGURE action: 
				List args = theTestGroup.getArgumentsSpecification();
				if (args != null && args.size() > 0) { 
					InsertArgumentsDlg.insertValues(args);
				}
				theTestGroup.setArguments(args);
				return createInform(aa, request);
			}
			else if (requestedAction instanceof SelectTests) {
				// SELECT_TESTS action: 
				TestDescriptor[] descriptors = theTestGroup.getDescriptors();
				if (descriptors != null && descriptors.length > 0) { 
					SelectTestsDlg.selectTests(descriptors);
				}
				return createInform(aa, request);
			}
			else if (requestedAction instanceof Resume) {
				// RESUME action: 
				if (executor != null) {
					executor.setDebugMode(((Resume) requestedAction).getDebugMode());
					executor.resume();
					return createInform(aa, request);
				}
				else {
					throw new FailureException("Test group not in execution");
				}
			}
			else if (requestedAction instanceof Exit) {
				// EXIT action: Make this agent terminate. The notification message 
				// will be sent in the takedown method --> return null
				exitNotification = createInform(aa, request);
				ReplySender.adjustReply(myAgent, exitNotification, request);
				myAgent.doDelete();
				return null;
			}
			else {
				throw new FailureException("Unknown action");
			}
		}  // End of method prepareResultNotification()
		
		private ACLMessage createInform(AgentAction aa, ACLMessage request) {
			ACLMessage inform = request.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			try {
				Done d = new Done(aa);
				myAgent.getContentManager().fillContent(inform, d);
			}
			catch (Exception e) {
				Logger.getLogger().logStackTrace(e);
			}
			return inform;
		}

		private ACLMessage createInform(AgentAction aa, ACLMessage request, int passedCnt, int failedCnt, int skippedCnt) {
			ACLMessage inform = request.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			try {
				ExecResult er = new ExecResult(passedCnt, failedCnt, skippedCnt);
				List l = new ArrayList();
				l.add(er);
				Result r = new Result(aa, l);
				myAgent.getContentManager().fillContent(inform, r);
			}
			catch (Exception e) {
				Logger.getLogger().logStackTrace(e);
			}
			return inform;
		}
	}  // END of inner class Controller
}