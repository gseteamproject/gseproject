package gseproject.passive.communicator;

import java.io.IOException;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.passive.core.SourcePalette;
import jade.lang.acl.ACLMessage;

public class SourcePalleteCommunicator extends StationCommunicator {
	private SourcePalette sourcePalette;

	public SourcePalleteCommunicator(SourcePalette sourcePallete) {
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
		if (!serviceType.equals(ServiceType.TAKE_BLOCK)) {
			return failureMessage(serviceTypeRequest);
		}
		Block block = sourcePalette.takeBlock();
		return addBlockToMessage(informMessage(serviceTypeRequest), block);
	}

	@Override
	public ACLMessage notifyGrid() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(super.GridAgent);
		//TODO: message.setContentObject(this.sourcePalette);
		return message;
	}

}
