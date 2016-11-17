package gseproject.core.grid.communicator;

import gseproject.infrastructure.contracts.RobotStateContract;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class RobotStateResponder extends SimpleAchieveREResponder {
    private static final long serialVersionUID = -6024009980616790516L;
    private IGridCommunicator communicator;
    private RobotStateContract currStateContract;

    public RobotStateResponder(Agent a, MessageTemplate mt, IGridCommunicator communicator) {
	super(a, mt);
	this.communicator = communicator;
    }

    public RobotStateResponder(Agent a, MessageTemplate mt, DataStore store, IGridCommunicator communicator) {
	super(a, mt, store);
	this.communicator = communicator;
    }

    public RobotStateResponder(Agent a, MessageTemplate mt, DataStore store) {
	super(a, mt, store);
    }

    protected ACLMessage prepareResponse(ACLMessage request) {
	this.currStateContract = communicator.parseRobotStateFromMessage(request);
	if(currStateContract == null){
	    System.out.println("wtf");
	}
	System.out.println(this.currStateContract);
	return communicator.agreeRobot(request);
    }

    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
	return communicator.agreeRobot(request);
    }

}
