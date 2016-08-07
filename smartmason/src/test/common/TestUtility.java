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

import jade.content.AgentAction;
import jade.content.ContentElement;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.Result;
import jade.core.AID;
import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.FIPAService;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.JADEManagementVocabulary;
import jade.domain.JADEAgentManagement.KillAgent;
import jade.domain.JADEAgentManagement.KillContainer;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.HashMap;
import jade.util.leap.Iterator;
import jade.util.leap.List;

import java.io.File;
import java.io.FilenameFilter;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;

import test.common.agentConfigurationOntology.AddBehaviour;
import test.common.agentConfigurationOntology.AgentConfigurationOntology;
import test.common.remote.RemoteManager;

/**
 Class including utility static methods for launching/killing
 agents, requesting generic AMS actions and creating remote JADE
 instances.
 @author Giovanni Caire - TILAB
 */
public class TestUtility {
	private static boolean verbose = true;
	
	public static String HTTP_MTP_CLASSPATH = "";
	public static String HTTP_MTP_ARG = "";
	
	public static final String CONFIGURABLE_AGENT = "test.common.ConfigurableAgent";
	
	private static final String SPY_NOTIFICATION = "spy-notification";
	private static int spyCnt = -1;
	
	private static jade.content.ContentManager cm = new jade.content.ContentManager();
	private static jade.content.lang.Codec codec = new jade.content.lang.leap.LEAPCodec();
	private static jade.content.onto.Ontology onto = AgentConfigurationOntology.getInstance();
	
	private static test.common.remote.RemoteManager defaultRm = null;
	
	static {
		/*String version = System.getProperty("java.version");
		if (version.startsWith("1.4")) {
			HTTP_MTP_CLASSPATH = "";
			HTTP_MTP_ARG = "";
		}
		else {
			HTTP_MTP_CLASSPATH = "../../tools/xercesImpl.jar";
			HTTP_MTP_ARG = "-jade_mtp_http_parser org.apache.xerces.parsers.SAXParser";
		}*/
		
		cm.registerLanguage(codec);
		cm.registerOntology(onto);
		cm.registerLanguage(new SLCodec(0), FIPANames.ContentLanguage.FIPA_SL0);
		cm.registerOntology(JADEManagementOntology.getInstance());
		cm.registerOntology(MobilityOntology.getInstance());
	}
	
	/**
	 Create a generic agent in the local container of the local platform
	 */
	public static AID createAgent(Agent a, String agentName, String agentClass, Object[] agentArgs) throws TestException {
		return createAgent(a, agentName, agentClass, agentArgs, null, null);
	}
	
	/**
	 Create a generic agent in the platform administrated by the indicated AMS
	 */
	public static AID createAgent(Agent a, String agentName, String agentClass, Object[] agentArgs, AID amsAID, String containerName) throws TestException {
		if (amsAID == null) {
			amsAID = a.getAMS();
		}
		
		CreateAgent ca = new CreateAgent();
		// Agent name
		ca.setAgentName(agentName);
		// Agent class
		ca.setClassName(agentClass);
		// Agent args
		if (agentArgs != null) {
			for (int i = 0; i < agentArgs.length; ++i) {
				ca.addArguments(agentArgs[i]);
			}
		}
		// Container where to create the agent
		if (containerName != null) {
			ca.setContainer(new ContainerID(containerName, null));
		}
		else {
			if (amsAID.equals(a.getAMS())) { 
				ca.setContainer((ContainerID) a.here());
			}
			else {
				ca.setContainer(new ContainerID(AgentContainer.MAIN_CONTAINER_NAME, null));
			}
		}
		
		try {
			requestAMSAction(a, amsAID, ca);
			return createNewAID(agentName, amsAID);
		}
		catch (TestException te) {
			throw new TestException("Error creating Agent "+agentName, te.getNested());
		}
	}
	
	/**
	 Kill an agent
	 */
	public static void killAgent(Agent a, AID agentAID) throws TestException {
		killAgent(a, agentAID, null);
	}
	
