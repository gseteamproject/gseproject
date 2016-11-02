package gseproject.robot;

import gseproject.robot.interaction.IRobotActuator;
import gseproject.robot.processing.IRobotProcessor;
import gseproject.robot.processing.RobotDummyProcessor;
import jade.core.Agent;

public class RobotAgent extends Agent {

    IRobotActuator actuator;

    private IRobotProcessor _processor;

    public RobotAgent(){

        _processor = new RobotDummyProcessor();

    }

    public void setup(){

    }
}
