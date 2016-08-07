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

package test.interPlatform.tests;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.util.leap.Properties;
import test.common.*;
import test.interPlatform.InterPlatformCommunicationTesterAgent;

/**
   Test sending and receiving messages across different platforms.
   @author Giovanni Caire - TILAB
 */
public class TestRemotePing extends Test {
	private static final String RESPONDER_NAME = "responder";
	private final String CONV_ID = "conv_ID"+hashCode();
	private final String CONTENT = "\"PING\"";
	private final String USER_DEF_KEY = "U_KEY";
	private final String USER_DEF_VALUE = "U_VALUE";
	
	private AID resp = null;
	
  public Behaviour load(Agent a) throws TestException { 
  	// The test must complete in 10 sec
  	setTimeout(10000);
  	
  	try {
			AID remoteAMS = (AID) getGroupArgument(InterPlatformCommunicationTesterAgent.REMOTE_AMS_KEY);
			resp = TestUtility.createAgent(a, RESPONDER_NAME, TestUtility.CONFIGURABLE_AGENT, null, remoteAMS, null);
  		TestUtility.addBehaviour(a, resp, "test.common.behaviours.NotUnderstoodResponder");
			log("Responder correctly started on remote platform");
  		
  		Behaviour b = new SimpleBehaviour() {
  			private boolean finished = false;
  			public void onStart() {
  				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
  				msg.addReceiver(resp);
  				msg.setConversationId(CONV_ID);
  				msg.setContent(CONTENT);
  				msg.addUserDefinedParameter(USER_DEF_KEY, USER_DEF_VALUE);
  				myAgent.send(msg);
  			}
  			
  			public void action() {
  				ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId(CONV_ID)); 
					if (msg != null) { 
						AID sender = msg.getSender();
						Properties pp = msg.getAllUserDefinedParameters();
						if (!sender.equals(resp)) {
							failed("Unexpected reply sender "+sender.getName());
						} 
						else if (msg.getPerformative() != ACLMessage.NOT_UNDERSTOOD) {
							failed("Unexpected reply performative "+msg.getPerformative());
						}
						else if (!CONTENT.equals(msg.getContent())) {
							failed("Unexpected reply content "+msg.getContent());
						}
						else if (pp.size() != 1) {
							failed(pp.size()+" user defined parameters found while 1 was expected");
						}
						else if (!USER_DEF_VALUE.equals(msg.getUserDefinedParameter(USER_DEF_KEY))) {
							failed("Unexpected user defined parameter "+msg.getUserDefinedParameter(USER_DEF_KEY));
						}
						else {
							passed("Reply message correctly received");
						}
						finished = true;
  				}
  				else {
  					block();
  				}
  			}
  			
  			public boolean done() {
  				return finished;
  			}
  		};
  	  		
  		return b;
  	}
  	catch (TestException te) {
  		throw te;
  	}
  	catch (Exception e) {
  		throw new TestException("Error loading test", e);
  	}
  }
					
  public void clean(Agent a) {
  	try {
  		TestUtility.killAgent(a, resp);
  		Thread.sleep(1000);
  	}
  	catch (Exception e) {
  		e.printStackTrace();
  	}
  }
  	
}

