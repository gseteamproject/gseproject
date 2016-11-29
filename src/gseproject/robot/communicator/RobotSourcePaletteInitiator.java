package gseproject.robot.communicator;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.passive.core.StationException;
import gseproject.robot.domain.RobotState;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREInitiator;

public class RobotSourcePaletteInitiator extends SimpleAchieveREInitiator {
	private static final long serialVersionUID = 5846740100535003193L;
	private RobotState robotState;
	public int exitValue=2;

	private static ACLMessage messageToSourcePalette() {
		ACLMessage messageToSourcePalette = new ACLMessage(ACLMessage.REQUEST);
		messageToSourcePalette.addReceiver(new AID("SourcePalette", AID.ISLOCALNAME));
		messageToSourcePalette.setProtocol(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_SOURCE_PALETTE_PROTOCOL);
		messageToSourcePalette.setContent(ServiceType.TAKE_BLOCK.name());
		return messageToSourcePalette;
	}

	public RobotSourcePaletteInitiator(Agent a, RobotState robotState) {
		super(a, messageToSourcePalette());
		this.robotState = robotState;
		System.out.println("Asking sourcePalette for a block");
	}

	protected void handleFailure(ACLMessage failure) {
		System.out.println("got failure");
		exitValue = 1;
	}

	protected void handleInform(ACLMessage inform) {
		System.out.println("got success");
		Block block = null;
		try {
			block = (Block) inform.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		if (block != null) {
			updateRobotState(block);
		} else {
			System.out.println("Block somehow is null");
		}
		System.out.println("Communication with sourcepalette ended:" + robotState);
		exitValue=0;
		done();
	}
	
	private synchronized void updateRobotState(Block block){
		this.robotState.block = new Block();
		this.robotState.block.Status = block.Status;
		this.robotState.isCarryingBlock = true;
	}
}
