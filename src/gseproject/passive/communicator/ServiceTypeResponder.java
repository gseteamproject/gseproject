package gseproject.passive.communicator;

import gseproject.passive.communicator.IStationCommunicator;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class ServiceTypeResponder extends AchieveREResponder {
	private static final long serialVersionUID = 4906304601007873881L;
	private IStationCommunicator stationCommunicator;

	public ServiceTypeResponder(Agent a, MessageTemplate mt, IStationCommunicator stationCommunicator) {
		super(a, mt);
		this.stationCommunicator = stationCommunicator;
	}

	public ServiceTypeResponder(Agent a, MessageTemplate mt, DataStore store,
			IStationCommunicator stationCommunicator) {
		super(a, mt, store);
		this.stationCommunicator = stationCommunicator;
	}

	protected ACLMessage perpareResponse(ACLMessage request) {
		ACLMessage response = stationCommunicator.handleServiceTypeRequest(request);
		ACLMessage gridNotification = stationCommunicator.notifyGrid();
		//TODO: send gridobject to grid
		return response;

	}

	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
		return response;
	}

}
