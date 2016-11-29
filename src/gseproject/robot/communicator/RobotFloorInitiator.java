package gseproject.robot.communicator;

import java.io.IOException;

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

public class RobotFloorInitiator extends SimpleAchieveREInitiator {
	private static final long serialVersionUID = 601886245234087074L;
	private RobotState robotState;
	private ServiceType serviceType;
	public int exitValue = 2;

	private static ACLMessage messageToCleaningFloor(AID receiver) {
		ACLMessage messageToSourcePalette = new ACLMessage(ACLMessage.REQUEST);
		messageToSourcePalette.addReceiver(receiver);
		messageToSourcePalette.setProtocol(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_CLEANING_FLOOR_PROTOCOL);
		return messageToSourcePalette;
	}

	private static ACLMessage messageToFloorServiceType(AID receiver, ServiceType serviceType) {
		ACLMessage messageToFloor = messageToCleaningFloor(receiver);
		messageToFloor.setContent(serviceType.name());
		return messageToFloor;
	}

	private static ACLMessage messgeToFloorGiveBlock(AID receiver, Block block) {
		ACLMessage messageToFloor = messageToCleaningFloor(receiver);
		try {
			System.out.println(block);
			messageToFloor.setContentObject(block);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messageToFloor;
	}

	public RobotFloorInitiator(Agent a, RobotState robotState, ServiceType serviceType, AID receiver) {
		super(a, messageToFloorServiceType(receiver, serviceType));
		this.robotState = robotState;
		this.serviceType = serviceType;
		System.out.println("Starting conversation with floor");
	}

	public RobotFloorInitiator(Agent a, RobotState robotState, AID receiver) {
		super(a, messgeToFloorGiveBlock(receiver, robotState.block));
		this.robotState = robotState;
		System.out.println("Starting conversation with floor " + robotState);
	}

	protected void handleFailure(ACLMessage failure) {
		System.out.println("could not perform my action.");
		exitValue=1;
	}

	protected void handleInform(ACLMessage inform) {
		if (serviceType == null) {
			Block block = null;
			try {
				block = (Block) inform.getContentObject();
			} catch (UnreadableException e) {
				e.printStackTrace();
				exitValue=1;
			}
			this.robotState.isCarryingBlock = true;
			this.robotState.block = block;
		} else {
			if (serviceType.name().equals(ServiceType.GIVE_BLOCK)) {
				this.robotState.block = new Block();
				this.robotState.isCarryingBlock = false;
				exitValue=0;
			} else if (serviceType.name().equals(ServiceType.TAKE_BLOCK.name())) {
				exitValue=0;
			} else if (serviceType.name().equals(ServiceType.I_OCCUPY.name())) {
				exitValue=0;
			} else if (serviceType.name().equals(ServiceType.I_LEAVE.name())) {
				exitValue=0;
			} else {
				exitValue=1;
			}
		}
	}
}