	/**
	 Kill an agent living in the platform administrated by the 
	 indicated AMS
	 */
	public static void killAgent(Agent a, AID agentAID, AID amsAID) throws TestException {
		if (amsAID == null) {
			amsAID = createNewAID("ams", agentAID);
		}
		
		ACLMessage request = createRequestMessage(a, amsAID, FIPANames.ContentLanguage.FIPA_SL0, JADEManagementVocabulary.NAME);
		
		KillAgent ka = new KillAgent();
		ka.setAgent(agentAID);
		
		try {
			requestAMSAction(a, amsAID, ka);
		}
		catch (TestException te) {
			throw new TestException("Error killing TargetAgent "+agentAID.getName(), te.getNested());
		}
	}
	/**
	 * Ask the AMS to kill a container.
	 * 
	 */
	public static void killContainer(Agent a, AID amsAID, String containerName) throws TestException{
		if(amsAID == null){
			amsAID = createNewAID("ams", a.getAID());
		}
		
		KillContainer kc = new KillContainer();
		ContainerID cid = new ContainerID();
		cid.setName(containerName);
		kc.setContainer(cid);
		
		try{
			requestAMSAction(a, amsAID, kc);
		}
		catch(TestException te){
			throw new TestException("Error killing container " + containerName, te.getNested());
		}
	}
	
	/**
	 * Ask the AMS to kill a container
	 */
	public static void killContainer(Agent a, String containerName) throws TestException{
		killContainer(a, null, containerName);
	}
	
	/**
	 Request an AMS agent to perform a given action in the JADE
	 management ontology.
	 */
	public static Object requestAMSAction(Agent a, AID amsAID, AgentAction action) throws TestException {
		return requestAMSAction(a, amsAID, action, JADEManagementVocabulary.NAME);
	}
	
	/**
	 Request an AMS agent to perform a given action of a given ontology.
	 */
	public static Object requestAMSAction(Agent a, AID amsAID, AgentAction action, String ontologyName) throws TestException {
		return requestAMSAction(a, amsAID, action, ontologyName, 30000);
	}
	 
	/**
	 Request an AMS agent to perform a given action of a given ontology specifying a given timeout.
	 */
	public static Object requestAMSAction(Agent a, AID amsAID, AgentAction action, String ontologyName, long timeout) throws TestException {
		try {
			if (amsAID == null) {
				amsAID = a.getAMS();
			}
			
			// Prepare the request
			ACLMessage request = createRequestMessage(a, amsAID, FIPANames.ContentLanguage.FIPA_SL0, JADEManagementVocabulary.NAME);
			
			Action act = new Action();
			act.setActor(amsAID);
			act.setAction(action);
			
			request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
			request.setOntology(ontologyName == null ? JADEManagementVocabulary.NAME : ontologyName);
			cm.fillContent(request,act);
			
			// Send message and collect reply
			ACLMessage inform = FIPAService.doFipaRequestClient(a, request, timeout);
			
			if (inform != null) {
				// Extract the result from the reply (if any)
				ContentElement ce = cm.extractContent(inform);
				if (ce instanceof Done) {
					// No result to return
					return null;
				}
				else if (ce instanceof Result) {
					return ((Result) ce).getValue();
				}
				else {
					throw new TestException("Unknown notification received from "+amsAID);
				}
			}
			else {
				throw new TestException("Timeout expired");
			}
		}
		catch (Exception e) {
			throw new TestException("Error executing action "+action, e);
		}
	}
	
	/////////////////////////////////////////////
	// Methods to configure a ConfigurableAgent 
	/////////////////////////////////////////////
	
	/**
	 Add a behaviour of the indicated class to the indicated ConfigurableAgent
	 */
	public static void addBehaviour(Agent a, AID targetAID, String behaviourClassName) throws TestException { 
		try {
			ACLMessage request = createRequestMessage(a, targetAID, codec.getName(), onto.getName());
			
			AddBehaviour ab = new AddBehaviour(null, behaviourClassName);
			synchronized (cm) {
				cm.fillContent(request, ab);
			}
			
			// Send message and collect reply
			FIPAService.doFipaRequestClient(a, request);
		}
		catch (Exception e) {
			throw new TestException("Error adding behaviour "+behaviourClassName+" to agent "+targetAID.getName(), e);
		}
	}
	
	/**
	 Make the indicated ConfigurableAgent perform the indicated action
	 */
	public static void forceAction(Agent a, AID targetAID, AgentAction action) throws TestException { 
		try {
			ACLMessage request = createRequestMessage(a, targetAID, codec.getName(), onto.getName());
			
			synchronized (cm) {
				cm.fillContent(request, action);
			}
			
			// Send message and collect reply
			FIPAService.doFipaRequestClient(a, request);
		}
		catch (Exception e) {
			throw new TestException("Error forcing action "+action+" to agent "+targetAID.getName(), e);
		}
	}
	
