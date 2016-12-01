package gseproject.core.grid.communicator;

import java.io.IOException;

import gseproject.core.grid.Grid;
import gseproject.core.grid.objects.GridObject;
import gseproject.infrastructure.contracts.*;
import gseproject.robot.domain.RobotState;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class GridCommunicator implements IGridCommunicator {
	private Agent gridAgent;
	private Grid grid;
	private RobotState rs;

	public GridCommunicator(Agent gridAgent, Grid grid, MessageTemplate msg) {
		this.gridAgent = gridAgent;
		this.grid = grid;

	}

	@Override
	public void receiveRobotStateContract() {
		ACLMessage robotStateMessage = this.gridAgent.blockingReceive(
				MessageTemplate.MatchProtocol(ProtocolTemplates.GridAgentProtocolTemplate.GRID_ROBOT_STATE_PROTOCOL));
		RobotStateContract contract;
		try {
			contract = (RobotStateContract) robotStateMessage.getContentObject();
			grid.getGridObjects().put(robotStateMessage.getSender(), (GridObject) contract);
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receiveFloorContract() {
		ACLMessage floorMessage = this.gridAgent.blockingReceive(
				MessageTemplate.MatchProtocol(ProtocolTemplates.GridAgentProtocolTemplate.FLOOR_GRIDAGENT_PROTOCOL));
		try {
			FloorContract contract = (FloorContract) floorMessage.getContentObject();
			grid.getGridObjects().put(floorMessage.getSender(), contract);
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void receivePaletteContract() {
		ACLMessage paletteMessage = this.gridAgent.blockingReceive(
				MessageTemplate.MatchProtocol(ProtocolTemplates.GridAgentProtocolTemplate.FLOOR_GRIDAGENT_PROTOCOL));
		try {
			PaletteContract contract = (PaletteContract) paletteMessage.getContentObject();
			grid.getGridObjects().put(paletteMessage.getSender(), contract);
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void sendGridToGUIAgent() {
		// TODO Auto-generated method stub

	}
	
	public ACLMessage sendGrid() {
		ACLMessage update = new ACLMessage(ACLMessage.REQUEST);
		try {
			update.setContentObject(this.grid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return update;
	}

}
