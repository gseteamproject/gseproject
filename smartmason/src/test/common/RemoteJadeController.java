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

import jade.util.leap.*;
import test.common.remote.RemoteManager;
import java.rmi.RemoteException;

/**
   @author Giovanni Caire - TILAB
 */
class RemoteJadeController implements JadeController {
	private int myId;
	private RemoteManager myRm;
	
	private List addresses = null;
	private String containerName = null;
	
	public RemoteJadeController(RemoteManager rm, int id) throws TestException {
		myRm = rm;
		myId = id;
		try {
			addresses = myRm.getJadeInstanceAddresses(myId);
			containerName = myRm.getJadeInstanceContainerName(myId);
		}
		catch (RemoteException re) {
			throw new TestException("Error getting information from remote manager", re);
		}
	}
	
	public List getAddresses() {
		return addresses;
	}
	
	public String getContainerName() {
		return containerName;
	}
		
	public void kill() {
		try {
			myRm.killJadeInstance(myId);
		}
		catch (Exception e) {
			System.out.println("WARNING: Error killing JADE instance remotely. "+e.getMessage());
		}
	}	
}
