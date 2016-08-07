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
   Test the correct transfer of the Message Envelope in communication 
   across different platforms.
   @author Giovanni Caire - TILAB
 */
public class TestEnvelope extends Test {
	private static final String RESPONDER_NAME = "responder";
	private static final String CONV_ID = "conv_ID";
	
	private static final TestSerializable TEST_SERIALIZABLE = new TestSerializable("XXXX", 1);
	private static final String TEST_STRING = "Test";
	
	private static final String KEY1 = "k1";
	private static final String KEY2 = "k2";
	
	private AID resp = null;
	
  public Behaviour load(Agent a) throws TestException { 
  	// The test must complete in 10 sec
  	setTimeout(10000);
  	
  	try {
			AID remoteAMS = (AID) getGroupArgument(InterPlatformCommunicationTesterAgent.REMOTE_AMS_KEY);
			resp = TestUtility.createAgent(a, RESPONDER_NAME, getClass().getName()+"$CheckEnvelopeAgent", null, remoteAMS, null);
			log("Responder correctly started on remote platform");
			
  		Behaviour b = new SimpleBehaviour() {
  			private boolean finished = false;
  			public void onStart() {
  				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
  				msg.setConversationId(CONV_ID);
  				msg.setDefaultEnvelope();
  				Envelope env = msg.getEnvelope();
  				env.addTo(resp);
  				env.addProperties(new Property(KEY1, TEST_SERIALIZABLE));
  				env.addProperties(new Property(KEY2, TEST_STRING));
  				myAgent.send(msg);
  			}
  			
  			public void action() {
  				ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId(CONV_ID)); 
					if (msg != null) { 
						AID sender = msg.getSender();
						if (sender.equals(resp) && msg.getPerformative() == ACLMessage.INFORM) {
							passed("Message envelope OK");
						}
						else {
							failed("Wrong message envelope: "+msg.getContent());
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

  /**
     Inner class CheckEnvelopeAgent.
     This agent receives a message from the tester and checks that the 
     Envelope is correct.
   */
  public static class CheckEnvelopeAgent extends Agent {
  	protected void setup() {
  		System.out.println("Responder started.");
  		ACLMessage msg = blockingReceive(MessageTemplate.MatchConversationId(CONV_ID));
  		System.out.println("Responder received test message.");
  		ACLMessage reply = msg.createReply();
  		reply.setPerformative(ACLMessage.FAILURE);
  		
  		try {
	  		Envelope env = msg.getEnvelope();
	  		Iterator it = env.getAllProperties();
	  		boolean serializableOK = false;
	  		boolean stringOK = false;
	  		int cnt = 0;
	  		while (it.hasNext()) {
	  			Property p = (Property) it.next();
	  			cnt++;
	  			String key = p.getName();
	  			if (key.equals(KEY1)) {
	  				if (p.getValue().equals(TEST_SERIALIZABLE)) {
	  					serializableOK = true;
	  				}
	  				else {
	  					reply.setContent("Wrong Serializable property value.");
	  					break;
	  				}
	  			}
  				else if (key.equals(KEY2)) {
	  				if (p.getValue().equals(TEST_STRING)) {
	  					stringOK = true;
	  				}
	  				else {
	  					reply.setContent("Wrong String property value.");
	  					break;
	  				}
  				}
  				else {
  					reply.setContent("Unexpected property key "+key);
  					break;
  				}
	  		}
	  		if (cnt == 2) {
		  		if (serializableOK && stringOK) {
		  			reply.setPerformative(ACLMessage.INFORM);
		  		}
	  		}
	  		else {
					reply.setContent(cnt+" properties found while 2 were expected");
	  		}	
  		}
  		catch (Throwable t) {
  			reply.setContent("Exception parsing envelope properties "+t.toString());
  			t.printStackTrace();
  		}
  		send(reply);
  	}
  } // END of inner class CheckEnvelopeAgent
  
  /**
     Inner class TestSerializable
   */
  static class TestSerializable implements java.io.Serializable {
  	private String s;
  	private int n;
  	
  	TestSerializable(String s, int n) {
  		this.s = s;
  		this.n = n;
  	}
  	
  	public boolean equals(Object obj) {
  		if (obj instanceof TestSerializable) {
  			TestSerializable ts = (TestSerializable) obj;
  			return (s.equals(ts.s) && n == ts.n);
  		}
  		else {
  			return false;
  		}
  	}
  } // END of inner class TestSerializable
}

