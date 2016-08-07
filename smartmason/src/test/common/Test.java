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
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.*;
import jade.core.behaviours.DataStore;
import test.common.xml.TestDescriptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
   Common base class for all test classes designed to be 
   executed within the JADE test suite framework
   @author Giovanni Caire - TILAB
 */
public abstract class Test implements Serializable {
	/** Constant value indicating that a test has been completed successfully */
	public static final int TEST_PASSED = 1;
	/** Constant value indicating that a test has NOT been completed successfully */
	public static final int TEST_FAILED = 0;
	/** Constant value indicating that a test has been skipped */
	public static final int TEST_SKIPPED = 2;
	/** Constant value indicating that the test result is not available */
	public static final int NOT_AVAILABLE = -1;

	public static final int DEFAULT_PORT = 1099;

	public static final String DEFAULT_PROTO = "http";
	public static final String DEFAULT_MTP = "jade.mtp.http.MessageTransportProtocol";

	private TestGroup myGroup;
	private TestDescriptor myDescriptor;

	private Wrapper myWrapper;
	private DataStore myStore;
	private String myKey;
	private String errorMsg = null;

	private long timeout = -1;

	private boolean pauseEnabled = false;
	private static MessageTemplate resumeTemplate = MessageTemplate.MatchContent("resume");
	
	public static SimpleDateFormat logTimeFormatter = new SimpleDateFormat("HH:mm:ss.SSS");

	/**
     Specific tests must re-define this method to perform test specific 
     initializations and to create the <code>Behaviour</code> that will
     actually perform the test.
     @param a The agent that executes the test
     @return the <code>Behaviour</code> that will actually perform the test
	 */
	public Behaviour load(Agent a) throws TestException {
		return null;
	}

	/**
     This method is not intended to be called or redefined by test developers
	 */
	public Behaviour load(Agent a, DataStore ds, String resultKey) throws TestException {
		myStore = ds;
		myKey = resultKey;
		Behaviour b = load(a);
		if (timeout > 0) {
			ParallelBehaviour pb = new ParallelBehaviour(a, ParallelBehaviour.WHEN_ANY);
			pb.addSubBehaviour(b);
			pb.addSubBehaviour(new WatchDog(a, timeout));
			myWrapper = new Wrapper(a, pb);
		}
		else {
			myWrapper = new Wrapper(a, b);
		}
		return myWrapper;
	}

	/**
     Specific tests can re-define this method to perform test specific 
     clean-up operations.
     @param a The agent that executes the test
	 */
	public void clean(Agent a) {
	}

	/**
     Retrieve an argument of the <code>TestGroup</code> this 
     <code>Test</code> belongs to.
     Group arguments may be set at run-time by the 
     user through the "Set arguments" button of the TestSuite Toolbar.
     When needed a <code>TestGroup</code> can also directly set
     group arguments from within its <code>initialize()</code> method.     
     Group arguments are available to all tests in the same group.
     @param key The key identifying the group argument to be retrieved
     @return The value of the group argument identified by <code>key</code>
	 */
	protected final Object getGroupArgument(String key) {
		return myGroup.getArgument(key);
	}

	/**
     Retrieve an argument of this <code>Test</code>.
     Test arguments can be set within the xml configuration file
     describing this test by adding elements of type <argument>.
     Unlike group arguments, test arguments are only available to 
     the test they refer to.
     @param key The key identifying the test argument to be retrieved
     @return The value of the test argument identified by <code>key</code>
	 */
	protected final String getTestArgument(String key) {
		return myDescriptor.getArg(key);
	}

	/**
     Specify a timeout for the test. If test execution does not 
     complete within the specified timeout the test automatically 
     fails.
	 */
	protected void setTimeout(long t) {
		timeout = t;
	}

	/**
     Terminates the tests marking it as successful.
     @param msg A test completion message that is appended to the log.
	 */
	protected final void passed(String msg) {
		log(msg);
		myStore.put(myKey, new Integer(TEST_PASSED));
		myWrapper.stop();
	}

	/**
     Terminates the tests marking it as failed.
     @param msg An error message that is appended to the log and can
     be viewed in the test suite GUI.
	 */
	protected final void failed(String reason) {
		log(reason);
		errorMsg = reason;
		myStore.put(myKey, new Integer(TEST_FAILED));
		myWrapper.stop();
	}

	/**
     @return true if this test has already been marked as failed by means of 
     a call to the <code>failed()</code> method
	 */
	protected final boolean isFailed() {
		Integer ii = (Integer) myStore.get(myKey);
		return (ii != null && ii.intValue() == TEST_FAILED);
	}

	/**
     Log a given String
	 */
	protected final void log(String s) {
		String time = logTimeFormatter.format(new Date());
		String separator = s.startsWith("---") ? " " : "--- ";
		Logger.getLogger().log(time+separator+s);
	}

	protected final void pause() {
		if (pauseEnabled) { 
			myWrapper.pause();
			log("Test paused. Send a REQUEST message with content \"resume\" to go on");
		}
	}

	protected final void enablePause(boolean b) {
		pauseEnabled = b;
	}

	//////////////////////////////////////
	// Private and package-scoped methods
	//////////////////////////////////////

	/** 
     Only called by the TestGroupExecutor
	 */
	TestDescriptor getDescriptor() {
		return myDescriptor;
	}

	/** 
     Only called by the TestGroupExecutor
	 */
	String getErrorMsg() {
		return errorMsg;
	}

	/** 
     Only called by the TestGroupExecutor
	 */
	void setErrorMsg(String msg) {
		errorMsg = msg;
	}

	/** 
     Only called by the TestGroup
	 */
	void setDescriptor(TestDescriptor td) {
		myDescriptor = td;
	}

	void setGroup(TestGroup tg) {
		myGroup = tg;
	}

	/**
     Inner class Wrapper
	 */
	private class Wrapper extends SerialBehaviour {
		private boolean stopped = false;
		private Behaviour wrapped;
		private Behaviour paused;
		private List children = new ArrayList(1);

		private Wrapper(Agent a, Behaviour b) {
			super(a);
			wrapped = b;
			registerAsChild(wrapped);
			children.add(wrapped);
		}

		protected void scheduleFirst() {
		}

		protected void scheduleNext(boolean currentDone, int currentResult) {
			if (stopped) {
				// If the Wrapper was stopped from the outside, at next round we must not execute the wrapped behaviour anymore
				wrapped = null;
			}
		}

		protected boolean checkTermination(boolean currentDone, int currentResult) {
			return stopped || currentDone;
		}

		protected Behaviour getCurrent() {
			return wrapped;
		}

		public Collection getChildren() {
			return children;
		}

		private void stop() {
			stopped = true;
			// In case this is called by a separated thread
			restart();
		}

		private void pause() {
			paused = wrapped;
			wrapped = new CyclicBehaviour(myAgent) {
				public void action() {
					ACLMessage msg = myAgent.receive(resumeTemplate);
					if (msg != null) {
						wrapped = paused;
					}
					else {
						block();
					}
				}
			};
		}

		public void reset() {
			stopped = false;
			super.reset();
		}  	
	} // END of inner class Wrapper


	/**
     Inner class WatchDog.
	 */
	private class WatchDog extends WakerBehaviour {
		private WatchDog(Agent a, long t) {
			super(a, t);
		}

		protected void handleElapsedTimeout() {
			failed("Timeout expired");
		}
	} // END of inner class WatchDog  		
}

