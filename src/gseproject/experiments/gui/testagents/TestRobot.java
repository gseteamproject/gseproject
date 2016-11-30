package gseproject.experiments.gui.testagents;

import gseproject.core.interaction.IActuator;
import jade.core.AID;
import jade.core.Agent;

public class TestRobot extends Agent {
	private static final long serialVersionUID = 1L;
	private AID gridAgent;

	protected void setup() {
		// TODO: set gridAgent
	}

	protected void takeDown() {
		System.out.println("TestAgent down!");
	}

}
