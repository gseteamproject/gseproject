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
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

import test.common.xml.TestDescriptor;
import test.common.testerAgentControlOntology.TestResult;


/**
   Generic behaviour that executes the tests included in a 
   <code>TestGroup</code> object
   @author Giovanni Caire - TILAB
 */
public class TestGroupExecutor extends FSMBehaviour {
 
	// State names 
	private static final String INIT_TEST_GROUP_STATE = "Init-test-group";
	private static final String LOAD_TEST_STATE = "Load-test";
	private static final String CLEAN_TEST_STATE = "Clean-test";
	private static final String EXECUTE_TEST_STATE = "Execute-test";
	private static final String HANDLE_RESULT_STATE = "Handle-result";
	private static final String END_STATE = "End";
	private static final String PAUSE_STATE = "Pause";
	
	// Data store key for the result of a test
	private static final String TEST_RESULT_KEY = "_test-result_";
	
	// Exit values from the LOAD_TEST_STATE
	private static final int EXIT = 0;
	private static final int EXECUTE = 1;
	private static final int SKIP = 2;
	private static final int PAUSE = 3;
	private static final int ABORT = 4;
	
	private boolean aborted = false;
	
	// Counters for statistics
	protected int passedCnt = 0;
	protected int failedCnt = 0;
	protected int skippedCnt = 0;
	
	// The pool of tests to be executed 
	private TestGroup tests;
	
	// The test object that is currently in execution
	private Test currentTest;

	// Flag indicating whether debug (step-by-step) execution is selected
	private boolean debugMode = false;
	// Flag indicating whether the execution is currently paused
	private boolean inPause = false;
	
