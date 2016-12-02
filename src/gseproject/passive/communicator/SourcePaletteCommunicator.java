package gseproject.passive.communicator;

import java.io.IOException;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.passive.core.SourcePalette;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class SourcePaletteCommunicator extends StationCommunicator {
	private SourcePalette sourcePalette;

	public SourcePaletteCommunicator(SourcePalette sourcePallete) {
		this.sourcePalette = sourcePallete;
	}

	private static ACLMessage addBlockToMessage(ACLMessage message, Block block) {
		if (block == null) {
			message.setPerformative(ACLMessage.FAILURE);
			return message;
		}
		try {
			message.setContentObject(block);
		} catch (IOException e) {
			e.printStackTrace();
			message.setPerformative(ACLMessage.FAILURE);
			return message;
		}
		return message;
	}

	@Override
	public ACLMessage handleServiceTypeRequest(ACLMessage serviceTypeRequest) {
		if (serviceTypeRequest == null || serviceTypeRequest.getContent() == null) {
			return failureMessage(serviceTypeRequest);
		}
		String serviceType = serviceTypeRequest.getContent();
		if (!serviceType.equals(ServiceType.TAKE_BLOCK.name())) {
			return failureMessage(serviceTypeRequest);
		}
		Block block = sourcePalette.takeBlock();
		return addBlockToMessage(informMessage(serviceTypeRequest), block);
	}

	@Override
	public ACLMessage updateTrack() {
		ACLMessage messageToTrackAgent = new ACLMessage(ACLMessage.INFORM);
		messageToTrackAgent.setProtocol(ProtocolTemplates.TrackProtocolTemplate.TRACK_PALETTE_PROTOCOL);
		try {
			messageToTrackAgent.setContentObject(this.sourcePalette);
		} catch (IOException e) {
			e.printStackTrace();
		}
		messageToTrackAgent.addReceiver(new AID("TrackAgent", AID.ISLOCALNAME));
		return messageToTrackAgent;
	}

}
