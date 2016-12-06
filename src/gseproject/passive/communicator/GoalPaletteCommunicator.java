package gseproject.passive.communicator;

import java.io.IOException;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.passive.core.GoalPalette;
import gseproject.passive.core.StationException;
import jade.core.AID;
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
	public ACLMessage updateTrack() {
		ACLMessage messageToTrackAgent = new ACLMessage(ACLMessage.INFORM);
		messageToTrackAgent.setProtocol(ProtocolTemplates.TrackProtocolTemplate.TRACK_PALETTE_PROTOCOL);
		try {
			messageToTrackAgent.setContentObject(this.goalPalette);
		} catch (IOException e) {
			e.printStackTrace();
		}
		messageToTrackAgent.addReceiver(new AID("TrackAgent", AID.ISLOCALNAME));
		return messageToTrackAgent;
	}

}
