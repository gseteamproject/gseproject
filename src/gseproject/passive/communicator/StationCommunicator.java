package gseproject.passive.communicator;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public abstract class StationCommunicator implements IStationCommunicator {
	protected static final AID GridAgent = new AID("GridAgent", true);

	protected static ACLMessage failureMessage(ACLMessage request) {
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.FAILURE);
		return reply;
	}

	protected static ACLMessage informMessage(ACLMessage request) {
		ACLMessage reply = request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}

}
