package gseproject.passive.communicator;

import java.io.IOException;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.passive.core.Floor;
import gseproject.passive.core.StationException;
import jade.lang.acl.ACLMessage;

public class FloorCommunicator extends StationCommunicator {
	private Floor floor;
	
	public FloorCommunicator(Floor floor) {
		this.floor = floor;
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

	private ACLMessage replyBlock(ACLMessage message) {
		Block block = null;
		try {
			block = floor.takeBlock();
		} catch (StationException e) {
			e.printStackTrace();
			return failureMessage(message);
		}
		return addBlockToMessage(agreeMessage(message), block);
	}

	@Override
	public ACLMessage handleServiceTypeRequest(ACLMessage serviceTypeRequest) {
		if (serviceTypeRequest == null || serviceTypeRequest.getContent() == null) {
			return failureMessage(serviceTypeRequest);
		}
		String serviceType = serviceTypeRequest.getContent();
		if (serviceType.equals(ServiceType.TAKE_BLOCK)) {
			if (!floor.hasFinishedBlock()) {
				return failureMessage(serviceTypeRequest);
			}
			return replyBlock(serviceTypeRequest);
		} else if (serviceType.equals(ServiceType.GIVE_BLOCK)) {
			if (floor.hasBlock()) {
				return failureMessage(serviceTypeRequest);
			}
			return agreeMessage(serviceTypeRequest);
		} else if (serviceType.equals(ServiceType.I_OCCUPY)) {
			if (floor.isOccupied()) {
				return failureMessage(serviceTypeRequest);
			} else {
				return agreeMessage(serviceTypeRequest);
			}
		}
		return failureMessage(serviceTypeRequest);
	}

	@Override
	public ACLMessage notifyGrid() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(GridAgent);
		//TODO: message.setContentObject(this.floor);
		return message;
	}

}
