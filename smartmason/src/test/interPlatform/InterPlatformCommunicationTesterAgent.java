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

package test.interPlatform;

import jade.core.Agent;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.AID;
import jade.wrapper.*;
import jade.util.leap.*;
import test.common.*;

/**
   @author Givanni Caire - TILAB
   @author Elisabetta Cortese - TILAB
 */
public class InterPlatformCommunicationTesterAgent extends TesterAgent {

	// Names and default values for group arguments
	public static final String REMOTE_AMS_KEY = "remote-ams";
	
	public static final String REMOTE_PLATFORM_NAME = "Remote-platform";
	public static final String REMOTE_PLATFORM_PORT = "9003";
	
	public static final String MTP_CLASS_KEY = "mtp";
	public static final String PROTO_KEY = "proto";
	public static final String MTP_URL_KEY = "url";
	public static final String MTP_URL_DEFAULT = "";
	public static final String ADDITIONAL_CLASSPATH_KEY = "classpath";
	public static final String ADDITIONAL_CLASSPATH_DEFAULT = "";
	
	protected TestGroup getTestGroup() {
		TestGroup tg = new TestGroup("test/interPlatform/interPlatformTestsList.xml"){		
			
			private JadeController jc1, jc2;
			
			public void initialize(Agent a) throws TestException {
				try {
					String mtp = (String) getArgument(MTP_CLASS_KEY);
					String proto = (String) getArgument(PROTO_KEY);
					String additionalArguments = ("http".equalsIgnoreCase(proto) ? TestUtility.HTTP_MTP_ARG : "");
					String addClasspath = "+"+TestUtility.HTTP_MTP_CLASSPATH+System.getProperty("path.separator")+((String) getArgument(ADDITIONAL_CLASSPATH_KEY));
					
					// Start the remote platform with the specified MTP
					jc1 = TestUtility.launchJadeInstance(REMOTE_PLATFORM_NAME, addClasspath, "-name "+REMOTE_PLATFORM_NAME+" -port "+REMOTE_PLATFORM_PORT+" -mtp "+mtp+" "+additionalArguments, new String[]{proto}); 
		
					// Construct the AID of the AMS of the remote platform and make it
					// accessible to the tests as a group argument
					AID remoteAMS = new AID("ams@"+REMOTE_PLATFORM_NAME, AID.ISGUID);
					Iterator it = jc1.getAddresses().iterator();
					while (it.hasNext()) {
						remoteAMS.addAddresses((String) it.next());
					}
					setArgument(REMOTE_AMS_KEY, remoteAMS);
					
					// Start a local container with the specified MTP
					String url = (String) getArgument(MTP_URL_KEY);
					jc2 = TestUtility.launchJadeInstance("Container-mtp", addClasspath,
							"-container -host "+TestUtility.getContainerHostName(a, null)+" -port "+Test.DEFAULT_PORT+
							" -mtp "+mtp+"("+url+") "+additionalArguments, null);
				}
				catch (TestException te) {
					throw te;
				}
				catch (Exception e) {
					throw new TestException("Error initializing TestGroup", e);
				}
			}
			
			public void shutdown(Agent a) {
  			try {
  				// Kill the remote platform and the mtp container
  				Thread.sleep(1000);
  				jc1.kill();
  				jc2.kill();
  			}
  			catch (Exception e) {
  				e.printStackTrace();
  			}
			}
		};
		
		tg.specifyArgument(MTP_CLASS_KEY, "MTP Class", Test.DEFAULT_MTP);
		tg.specifyArgument(PROTO_KEY, "Protocol", Test.DEFAULT_PROTO);
		tg.specifyArgument(MTP_URL_KEY, "Local MTP URL", MTP_URL_DEFAULT);
		tg.specifyArgument(ADDITIONAL_CLASSPATH_KEY, "Additional classpath", ADDITIONAL_CLASSPATH_DEFAULT);
		return tg;
	}
		
	// Main method that allows launching this test as a stand-alone program	
	public static void main(String[] args) {
		try {
			// Get a hold on JADE runtime
      Runtime rt = Runtime.instance();

      // Exit the JVM when there are no more containers around
      rt.setCloseVM(true);
      
      Profile pMain = new ProfileImpl(null, Test.DEFAULT_PORT, null);

      AgentContainer mc = rt.createMainContainer(pMain);

      AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
      rma.start();

      AgentController tester = mc.createNewAgent("tester", "test.interPlatform.InterPlatformCommunicationTesterAgent", args);
      tester.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
