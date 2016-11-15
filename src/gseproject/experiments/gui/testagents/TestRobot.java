package gseproject.experiments.gui.testagents;

import gseproject.core.interaction.IActuator;
import gseproject.robot.interaction.VirtualActuator;
import jade.core.AID;
import jade.core.Agent;

public class TestRobot extends Agent {
    private static final long serialVersionUID = 1L;
    private IActuator actuator;
    private AID gridAgent;

    protected void setup() {
	this.actuator = new VirtualActuator();
	// TODO: set gridAgent
    }

    protected void takeDown() {
	System.out.println("TestAgent down!");
    }

}
