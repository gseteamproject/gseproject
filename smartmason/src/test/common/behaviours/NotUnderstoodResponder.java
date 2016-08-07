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

package test.common.behaviours;

import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.util.leap.*;
import jade.domain.FIPAAgentManagement.Envelope;

import java.util.Enumeration;

/**
   @author Giovanni Caire - TILAB
 */
public class NotUnderstoodResponder extends GenericMessageHandler {
	protected void handleMessage(ACLMessage msg) {
		System.out.println("Agent "+myAgent.getName()+": Received message. Replying...");
		ACLMessage reply = msg.createReply();
		reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
		// Set the same content
		reply.setContent(msg.getContent());
    
    // Set the same payload encoding parameters
    Envelope me,re;
    if ((me = msg.getEnvelope()) != null) {
      reply.setEnvelope(re = new Envelope());
      re.setAclRepresentation(me.getAclRepresentation());
      re.setPayloadEncoding(me.getPayloadEncoding());
    }
    
		// Set the same user defined parameters
		Properties pp = msg.getAllUserDefinedParameters();
		Enumeration e = pp.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = pp.getProperty(key);
			reply.addUserDefinedParameter(key, value);
		}
		myAgent.send(reply);
	}	
}
