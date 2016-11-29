package gseproject.passive;

import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.passive.communicator.GoalPaletteCommunicator;
import gseproject.passive.communicator.IStationCommunicator;
import gseproject.passive.communicator.ServiceTypeResponder;
import gseproject.passive.core.GoalPalette;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class GoalpaletteAgent extends Agent {
	private static final long serialVersionUID = -1104113813018831544L;
	private GoalPalette goalPalette;
	private IStationCommunicator stationCommunicator;
	private MessageTemplate robotServiceRequestTemplate;

	@Override
	protected void setup() {
		this.robotServiceRequestTemplate = AchieveREResponder
				.createMessageTemplate(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_GOAL_PALETTE_PROTOCOL);
		this.goalPalette = new GoalPalette(5);
		this.stationCommunicator = new GoalPaletteCommunicator(this.goalPalette);
		
	}

	@Override
	protected void takeDown() {

	}

}
