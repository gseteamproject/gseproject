package test.common;

import jade.util.leap.List;
import jade.util.leap.ArrayList;

import javax.swing.JOptionPane;

public class ManualJadeController implements JadeController {
	private static final String JADE_LAUNCHER_TITLE = "JADE Launcher";
	
	private String instanceName;
	private String containerName;
	private List addresses = new ArrayList();
	
	public ManualJadeController(String instanceName, String cmdLine, String[] protoNames) throws TestException {
		this.instanceName = instanceName;
		String tmp = (String) JOptionPane.showInputDialog(null, "Start a JADE instance called "+instanceName+" using the following command line and press OK.", JADE_LAUNCHER_TITLE, JOptionPane.PLAIN_MESSAGE, null, null, cmdLine);
		if (tmp == null) {
			throw new TestException("JADE startup cancelled");
		}
		containerName = JOptionPane.showInputDialog(null, "Insert the newly born container name below and press OK", JADE_LAUNCHER_TITLE, JOptionPane.PLAIN_MESSAGE);
		if (protoNames != null) {
			for (int i = 0; i < protoNames.length; ++i) {
				String addr = JOptionPane.showInputDialog(null, "Insert the MTP address for protocol "+protoNames[i]+" below and press OK", JADE_LAUNCHER_TITLE, JOptionPane.PLAIN_MESSAGE);
				addresses.add(addr);
			}
 		}
	}
	
	public List getAddresses() {
		return addresses;
	}
	
	public String getContainerName() {
		return containerName;
		
	}
		
	public void kill() {
		JOptionPane.showMessageDialog(null, "Kill the JADE instance called "+instanceName+" and press OK", JADE_LAUNCHER_TITLE, JOptionPane.PLAIN_MESSAGE);
	}
}
