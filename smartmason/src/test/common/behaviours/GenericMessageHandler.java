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
import test.common.TestUtility;

/**
   @author Giovanni Caire - TILAB
 */
public class GenericMessageHandler extends CyclicBehaviour {
	private List received = new LinkedList();
	
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			if (!isInReceived(msg)) {
				// Give other behaviours a chance to process this message
				myAgent.postMessage(msg);
				received.add(msg);
			}
			else {
				// No other behaviour has processed this message -->
				// Handle it
				TestUtility.log("Handling message\n"+msg);
				handleMessage(msg);
				received.remove(msg);
			}
		}
		block();
	}
	
	protected void handleMessage(ACLMessage msg) {
	}
	
	private boolean isInReceived(ACLMessage msg) {
		boolean found = false;
		Iterator it = received.iterator();
		while (it.hasNext()) {
			ACLMessage recv = (ACLMessage) it.next();
			if (msg.equals(recv)) {
				found = true;
				break;
			}
		}
		return found;
	}
}