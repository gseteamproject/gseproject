package gseproject.tests.interaction;

import jade.Boot;

public class MoveTest {
    public static void main(String[] args){
	String[] parameters = new String[2];
   	parameters[0] = "-gui";
   	parameters[1] = "GridAgent:gseproject.gridagent.GridAgent;";
   	parameters[1] += "Robot:gseproject.robot.RobotAgent;";
   	Boot.main(parameters);
       }
}
