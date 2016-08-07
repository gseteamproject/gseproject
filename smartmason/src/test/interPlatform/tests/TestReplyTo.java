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
import jade.domain.FIPAAgentManagement.*;
import jade.util.leap.Iterator;
import test.common.*;
import test.interPlatform.InterPlatformCommunicationTesterAgent;

/**
 Test the correct handling of the :reply-to slot of an ACL message.
 The Tester agent sends a message to agent A specifying B in the reply-to 
 field. Agent B waits for a message from A and then notifies the Tester.
 @author Giovanni Caire - TILAB
 */
public class TestReplyTo extends Test {
	private static final String AGENT_A = "A";
	private static final String AGENT_B = "B";
	private static final String CONV_ID = "conv_ID";
	
	private AID agentA, agentB;
	private JadeController jc;
	
	public Behaviour load(Agent a) throws TestException { 
		// The test must complete in 10 sec
		setTimeout(10000);
		
		// Start a remote container
		jc = TestUtility.launchJadeInstance("Container-1", null, "-container -host "+TestUtility.getContainerHostName(a, null)+" -port "+Test.DEFAULT_PORT, null);
		// Start Agent A on the remote container
		agentA = TestUtility.createAgent(a, AGENT_A, getClass().getName()+"$ResponderAgent", null, a.getAMS(), jc.getContainerName());
		// Start agent B on a remote platform
		AID remoteAMS = (AID) getGroupArgument(InterPlatformCommunicationTesterAgent.REMOTE_AMS_KEY);
		agentB = TestUtility.createAgent(a, AGENT_B, getClass().getName()+"$ResponderAgent", null, remoteAMS, null);
		
		Behaviour b = new Behaviour(a) {
			public boolean finished = false;
			
			public void onStart() {
				// Send a message to A
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(agentA);
				msg.addReplyTo(agentB);
				msg.setConversationId(CONV_ID);
				System.out.println("Sending message "+msg);
				myAgent.send(msg);
			}
			
			public void action() {
				// Waits for a reply from B
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId(CONV_ID));
				if (msg != null) {
					log("Reply received.");
					if (msg.getSender().equals(agentB)) {
						passed("Reply correct.");
					}
					else {
						failed("Unexpected reply sender "+msg.getSender().getName());
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
	
	public void clean(Agent a) {
		try {
			jc.kill();
			TestUtility.killAgent(a, agentB);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 Inner class ResponderAgent.
	 This agent receives a message from the tester and simply sends
	 a reply (created using the createReply() method).
	 */
	public static class ResponderAgent extends Agent {
		protected void setup() {
			
			addBehaviour(new CyclicBehaviour(this) {
				public void action() {
					ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId(CONV_ID));
					if (msg != null) {
						System.out.println(getLocalName()+": Responder received test message.");
						ACLMessage reply = msg.createReply();
						reply.addReplyTo(msg.getSender());
						reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						myAgent.send(reply);
					}
					else {
						block();
					}
				}
			} );
			
			System.out.println(getLocalName()+": Responder started.");
		}
	} // END of inner class ResponderAgent
}

