package gseproject.robot.communicator;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.robot.domain.RobotState;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREInitiator;

public class RobotGoalPaletteInitiator extends SimpleAchieveREInitiator {
	private static final long serialVersionUID = 6757202535698639128L;
	private RobotState robotState;

	private static ACLMessage messageToGoalPalette() {
		ACLMessage messageToSourcePalette = new ACLMessage(ACLMessage.REQUEST);
		messageToSourcePalette.addReceiver(new AID("GoalPalette", AID.ISLOCALNAME));
		messageToSourcePalette.setProtocol(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_GOAL_PALETTE_PROTOCOL);
		messageToSourcePalette.setContent(ServiceType.GIVE_BLOCK.name());
		return messageToSourcePalette;
	}

	public RobotGoalPaletteInitiator(Agent a, RobotState robotState) {
		super(a, messageToGoalPalette());
		this.robotState = robotState;
	}

	public RobotGoalPaletteInitiator(Agent a, ACLMessage msg, RobotState robotState) {
		super(a, msg);
		this.robotState = robotState;
	}

	public RobotGoalPaletteInitiator(Agent a, ACLMessage msg, DataStore store, RobotState robotState) {
		super(a, msg, store);
		this.robotState = robotState;
	}

	protected void handleFailure(ACLMessage failure) {
		System.out.println("could not perform my action.");
	}

	protected void handleInform(ACLMessage inform) {
		this.robotState.isCarryingBlock = false;
		this.robotState.block = new Block();
	}

}
