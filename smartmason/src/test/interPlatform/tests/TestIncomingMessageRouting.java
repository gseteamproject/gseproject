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
   Test the "incoming message routing" mechanism i.e. the case where
   a message is delivered to an agent on a remote platform and living
   on a container different from that where the MTP that receives the 
   message is installed.
   @author Giovanni Caire - TILAB
 */
public class TestIncomingMessageRouting extends Test {
	private static final String RESPONDER_NAME = "responder";
	private final String CONV_ID = "conv_ID"+hashCode();
	private final String CONTENT = "\"PING\"";
	private JadeController jc;
	
	private AID resp = null;
	
  public Behaviour load(Agent a) throws TestException { 
  	// The test must complete in 10 sec
  	setTimeout(10000);
  	
  	try {
  		// Get the remote AMS as group argument
			AID remoteAMS = (AID) getGroupArgument(InterPlatformCommunicationTesterAgent.REMOTE_AMS_KEY);
  		
			// Start a peripheral container in the remote platform
  		String host = TestUtility.getContainerHostName(a, remoteAMS, null);
			String port = InterPlatformCommunicationTesterAgent.REMOTE_PLATFORM_PORT;
  		jc = TestUtility.launchJadeInstance("Remote-Container", null, "-container -host "+host+" -port "+port, null);
			log("Peripheral container correctly created on remote platform");

  		// Create a responder agent on the remote container
			resp = TestUtility.createAgent(a, RESPONDER_NAME, "test.interPlatform.tests.TestIncomingMessageRouting$PingAgent", null, remoteAMS, jc.getContainerName());
			log("Responder correctly started on remote platform");
  		
  		Behaviour b = new SimpleBehaviour() {
  			private boolean finished = false;
  			public void onStart() {
  				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
  				msg.addReceiver(resp);
  				msg.setConversationId(CONV_ID);
  				msg.setContent(CONTENT);
  				myAgent.send(msg);
  			}
  			
  			public void action() {
  				ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId(CONV_ID)); 
					if (msg != null) { 
						AID sender = msg.getSender();
						if (!sender.equals(resp)) {
							failed("Unexpected reply sender "+sender.getName());
						} 
						else if (msg.getPerformative() != ACLMessage.NOT_UNDERSTOOD) {
							failed("Unexpected reply performative "+msg.getPerformative());
						}
						else if (!CONTENT.equals(msg.getContent())) {
							failed("Unexpected reply content "+msg.getContent());
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
  		jc.kill();
  	}
  	catch (Exception e) {
  		e.printStackTrace();
  	}
  }

  /**
     Inner class PingAgent
   */
  public static class PingAgent extends Agent {
  	protected void setup() {
  		addBehaviour(new CyclicBehaviour(this) {
  			public void action() {
  				ACLMessage msg = myAgent.receive();
  				if (msg != null) {
  					ACLMessage reply = msg.createReply();
  					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
  					reply.setContent(msg.getContent());
  					myAgent.send(reply);
  				}
  				else {
  					block();
  				}
  			}
  		} );
  	}
  }
}

