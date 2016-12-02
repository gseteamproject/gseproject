package gseproject.passive.communicator;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ServiceTypeResponder extends CyclicBehaviour {
	private static final long serialVersionUID = 4906304601007873881L;
	private IStationCommunicator stationCommunicator;
	private MessageTemplate messageTemplate;

	public ServiceTypeResponder(MessageTemplate template, IStationCommunicator stationCommunicator) {
		this.stationCommunicator = stationCommunicator;
		this.messageTemplate = template;
	}

	@Override
	public void action() {
		ACLMessage request = myAgent.blockingReceive(this.messageTemplate);
		ACLMessage reply = stationCommunicator.handleServiceTypeRequest(request);
		myAgent.send(reply);
		ACLMessage updateTackMessage = stationCommunicator.updateTrack();
		myAgent.send(updateTackMessage);
	}

}
