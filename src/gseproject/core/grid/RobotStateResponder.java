package gseproject.core.grid;

import gseproject.core.grid.communicator.IGridCommunicator;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class RobotStateResponder extends SimpleAchieveREResponder {
    private static final long serialVersionUID = -6024009980616790516L;
    private IGridCommunicator communicator;

    public RobotStateResponder(Agent a, MessageTemplate mt, IGridCommunicator communicator) {
	super(a, mt);
	this.communicator = communicator;
    }

    public RobotStateResponder(Agent a, MessageTemplate mt, DataStore store, IGridCommunicator communicator) {
	super(a, mt, store);
	this.communicator = communicator;
    }

    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
	communicator.notifyGUI(request);
	return communicator.agreeRobot(request);
    }

}
