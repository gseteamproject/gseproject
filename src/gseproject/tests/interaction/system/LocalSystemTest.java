package gseproject.tests.interaction.system;

import jade.Boot;

public class LocalSystemTest {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "TestRobot:gseproject.robot.RobotAgent;";
		parameters[1] += "SourcePalette:gseproject.passive.SourcepaletteAgent;";
		parameters[1] += "CleaningFloor:gseproject.passive.CleaningFloorAgent;";
		parameters[1] += "PaintingFloor:gseproject.passive.PaintingFloorAgent;";
		parameters[1] += "GoalPalette:gseproject.passive.GoalpaletteAgent;";
		Boot.main(parameters);
	}
}