	/////////////////////////////////////////////
	// Methods to launch remote JADE instances 
	// and to configure the RemoteManager 
	/////////////////////////////////////////////
	
	/**
	 Launch a new instance of JADE in a separate process 
	 */
	public static JadeController launchJadeInstance(String instanceName, String classpath, String jadeArgs, String[] protoNames) throws TestException { 
		return launchJadeInstance(null, instanceName, classpath, jadeArgs, protoNames);
	}
	
	/**
	 Launch a new instance of JADE in a separate process using the
	 indicated RemoteManager
	 */
	public static JadeController launchJadeInstance(RemoteManager rm, String instanceName, String classpath, String jadeArgs, String[] protoNames) throws TestException { 
		return launch(rm, instanceName, classpath, null, "jade.Boot", jadeArgs, protoNames, null);
	}
	
	/**
	 Launch a new instance of a JADE split container in a separate process 
	 */
	public static JadeController launchSplitJadeInstance(String instanceName, String classpath, String jadeArgs) throws TestException {
		return launchSplitJadeInstance(null, instanceName, classpath, jadeArgs);
	}
	
	/**
	 Launch a new instance of a JADE split container in a separate process 
	 using the indicated RemoteManager
	 */
	public static JadeController launchSplitJadeInstance(RemoteManager rm, String instanceName, String classpath, String jadeArgs) throws TestException {
		return launch(rm, instanceName, classpath, null, "jade.MicroBoot", jadeArgs, null, null);
	}
	
	/**
	 Launch a new instance of JADE in a separate process.
	 If the jvmArgs string includes the -cp or -classpath option, the <code>classpath</code> parameter is ignored.  
	 */
	public static JadeController launch(RemoteManager rm, String instanceName, String classpath, String jvmArgs, String mainClass, String jadeArgs, String[] protoNames, OutputHandler outHandler) throws TestException { 
		JadeController jc = null;
		if (rm == null) {
			rm = defaultRm;
		}
		
		if (rm != null) {
			// If a RemoteManager is set, use it and launch the JADE instance remotely
			try {
				int id = rm.launchJadeInstance(instanceName, classpath, jvmArgs, mainClass, jadeArgs, protoNames);
				jc = new RemoteJadeController(rm, id);
			}
			catch (RemoteException re) {
				throw new TestException("Communication error launching JADE instance remotely", re);
			}
		}
		else {
			// Otherwise launch the JADE instance locally
			jc = localLaunch(instanceName, classpath, jvmArgs, mainClass, jadeArgs, protoNames, outHandler, null);
		}
		return jc;
	}

	private static String adjustFileSeparator(String path) {
		path = path.replaceAll("\\\\", Matcher.quoteReplacement(File.separator));
		return path.replaceAll("/", Matcher.quoteReplacement(File.separator));
	}

	private static String adjustWindowsPath(String path) {
		if (path.startsWith("\"") || System.getProperty("os.name").toLowerCase().indexOf("windows")==-1) {
			return path;
		} 
		
		// Windows
		return "\""+path+"\"";
	}
	
