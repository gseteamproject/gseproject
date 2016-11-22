package gseproject.passive.communicator;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.passive.core.GoalPalette;
import gseproject.passive.core.StationException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class GoalPaletteCommunicator extends StationCommunicator {
	private GoalPalette goalPalette;

	public GoalPaletteCommunicator(GoalPalette pallete) {
		this.goalPalette = (GoalPalette) pallete;
	}

	@Override
	public ACLMessage handleServiceTypeRequest(ACLMessage serviceTypeRequest) {
		Block block;
		try {
			block = (Block) serviceTypeRequest.getContentObject();
			if (block != null) {
				System.out.println(block);
				goalPalette.giveBlock(block);
				return informMessage(serviceTypeRequest);
			} else {
				return failureMessage(serviceTypeRequest);
			}
		} catch (StationException | UnreadableException e) {
			e.printStackTrace();
			return failureMessage(serviceTypeRequest);
		}
	}

	@Override
	public ACLMessage notifyGrid() {
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(super.GridAgent);
		// TODO: message.setContentObject(this.goalPalette);
		return message;
	}

}
