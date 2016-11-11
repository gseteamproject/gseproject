package gseproject.experiments.gui.testagents;

import java.awt.Point;
import java.io.IOException;

import gseproject.core.interaction.IActuator;
import gseproject.robot.interaction.VirtualActuator;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class TestRobot extends Agent {
    private static final long serialVersionUID = 1L;
    private IActuator actuator;
    private AID gridAgent;

    protected void setup() {
	this.actuator = new VirtualActuator();
	//TODO: set gridAgent
    }

    protected void takeDown() {
	System.out.println("TestAgent down!");
    }

}
