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
 * Test sending a message with the payload encoded using a given charset.
 * The charset is given by the test parameter "charset" (e.g. UTF-8), default is US-ASCII.
 * A second parameter "pass" indicates if the test is meant to succeed or to fail.
 *
 * @author Nicolas Lhuillier - Motorola
 */
public class TestCharset extends Test {
	public static final String KEY1 = "pass";
  public static final String KEY2 = "charset";
  
  private static final String RESPONDER_NAME = "responder";
	private final String CONV_ID = "conv_ID"+hashCode();
	private final String CONTENT = "יטחאûן";
	private boolean pass = false;
  private String charset;
  
	private AID resp = null;
	
  public Behaviour load(Agent a) throws TestException { 
  	try {
			// Initialize parameters
      String tmp;
      if ((tmp = getTestArgument(KEY1)) != null) {
        pass = Boolean.valueOf(tmp).booleanValue();
      }
      charset = getTestArgument(KEY2);

      AID remoteAMS = (AID) getGroupArgument(InterPlatformCommunicationTesterAgent.REMOTE_AMS_KEY);
			resp = TestUtility.createAgent(a, RESPONDER_NAME, TestUtility.CONFIGURABLE_AGENT, null, remoteAMS, null);
  		TestUtility.addBehaviour(a, resp, "test.common.behaviours.NotUnderstoodResponder");
			log("Responder correctly started on remote platform");
  		
  		Behaviour b1 = new SimpleBehaviour() {
          private boolean finished = false;
          public void onStart() {
             
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(resp);
            msg.setConversationId(CONV_ID);
            msg.setContent(CONTENT);
            if (charset != null) {
              msg.setDefaultEnvelope();
              msg.getEnvelope().setPayloadEncoding(charset);
            }
            else {
              charset = ACLCodec.DEFAULT_CHARSET;
            }
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
              else if ((pass)^(CONTENT.equals(msg.getContent()))) {
                failed("Unexpected reply content "+msg.getContent());
              }
              else {
                passed("Correct reply message received");
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
  	
  		// If we don't receive any answer in 10 sec --> TEST_FAILED
  		Behaviour b2 = new WakerBehaviour(a, 10000) {
          protected void handleElapsedTimeout() {
            failed("Reply timeout expired");
          }
        };
  		
  		ParallelBehaviour b = new ParallelBehaviour(a, ParallelBehaviour.WHEN_ANY); 
  		b.addSubBehaviour(b1);
  		b.addSubBehaviour(b2);
  		
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

