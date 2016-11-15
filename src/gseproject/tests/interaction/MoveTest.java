package gseproject.tests.interaction;

import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import jade.Boot;

public class MoveTest {
    public static void main(String[] args){
   	SerializationController sc = SerializationController.Instance;
   	sc.RegisterSerializator(RobotStateContract.class, new RobotStateWriter(), new RobotStateReader());
	String[] parameters = new String[2];
   	parameters[0] = "-gui";
   	parameters[1] = "GridAgent:gseproject.gridagent.GridAgent;";
   	parameters[1] += "TestRobotMoveAgent:gseproject.tests.interaction.TestRobotMoveAgent;";
   	Boot.main(parameters);
       }
}
