package gseproject.robot.communicator;

import gseproject.robot.RobotAgent;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

public class RobotStateInitiator extends SimpleAchieveREInitiator {

    // <editor-fold desc="Private constructors">
    private RobotStateInitiator(Agent a, ACLMessage msg) {
	super(a, msg);
    }

    private RobotStateInitiator(Agent a, ACLMessage msg, DataStore store) {
	super(a, msg, store);
    }
    // </editor-fold>

    private ACLMessage _templateMessage;

    public RobotStateInitiator(RobotAgent a, ACLMessage msg) {
	super(a, msg);
	_templateMessage = msg;
    }

}
