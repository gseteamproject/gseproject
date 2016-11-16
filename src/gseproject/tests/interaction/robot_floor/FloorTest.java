package gseproject.tests.interaction.robot_floor;

import jade.Boot;

public class FloorTest {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "CleaningFloor:gseproject.passive.floor.CleaningFloorAgent;";
		parameters[1] += "TestAgentClean:gseproject.tests.interaction.TestAgentClean;";
		Boot.main(parameters);
	}
}
