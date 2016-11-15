package gseproject.gridagent;

import gseproject.core.grid.Grid;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class RobotStateResponder extends SimpleAchieveREResponder {
    private static final long serialVersionUID = -6024009980616790516L;
    private Grid grid;
    private static SerializationController sc = SerializationController.Instance;

    public RobotStateResponder(Agent a, MessageTemplate mt, Grid grid) {
	super(a, mt);
	this.grid = grid;
    }

    public RobotStateResponder(Agent a, MessageTemplate mt, DataStore store, Grid grid) {
	super(a, mt, store);
	this.grid = grid;
    }

    protected ACLMessage prepareResponse(ACLMessage request) {
	return request;
    }

    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
	RobotStateContract state = sc.Deserialize(RobotStateContract.class, request.getContent());
	updateGrid(state);
	sendNewGridToGUI();
	System.out.println(request.getSender() + ":" + state.toString());
	ACLMessage agree = request.createReply();
	agree.setPerformative(ACLMessage.AGREE);
	return agree;
    }

    private void updateGrid(RobotStateContract state) {

    }

    private void sendNewGridToGUI() {

    }

    public Grid getGrid() {
	return this.grid;
    }
}
