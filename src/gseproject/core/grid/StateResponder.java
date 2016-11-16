package gseproject.core.grid;

import gseproject.core.grid.communicator.IGridCommunicator;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;

public class StateResponder extends SimpleAchieveREResponder {
	private static final long serialVersionUID = -6024009980616790516L;
	private IGridCommunicator communicator;

	public StateResponder(Agent a, MessageTemplate mt, IGridCommunicator communicator) {
		super(a, mt);
		this.communicator = communicator;
	}

	public StateResponder(Agent a, MessageTemplate mt, DataStore store, IGridCommunicator communicator) {
		super(a, mt, store);
		this.communicator = communicator;
	}

	public StateResponder(Agent a, MessageTemplate mt, DataStore store) {
		super(a, mt, store);
	}

	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
		if(!request.getSender().equals(communicator.GUI_AID())){
			//reply to Robot
			return communicator.agreeRobot(request);
		} else {
			System.out.println("message from GUI");
			//reply to GUI
			return communicator.sendInitialGrid(request);
		}
	}

}