	public TestGroupExecutor(Agent a, TestGroup tg) {
		super(a);
		
		if (tg == null) {
			throw new IllegalArgumentException("Null test group");
		}
		tests = tg;
		
		// Transition table
		registerDefaultTransition(INIT_TEST_GROUP_STATE, LOAD_TEST_STATE);
		registerTransition(INIT_TEST_GROUP_STATE, END_STATE, ABORT);
		registerTransition(LOAD_TEST_STATE, EXECUTE_TEST_STATE, EXECUTE);
		registerTransition(LOAD_TEST_STATE, CLEAN_TEST_STATE, SKIP);
		registerTransition(LOAD_TEST_STATE, END_STATE, EXIT);
		registerTransition(LOAD_TEST_STATE, PAUSE_STATE, PAUSE);
		registerDefaultTransition(PAUSE_STATE, EXECUTE_TEST_STATE);
		registerDefaultTransition(EXECUTE_TEST_STATE, HANDLE_RESULT_STATE);
		registerDefaultTransition(HANDLE_RESULT_STATE, CLEAN_TEST_STATE);
		registerDefaultTransition(CLEAN_TEST_STATE, LOAD_TEST_STATE);
		
		// INIT_TEST_GROUP_STATE
		Behaviour b = new OneShotBehaviour() {
			public void action() {
				try {
					tests.initialize(myAgent);
				}
				catch (TestException te) {
					log("Error in TestGroup initialization. Abort");
					te.printStackTrace();
					aborted = true;
				}
			}
			
			public int onEnd() {
				return (aborted ? ABORT : -1);
			}
		};
		registerFirstState(b, INIT_TEST_GROUP_STATE);

		// LOAD_TEST_STATE
		b = new OneShotBehaviour() {
			private int ret;
			
			public void action() {
				try {
					currentTest = tests.next();
					if (currentTest != null) {
						ret = EXECUTE;
            TestDescriptor td = currentTest.getDescriptor();

            log("--------------------------------------------");                        
            log("Executing test: "+td.getName());
            log("WHAT: "+td.getWhat());
            log("HOW:  "+td.getHow());
            log("PASSED WHEN: "+td.getPassedWhen());
            Logger.loggedCurrentTest = td.getName();
					
						Behaviour b2 = currentTest.load(myAgent, getDataStore(), TEST_RESULT_KEY);
						registerState(b2, EXECUTE_TEST_STATE);
						
						if (debugMode) {
							ret = PAUSE;
							inPause = true;
						}
					}
					else {
						// When next() returns null there are no more tests to execute
						ret = EXIT;
					}
				}
				catch (TestException te) {
					boolean expected = false;
					if (te instanceof SkippedException) {
						SkippedException se = (SkippedException) te;
						expected = se.getExpected();
						currentTest = new DummyTest(se.getDescriptor());
					}
					
					if (!expected) {
						// Some problems occured initializing this test. Skip it
						log("Problems in test initialization ["+te.getMessage()+"]");
						log("Skip this test.");
						skippedCnt++;
						currentTest.setErrorMsg(te.getMessage());
					}
					else {
						currentTest.setErrorMsg("Set to skipped");
					}
					     
          sendTestResultNotification(Test.TEST_SKIPPED);
					ret = SKIP;
				}
			}
			
			public int onEnd() {
				if (ret == EXECUTE) {
					// Before entering the execution state flush messages that
					// may have been left into the queue and that may confuse 
					// the test execution
					flushMessageQueue();
				}
				return ret;
			}
		};
		b.setDataStore(getDataStore());
		registerState(b, LOAD_TEST_STATE);

		// HANDLE_RESULT_STATE
		b = new OneShotBehaviour() {
			public void action() {
				int result = Test.NOT_AVAILABLE;
				try {
					Integer i = (Integer) getDataStore().get(TEST_RESULT_KEY);
					if (i != null) {
						result = i.intValue();
					}
				}
				catch (Exception e) {
					System.out.println("TestGroupExecutor exception");
					e.printStackTrace();
				}
  			
				if (result == Test.TEST_PASSED) {
  				log("Test PASSED");
  				passedCnt++;
  				
  				sendTestResultNotification(Test.TEST_PASSED);
  			}
  			else if (result == Test.TEST_FAILED) {
  				log("Test FAILED");
  				failedCnt++;
  				
  				sendTestResultNotification(Test.TEST_FAILED);
  			}
  			else {
  				log("WARNING: Test result not available!!!");
  				skippedCnt++;
  			}
			}			
		};
		b.setDataStore(getDataStore());
		registerState(b, HANDLE_RESULT_STATE);

		// CLEAN_TEST_STATE
		b = new OneShotBehaviour() {
			public void action() {
				if (currentTest!= null) {
	  			try {
			  		currentTest.clean(myAgent);
	  			}
	  			catch (Exception e) {
	  				// Just print a warning
	  				log("Warning: Exception in test cleaning ["+e.getMessage()+"]");
	  			}
	  			finally {
	  				try {
	  					Thread.sleep(1000);
	  				}
	  				catch (Exception e ) { e.printStackTrace(); }
	  			}
				}
			}
		};
		b.setDataStore(getDataStore());
		registerState(b, CLEAN_TEST_STATE);
  			
		// END_STATE
		b = new OneShotBehaviour() {
			public void action() {
				if (!aborted) {
					StringBuffer sb = new StringBuffer("\n--------------------------------------------\n");
					sb.append("--------------------------------------------\n");
    			sb.append("Test summary:\n");
    			sb.append(passedCnt+" tests PASSED\n");
    			sb.append(failedCnt+" tests FAILED\n");
	    		if (skippedCnt > 0) {
  	  			sb.append(skippedCnt+" tests SKIPPED due to initialization/termination problems\n");
    			}	
    			log(sb.toString());
    		
					tests.shutdown(myAgent);
				}
			}			
		};
		b.setDataStore(getDataStore());
		registerLastState(b, END_STATE);
	
		// PAUSE_STATE
		b = new SimpleBehaviour() {
			private boolean finished;
			
			public void action() {
				if (inPause) {
					block();
					finished = false;
				}
				else {
					finished = true;
				}
			}	
			
			public boolean done() {
				return finished;
			}
			
			public int onEnd() {
				// Before entering the execution state flush messages that
				// may have been left into the queue and that may confuse 
				// the test execution
				flushMessageQueue();
				return 0;
			}
		};
		b.setDataStore(getDataStore());
		registerState(b, PAUSE_STATE);
	}
	
	void resume() {
		inPause = false;
		super.restart();
	}
	
	void setDebugMode(boolean b) {
		debugMode = b;
	}
        
 
  private void sendTestResultNotification(int result) {
    String name = currentTest.getDescriptor().getName();
    String errorMsg = null;
    if (result != Test.TEST_PASSED) {
    	errorMsg = currentTest.getErrorMsg();
    }
  	TestResult tr = new TestResult(name, result, errorMsg);
  	((TesterAgent) myAgent).notifyController(tr);
  }
	
	private void flushMessageQueue() {
		while (myAgent.receive() != null) {
			;
		}
	}
	
	private void log(String s) {
		Logger.getLogger().log(s);
	}
	
	/**
	   Inner class DummyTest.
	   This class is used to treat in a uniform way the notification
	   of performed and skipped tests 
	 */
	private class DummyTest extends Test {
		private DummyTest(TestDescriptor td) {
			super();
			setDescriptor(td);
		}
	}
	
}

