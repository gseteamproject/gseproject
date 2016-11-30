package gseproject.robot.communicator;

import java.io.IOException;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RobotToStationCommunicator implements IRobotToStationComm {
	private AID sourcePaletteAID;
	private AID cleaningFloorAID;
	private AID paintingFloorAID;
	private AID goalPaletteAID;
	private Agent robotAgent;
	private String currProtocol;

	public RobotToStationCommunicator(AID sourcePaletteAID, AID cleaningFloorAID, AID paintingFloorAID,
			AID goalPaletteAID, Agent robotAgent) {
		this.sourcePaletteAID = sourcePaletteAID;
		this.cleaningFloorAID = cleaningFloorAID;
		this.paintingFloorAID = paintingFloorAID;
		this.goalPaletteAID = goalPaletteAID;
		this.robotAgent = robotAgent;
	}

	private void takeBlock(AID receiver) {
		ACLMessage messageToSourcePalette = new ACLMessage(ACLMessage.REQUEST);
		messageToSourcePalette.addReceiver(receiver);
		messageToSourcePalette.setProtocol(this.currProtocol);
		messageToSourcePalette.setContent(ServiceType.TAKE_BLOCK.name());
		robotAgent.send(messageToSourcePalette);
	}

	private void giveBlock(AID receiver, Block block) {
		ACLMessage messageToSourcePalette = new ACLMessage(ACLMessage.REQUEST);
		messageToSourcePalette.addReceiver(receiver);
		messageToSourcePalette.setProtocol(this.currProtocol);
		try {
			messageToSourcePalette.setContentObject(block);
		} catch (IOException e) {
			e.printStackTrace();
		}
		robotAgent.send(messageToSourcePalette);
	}

	private void finishBlock(AID receiver) {
		ACLMessage messageToFloor = new ACLMessage(ACLMessage.REQUEST);
		messageToFloor.addReceiver(receiver);
		messageToFloor.setProtocol(this.currProtocol);
		messageToFloor.setContent(ServiceType.FINISH_BLOCK.name());
		robotAgent.send(messageToFloor);
	}
	
	private void occupyBlock(AID receiver){
		ACLMessage messageToFloor = new ACLMessage(ACLMessage.REQUEST);
		messageToFloor.addReceiver(receiver);
		messageToFloor.setProtocol(this.currProtocol);
		messageToFloor.setContent(ServiceType.I_OCCUPY.name());
		robotAgent.send(messageToFloor);
	}
	
	@Override
	public void requestDirtyBlock() {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_SOURCE_PALETTE_PROTOCOL;
		takeBlock(this.sourcePaletteAID);
	}

	@Override
	public void requestCleanedBlock() {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_CLEANING_FLOOR_PROTOCOL;
		takeBlock(this.cleaningFloorAID);
	}

	@Override
	public void requestPaintedBlock() {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_PAINTING_FLOOR_PROTOCOL;
		takeBlock(this.paintingFloorAID);
	}

	@Override
	public ACLMessage receiveReply() {
		return this.robotAgent.blockingReceive(MessageTemplate.MatchProtocol(this.currProtocol));
	}

	public String currProtocol() {
		return this.currProtocol;
	}

	@Override
	public void giveCleanedBlock(Block cleanedBlock) {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_PAINTING_FLOOR_PROTOCOL;
		giveBlock(this.paintingFloorAID, cleanedBlock);
	}

	@Override
	public void giveDirtyBlock(Block dirtyBlock) {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_CLEANING_FLOOR_PROTOCOL;
		giveBlock(this.cleaningFloorAID, dirtyBlock);
	}

	@Override
	public void givePaintedBlock(Block paintedBlock) {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_GOAL_PALETTE_PROTOCOL;
		giveBlock(this.goalPaletteAID, paintedBlock);
	}

	@Override
	public void requestCleanBlock() {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_CLEANING_FLOOR_PROTOCOL;
		finishBlock(this.cleaningFloorAID);
	}

	@Override
	public void requestPaintBlock() {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_PAINTING_FLOOR_PROTOCOL;
		finishBlock(this.paintingFloorAID);
	}

	@Override
	public void requestOccupyCleaningFloor() {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_CLEANING_FLOOR_PROTOCOL;
		occupyBlock(this.cleaningFloorAID);
	}

	@Override
	public void requestOccupyPaintingFloor() {
		this.currProtocol = ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_PAINTING_FLOOR_PROTOCOL;
		occupyBlock(this.paintingFloorAID);
	}

}
