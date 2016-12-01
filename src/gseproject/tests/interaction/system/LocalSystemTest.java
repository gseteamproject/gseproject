package gseproject.tests.interaction.system;

import jade.Boot;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class LocalSystemTest {
	public static void main(String[] args) {


		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "SourcePalette:gseproject.passive.SourcepaletteAgent;";
		parameters[1] += "CleaningFloor:gseproject.passive.CleaningFloorAgent;";
		parameters[1] += "PaintingFloor:gseproject.passive.PaintingFloorAgent;";
		parameters[1] += "GoalPalette:gseproject.passive.GoalpaletteAgent;";


		Boot.main(parameters);

		/*
		 * Get the JADE runtime interface (singleton)
		 */
		jade.core.Runtime runtime = jade.core.Runtime.instance();

		/*
		 * Create a Profile, where the launch arguments are stored
		 */
		Profile profTrans = new ProfileImpl();
		profTrans.setParameter(Profile.CONTAINER_NAME, "TransporterContainer");
		profTrans.setParameter(Profile.MAIN_HOST, "localhost");

		/*
		 * Create a non-main agent container with Transporter
		 */
		ContainerController contTrans = runtime.createAgentContainer(profTrans);
		try {
			AgentController aTransp = contTrans.createNewAgent("Transporter",
					"gseproject.robot.RobotAgent",
					new Object[] {});
			aTransp.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}


		/*
		 * Create a non-main agent container with Painter
		 */
		Profile profPaint = new ProfileImpl();
		profPaint.setParameter(Profile.CONTAINER_NAME, "PaintContainer");
		profPaint.setParameter(Profile.MAIN_HOST, "localhost");
		ContainerController contPaint = runtime.createAgentContainer(profPaint);
		try {
			AgentController aPaint = contPaint.createNewAgent("Painter",
					"gseproject.robot.RobotAgent",
					new Object[] {});
			aPaint.start();

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}

		/*
		 * Create a non-main agent container with Cleaner
		 */
		Profile profClean = new ProfileImpl();
		profClean.setParameter(Profile.CONTAINER_NAME, "CleanerContainer");
		profClean.setParameter(Profile.MAIN_HOST, "localhost");
		ContainerController contCleaner = runtime.createAgentContainer(profClean);
		try {
			AgentController aClean = contCleaner.createNewAgent("Cleaner",
					"gseproject.robot.RobotAgent",
					new Object[] {});
			aClean.start();

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
}