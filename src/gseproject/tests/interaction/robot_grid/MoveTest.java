package gseproject.tests.interaction.robot_grid;

import jade.Boot;

public class MoveTest {
    public static void main(String[] args){
	String[] parameters = new String[2];
   	parameters[0] = "-gui";
   	parameters[1] = "GridAgent:gseproject.core.grid.GridAgent;";
   	parameters[1] += "Robot:gseproject.robot.RobotAgent;";
   	Boot.main(parameters);
       }
}
