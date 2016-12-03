package gseproject.passive;

import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.passive.communicator.GoalPaletteCommunicator;
import gseproject.passive.communicator.IStationCommunicator;
import gseproject.passive.communicator.ServiceTypeResponder;
import gseproject.passive.core.GoalPalette;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;

public class GoalpaletteAgent extends Agent {
	private static final long serialVersionUID = -1104113813018831544L;
	private GoalPalette goalPalette;
	private IStationCommunicator stationCommunicator;
	private MessageTemplate robotServiceRequestTemplate;

	@Override
	protected void setup() {
		this.goalPalette = new GoalPalette(5);
		this.stationCommunicator = new GoalPaletteCommunicator(this.goalPalette);
		this.robotServiceRequestTemplate = MessageTemplate.MatchProtocol(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_GOAL_PALETTE_PROTOCOL);
		this.addBehaviour(new ServiceTypeResponder(robotServiceRequestTemplate, stationCommunicator));
	}

	@Override
	protected void takeDown() {

	}

}
