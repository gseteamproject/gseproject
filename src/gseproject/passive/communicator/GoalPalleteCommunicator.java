package gseproject.passive.communicator;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.passive.core.StationException;
import gseproject.passive.pallete.core.GoalPalette;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class GoalPalleteCommunicator extends StationCommunicator {
	private GoalPalette goalPalette;

	public GoalPalleteCommunicator(GoalPalette pallete) {
		this.goalPalette = (GoalPalette) pallete;
	}

	@Override
	public ACLMessage handleServiceTypeRequest(ACLMessage serviceTypeRequest) {
		if (serviceTypeRequest == null || serviceTypeRequest.getContent() == null) {
			return failureMessage(serviceTypeRequest);
		}
		String serviceType = serviceTypeRequest.getContent();
		if (serviceType.equals(ServiceType.GIVE_BLOCK)) {
			Block block;
			try {
				block = (Block) serviceTypeRequest.getContentObject();
				goalPalette.giveBlock(block);
			} catch (StationException | UnreadableException e) {
				e.printStackTrace();
				return failureMessage(serviceTypeRequest);
			}
		}
		return agreeMessage(serviceTypeRequest);
	}

	@Override
	public ACLMessage notifyGrid() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(super.GridAgent);
		//TODO: message.setContentObject(this.goalPalette);
		return message;
	}

}
