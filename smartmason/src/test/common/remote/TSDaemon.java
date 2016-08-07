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

package test.common.remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.*;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.Hashtable;
import java.util.Iterator;

import test.common.*;
import jade.core.Profile;
import jade.util.leap.List;

/**
 @author Giovanni Caire - TILAB
 */
public class TSDaemon extends UnicastRemoteObject implements RemoteManager, OutputHandler {

	public static final String DEFAULT_NAME = "TSDaemon";
	public static final int DEFAULT_PORT = 7777;

	private Hashtable controllers = new Hashtable();
	private int instanceCnt = 0;

	private String additionalArgs = null;

	public TSDaemon() throws RemoteException {
		super(Integer.parseInt(System.getProperty("tsdaemon.remoteobjectport", "0")));
	}
	
	public TSDaemon(int port) throws RemoteException {
		super(port);
	}
	
	/**
	@param port the port where the TSDaemon started
	@param name the name used for name binding
	 **/
	protected void printWelcomeMessage(String port, String name) {
		System.out.println("Test Suite Daemon ready on port " + port + " using name: " + name);
	}

	public static void main(String args[]) {
		try {
			TSDaemon daemon = new TSDaemon();
			daemon.start(args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start(String[] args) {
		StringBuffer sb = new StringBuffer();
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; ++i) {
				sb.append(args[i]);
				sb.append(" ");
			}
		}

		try {
			// Redefine RMI socket factory to enable the keep-alive
			RMISocketFactory.setSocketFactory(new KeepAliveSocketFactory());
			
			// Create an RMI registry on the local host and DAEMON port
			// (if one is already running just get it)
			String name = System.getProperty("tsdaemon.name", DEFAULT_NAME);
			String port = System.getProperty("tsdaemon.port", String.valueOf(DEFAULT_PORT));
			String hostName = Profile.getDefaultNetworkName();
			Registry theRegistry = getRmiRegistry(hostName, Integer.parseInt(port));
			String registryID = "rmi://"+hostName+":"+port;

			// Bind to the registry
			additionalArgs = sb.toString();
			Naming.bind(registryID+"//"+name, this);
			printWelcomeMessage(port, name);
		}
		catch(Exception e) {
			System.out.println("ERROR starting Test Suite Daemon");
			e.printStackTrace();
			System.exit(1);
		}
	}

	class KeepAliveSocketFactory extends RMISocketFactory {
		public Socket createSocket(String host, int port) throws IOException {
			Socket s = getDefaultSocketFactory().createSocket(host, port);
			s.setKeepAlive(true);
			return s;
		}
		public ServerSocket createServerSocket(int port) throws IOException {
			return getDefaultSocketFactory().createServerSocket(port);
		}
	}

	/**
	 * If an RMI registry is already active on this host
	 * at the given portNumber, then that registry is returned, 
	 * otherwise a new registry is created and returned.
	 */
	private static Registry getRmiRegistry(String host, int portNumber) throws RemoteException {
		Registry rmiRegistry = null;
		// See if a registry already exists and
		// make sure we can really talk to it.
		try {
			rmiRegistry = LocateRegistry.getRegistry(host, portNumber);
			rmiRegistry.list();
		} 
		catch (Exception exc) {
			rmiRegistry = null;
		}

		// If rmiRegistry is null, then we failed to find an already running
		// instance of the registry, so let's create one.
		if (rmiRegistry == null) {
			rmiRegistry = LocateRegistry.createRegistry(portNumber);
		}

		return rmiRegistry;
	}

	protected int localLaunchJadeInstance(String instanceName, String classpath, String jvmArgs, String mainClass, String jadeArgs, String[] protoNames, OutputHandler outputHandler, String workingDir) throws TestException {
		instanceCnt++;
		jadeArgs = additionalArgs + jadeArgs;
		JadeController jc = null;
		jc = TestUtility.localLaunch(instanceName, classpath, jvmArgs, mainClass, jadeArgs, protoNames, outputHandler, workingDir);
		controllers.put(new Integer(instanceCnt), jc);
		return instanceCnt;
	}
	
	//////////////////////////////////////////
	// RemoteManager INTERFACE IMPLEMENTATION
	//////////////////////////////////////////
	public int launchJadeInstance(String instanceName, String classpath, String jvmArgs, String mainClass, String jadeArgs, String[] protoNames) throws TestException, RemoteException {
		instanceCnt++;
		jadeArgs = additionalArgs + jadeArgs;
		JadeController jc = null;
		jc = TestUtility.launch(null, instanceName, classpath, jvmArgs, mainClass, jadeArgs, protoNames, this);
		controllers.put(new Integer(instanceCnt), jc);
		return instanceCnt;
	}

	public List getJadeInstanceAddresses(int id) throws TestException, RemoteException {
		JadeController jc = (JadeController) controllers.get(new Integer(id));
		if (jc != null) {
			return jc.getAddresses();
		}
		else {
			throw new TestException("No JADE instance corresponding to ID "+id);
		}
	}

	public String getJadeInstanceContainerName(int id) throws TestException, RemoteException {
		JadeController jc = (JadeController) controllers.get(new Integer(id));
		if (jc != null) {
			return jc.getContainerName();
		}
		else {
			throw new TestException("No JADE instance corresponding to ID "+id);
		}
	}

	public void killJadeInstance(int id) throws TestException, RemoteException {
		JadeController jc = (JadeController) controllers.remove(new Integer(id));
		if (jc != null) {
			jc.kill();
		}
		else {
			throw new TestException("No JADE instance corresponding to ID "+id);
		}
	}

	public int getJadeInstanceId(String containerName) throws TestException, RemoteException {
		synchronized (controllers) {
			Iterator it = controllers.keySet().iterator();
			while (it.hasNext()) {
				Integer id = (Integer) it.next();
				JadeController jc = (JadeController) controllers.get(id);
				if (jc.getContainerName().equalsIgnoreCase(containerName)) {
					return id.intValue();
				}
			}
			return -1;
		}
	}

	/////////////////////////////////////////////////
	// OutputHandler interface implementation
	/////////////////////////////////////////////////
	public void handleOutput(String source, String msg) {
		System.out.println("###"+source+">> "+msg);
	}

	public void handleTermination(int exitValue) {
		System.out.println("Process terminated with exitValue="+exitValue);
	}
}

