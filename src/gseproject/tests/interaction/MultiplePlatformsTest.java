package gseproject.tests.interaction;

import jade.core.Profile;

import jade.core.ProfileImpl;

import jade.core.Runtime;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MultiplePlatformsTest {	
	private static void addStationToRuntime(Runtime rt){
		ProfileImpl pContainer = new ProfileImpl("192.168.111.1", 8888, "stations");

		AgentContainer cont = rt.createAgentContainer(pContainer);
		
		try {
			cont.createNewAgent("CleaningFloor", "gseproject.passive.CleaningFloorAgent", null);
			cont.createNewAgent("PaintingFloor", "gseproject.passive.PaintingFloorAgent", null);
			cont.createNewAgent("SourcePalette", "gseproject.passive.SourcepaletteAgent", null);
			cont.createNewAgent("GoalPalette", "gseproject.passive.GoalpaletteAgent", null);
		} catch (StaleProxyException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void addTransporterContainer(Runtime rt){
		ProfileImpl pContainer = new ProfileImpl("192.168.111.1", 8888, "transporter");
		AgentContainer cont = rt.createAgentContainer(pContainer);
		try {
			cont.createNewAgent("Transporter", "gseproject.robot.RobotAgent", null);
		} catch (StaleProxyException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void addCleanerContainer(Runtime rt){
		ProfileImpl pContainer = new ProfileImpl("192.168.111.1", 8888, "cleaner");
		AgentContainer cont = rt.createAgentContainer(pContainer);
		try {
			cont.createNewAgent("Cleaner", "gseproject.robot.RobotAgent", null);
		} catch (StaleProxyException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void addPainterContainer(Runtime rt){
		ProfileImpl pContainer = new ProfileImpl("192.168.111.1", 8888, "painter");
		AgentContainer cont = rt.createAgentContainer(pContainer);
		try {
			cont.createNewAgent("Painter", "gseproject.robot.RobotAgent", null);
		} catch (StaleProxyException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Runtime rt = Runtime.instance();

		// Exit the JVM when there are no more containers around
		rt.setCloseVM(true);

		// Create a default profile
		Profile profile = new ProfileImpl("192.168.111.1", 8888, "main");
		AgentContainer mainContainer = rt.createMainContainer(profile);

		addStationToRuntime(rt);
		addTransporterContainer(rt);
		addCleanerContainer(rt);
		addPainterContainer(rt);
	
		AgentController rma = null;
		try {
			rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
			rma.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

	}
}