	private static List getJarsFromDir(String dirPath) {
		File[] jars = new File(dirPath).listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});

		List files = new ArrayList();
		if (jars != null) {
			for (File jar : jars) {
				files.add(jar.getName());
			}
		}
		return files;
	}

	private static String getOptimizeClasspath(String classpath) {
		HashMap jarsByDir = new HashMap();
		StringBuilder sb = new StringBuilder();
		String[] paths = classpath.split(File.pathSeparator);
		for (String path : paths) {
			if (path.toLowerCase().endsWith(".jar")) {
				// This is a jar
				path = adjustFileSeparator(path);
				int pos = path.lastIndexOf(File.separator);
				if (pos > 0) {
					// Jar in a specific dir -> add it to map
					String dir = path.substring(0, pos);
					String jar = path.substring(pos+1);
					
					List jars = (List) jarsByDir.get(dir);
					if (jars == null) {
						jars = new ArrayList();
						jarsByDir.put(dir, jars);
					}
					jars.add(jar);
				}
				else {
					// Jar without path -> add it
					sb.append(adjustWindowsPath(path)+File.pathSeparator);
				}
			}
			else {
				// This is a dir -> add it
				sb.append(adjustWindowsPath(path)+File.pathSeparator);
			}
		}

		// Check if dir contains all classpath jars
		Iterator dirIt = jarsByDir.keySet().iterator();
		while (dirIt.hasNext()) {
			String dir = (String) dirIt.next();
			List jars = (List) jarsByDir.get(dir);
			List dirJars = getJarsFromDir(dir);

			Iterator jarIt = jars.iterator();
			while (jarIt.hasNext()) {
				String jar = (String) jarIt.next();
				dirJars.remove(jar);
			}
			
			if (dirJars.isEmpty()) {
				// All jar contained -> add dir with add it with /*
				sb.append(adjustWindowsPath(dir+File.separator+"*")+File.pathSeparator);
			}
			else {
				// Add specific jars
				jarIt = jars.iterator();
				while (jarIt.hasNext()) {
					String jar = (String) jarIt.next();
					// Add dir with specific jar
					sb.append(adjustWindowsPath(dir+File.separator+jar)+File.pathSeparator);
				}				
			}
		}
		
		return sb.toString();
	}
	
	public static JadeController localLaunch(String instanceName, String classpath, String jvmArgs, String mainClass, String jadeArgs, String[] protoNames, OutputHandler outputHandler, String workingDir) throws TestException {
		JadeController jc;
		
		// JVM arguments
		if (jvmArgs == null) {
			jvmArgs = "";
		}

		// Prepare the startup-tag used to check the JADE containers startup
		String startupTag = UUID.randomUUID().toString();
		jvmArgs += " -Dstartup-tag="+startupTag;
		
		// FIXME: We should propagate all -Dkey=value properties...
		if (System.getProperty("project-home") != null) {
			jvmArgs = jvmArgs + " -Dproject-home="+System.getProperty("project-home");
		}
		
		// Classpath: 
		// - If a classpath is already specified in the JVM args (-cp ... or -classpath ....), just use it
		// Otherwise
		// - If the classpath argument specifies a normal classpath, use it.
		// - If the classpath argument begins with +, use <classpath>;<current-classpath>
		// - If the classpath argument is null, use the current classpath only
		String classpathOption = "";
		if (jvmArgs.indexOf("-cp ") < 0 && jvmArgs.indexOf("-classpath ") < 0) {
			if (classpath == null || classpath.startsWith("+")) {
				String currentCp = System.getProperty("java.class.path");
				if (classpath == null) {
					classpath = currentCp;
				}
				else {
					classpath = classpath.substring(1)+System.getProperty("path.separator")+currentCp;
				}
			}
			
			classpathOption = "-cp "+getOptimizeClasspath(classpath);
		}
		
		// Java executable.
		// If the JAVA_HOME variable is specified, use that in <JAVA_HOME>/bin
		// Else use that in the PATH
		String javaExe = "java ";
		String javaHome = System.getenv("JAVA_HOME");
		if(javaHome != null){
			javaExe = javaHome + System.getProperty("file.separator")+ "bin" + System.getProperty("file.separator")+javaExe;
		}

		String commandLine = javaExe+classpathOption+" "+jvmArgs+" -DTSDaemon=true "+mainClass+" "+jadeArgs;
		
		if ("true".equalsIgnoreCase(System.getProperty("DEBUG"))) {
			System.out.println("Manual launch!!!!!!!!!!!!!!");
			jc = new ManualJadeController(instanceName, commandLine, protoNames);
		}
		else {
			try {
				jc = new LocalJadeController(instanceName, commandLine, protoNames, outputHandler, workingDir, startupTag);
			} catch(ExecException e) {
				System.out.println("Error launching JADE. Check the JAVA_HOME environment variable (JAVA_HOME="+javaHome+")");
				throw e;
			}
		}
		
		return jc;
	}


	public static RemoteManager createRemoteManager(String hostName, int port, String managerName) throws TestException {
		String remoteManagerRMI = "rmi://"+hostName+":"+port+"//"+managerName;
		try {
			return (RemoteManager) Naming.lookup(remoteManagerRMI);
		}
		catch (Exception e) {
			throw new TestException("Error looking up remote manager "+remoteManagerRMI, e);
		}
	}
	
	public static void setDefaultRemoteManager(RemoteManager rm) {
		defaultRm = rm;
	}
	
	public static RemoteManager getDefaultRemoteManager() {
		return defaultRm;
	}
	
	/**
	 @return the name of the local host
	 */
	public static String getLocalHostName() throws TestException {
		return jade.core.Profile.getDefaultNetworkName();
	}
	
	/**
	 @return the name of the host a given container in the local
	 platform is running on
	 */
	public static String getContainerHostName(Agent a, String containerName) throws TestException {
		return getContainerHostName(a, null, containerName);
	}
	
	/**
	 @return the name of the host a given container in the platform
	 managed by the specified AMS is running on
	 */
	public static String getContainerHostName(Agent a, AID ams, String containerName) throws TestException {
		try {
			if (containerName == null) {
				containerName = AgentContainer.MAIN_CONTAINER_NAME;
			}
			
			String[] args = null;
			if (ams == null || a.getAMS().equals(ams)) {
				// The container is in the local platform --> arg0 is the localName
				args = new String[] {a.getLocalName()};
			}
			else {
				// The container is in a remote platform --> arg0 is the GUID and arg1 and following 
				// args are the addresses
				List l = new ArrayList();
				l.add(a.getName());
				Iterator it = a.getAID().getAllAddresses();
				while (it.hasNext()) {
					l.add(it.next());
				}
				args = new String[l.size()];
				for (int i = 0; i < l.size(); ++i) {
					args[i] = (String) l.get(i);
				}
			}
			
			spyCnt++;
			AID spy = TestUtility.createAgent(a, "spy"+spyCnt, "test.common.TestUtility$HostSpyAgent", args, ams, containerName);
			ACLMessage msg = a.blockingReceive(MessageTemplate.MatchConversationId(SPY_NOTIFICATION), 10000);
			if (msg != null) {
				if (msg.getPerformative() == ACLMessage.INFORM) {
					return msg.getContent();
				}
				else {
					throw new TestException("Can't get hostname for container "+containerName+": FAILURE received");
				}
			}
			else {
				throw new TestException("Can't get hostname for container "+containerName+": timeout expired");
			}
		}
		catch (Exception e) {
			throw new TestException("Can't get hostname for container "+containerName, e);
		}
	}
	
	/**
	 */
	private static ACLMessage createRequestMessage(Agent sender, AID receiver, String language, String ontology) {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setSender(sender.getAID());
		request.addReceiver(receiver);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.setLanguage(language);
		request.setOntology(ontology);
		request.setReplyWith("rw"+sender.getName()+(new Date()).getTime());
		request.setConversationId("conv"+sender.getName()+(new Date()).getTime());
		return request;
	}
	
	
	public static AID createNewAID(String newName, AID anAID) {
		String name = anAID.getName();
		String hap = null;
		int atPos = name.lastIndexOf('@');
		if(atPos == -1)
			hap = name;
		else
			hap = name.substring(atPos + 1);
		
		AID id = new AID(newName+"@"+hap, AID.ISGUID);
		Iterator it = anAID.getAllAddresses();
		while (it.hasNext()) {
			id.addAddresses((String) it.next());
		}
		return id;
	}
	
	/**
	 */
	public static void log(String s) {
		if (verbose) {
			System.out.println(s);
		} 
	} 	
	
	/**
	 */
	public static void log(Object o) {
		log(o.toString());
	}
	
	/**
	 */
	public static void log(Throwable t) {
		if (verbose) {
			t.printStackTrace();
		} 
	} 	
	
	/**
	 */
	public static void setVerbose(boolean b) {
		verbose = b;
	}
	
	/**
	 Inner class HostSpyAgent.
	 This agent is used to get the hostname a container is running on.
	 */
	public static class HostSpyAgent extends Agent {
		protected void setup() {
			Object[] args = getArguments();
			if (args.length > 0) {
				AID receiver = null;
				if (args.length == 1) {
					// Receiver is in the same platform --> arg0 is the localName
					receiver = new AID((String) args[0], AID.ISLOCALNAME);
				}
				else {
					// Receiver is remote --> arg0 is the GUID and arg1 following 
					// args are the addresses
					receiver = new AID((String) args[0], AID.ISGUID);
					for (int i = 1; i < args.length; ++i) {
						receiver.addAddresses((String) args[i]);
					}
				}
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(receiver);
				msg.setConversationId(SPY_NOTIFICATION);
				try {
					msg.setContent(TestUtility.getLocalHostName());
				}
				catch (Exception e) {
					e.printStackTrace();
					msg.setPerformative(ACLMessage.FAILURE);
				}
				send(msg);
			}
			doDelete();
		}
	} // END of inner class HostSpyAgent
}


