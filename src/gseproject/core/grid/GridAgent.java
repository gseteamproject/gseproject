package gseproject.core.grid;

import gseproject.core.grid.communicator.GridCommunicator;
import gseproject.core.grid.communicator.IGridCommunicator;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import gseproject.core.grid.Grid.GridBuilder;

public class GridAgent extends Agent {
	private static final long serialVersionUID = -9064311405865477921L;
	private Grid grid;
	private IGridCommunicator gridCommunicator;

	private static MessageTemplate getMessageTemplate() {
		return AchieveREResponder.createMessageTemplate(ProtocolTemplates.RobotProtocolTemplates.ROBOT_STATE_PROTOCOL);
	}

	private Grid initGrid() {
		return new GridBuilder(20, 11)
				// source palettes
				.setSpaceType(0, 0, SpaceType.PALETTE_SOURCE).setSpaceType(1, 0, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 1, SpaceType.PALETTE_SOURCE).setSpaceType(1, 1, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 2, SpaceType.PALETTE_SOURCE).setSpaceType(1, 2, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 4, SpaceType.PALETTE_SOURCE).setSpaceType(1, 4, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 5, SpaceType.PALETTE_SOURCE).setSpaceType(1, 5, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 6, SpaceType.PALETTE_SOURCE).setSpaceType(1, 6, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 8, SpaceType.PALETTE_SOURCE).setSpaceType(1, 8, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 9, SpaceType.PALETTE_SOURCE).setSpaceType(1, 9, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 10, SpaceType.PALETTE_SOURCE).setSpaceType(1, 10, SpaceType.PALETTE_SOURCE)
				// goal palettes
				.setSpaceType(18, 0, SpaceType.PALETTE_GOAL).setSpaceType(19, 0, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 1, SpaceType.PALETTE_GOAL).setSpaceType(19, 1, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 2, SpaceType.PALETTE_GOAL).setSpaceType(19, 2, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 4, SpaceType.PALETTE_GOAL).setSpaceType(19, 4, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 5, SpaceType.PALETTE_GOAL).setSpaceType(19, 5, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 6, SpaceType.PALETTE_GOAL).setSpaceType(19, 6, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 8, SpaceType.PALETTE_GOAL).setSpaceType(19, 8, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 9, SpaceType.PALETTE_GOAL).setSpaceType(19, 9, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 10, SpaceType.PALETTE_GOAL).setSpaceType(19, 10, SpaceType.PALETTE_GOAL)
				// main track
				.setSpaceType(3, 0, SpaceType.TRACK).setSpaceType(4, 0, SpaceType.TRACK)
				.setSpaceType(5, 0, SpaceType.TRACK).setSpaceType(6, 0, SpaceType.TRACK)
				.setSpaceType(7, 0, SpaceType.TRACK).setSpaceType(8, 0, SpaceType.TRACK)
				.setSpaceType(9, 0, SpaceType.TRACK).setSpaceType(10, 0, SpaceType.TRACK)
				.setSpaceType(11, 0, SpaceType.TRACK).setSpaceType(12, 0, SpaceType.TRACK)
				.setSpaceType(13, 0, SpaceType.TRACK).setSpaceType(14, 0, SpaceType.TRACK)
				.setSpaceType(15, 0, SpaceType.TRACK).setSpaceType(3, 10, SpaceType.TRACK)
				.setSpaceType(4, 10, SpaceType.TRACK).setSpaceType(5, 10, SpaceType.TRACK)
				.setSpaceType(6, 10, SpaceType.TRACK).setSpaceType(7, 10, SpaceType.TRACK)
				.setSpaceType(8, 10, SpaceType.TRACK).setSpaceType(9, 10, SpaceType.TRACK)
				.setSpaceType(10, 10, SpaceType.TRACK).setSpaceType(11, 10, SpaceType.TRACK)
				.setSpaceType(12, 10, SpaceType.TRACK).setSpaceType(13, 10, SpaceType.TRACK)
				.setSpaceType(14, 10, SpaceType.TRACK).setSpaceType(15, 10, SpaceType.TRACK)
				.setSpaceType(3, 1, SpaceType.TRACK).setSpaceType(3, 2, SpaceType.TRACK)
				.setSpaceType(3, 3, SpaceType.TRACK).setSpaceType(3, 4, SpaceType.TRACK)
				.setSpaceType(3, 5, SpaceType.TRACK).setSpaceType(3, 6, SpaceType.TRACK)
				.setSpaceType(3, 7, SpaceType.TRACK).setSpaceType(3, 8, SpaceType.TRACK)
				.setSpaceType(3, 9, SpaceType.TRACK).setSpaceType(3, 10, SpaceType.TRACK)
				.setSpaceType(16, 0, SpaceType.TRACK).setSpaceType(16, 1, SpaceType.TRACK)
				.setSpaceType(16, 2, SpaceType.TRACK).setSpaceType(16, 3, SpaceType.TRACK)
				.setSpaceType(16, 4, SpaceType.TRACK).setSpaceType(16, 5, SpaceType.TRACK)
				.setSpaceType(16, 6, SpaceType.TRACK).setSpaceType(16, 7, SpaceType.TRACK)
				.setSpaceType(16, 8, SpaceType.TRACK).setSpaceType(16, 9, SpaceType.TRACK)
				.setSpaceType(16, 10, SpaceType.TRACK)
				// cleaning station
				.setSpaceType(7, 0, SpaceType.CLEANING_STATION_TRANSPORT).setSpaceType(7, 1, SpaceType.CLEANING_STATION)
				.setSpaceType(7, 2, SpaceType.CLEANING_STATION_WORKSPACE).setSpaceType(6, 1, SpaceType.TRACK)
				.setSpaceType(6, 2, SpaceType.TRACK).setSpaceType(8, 1, SpaceType.TRACK)
				.setSpaceType(8, 2, SpaceType.TRACK)
				// painting station
				.setSpaceType(12, 0, SpaceType.PAINTING_STATION_TRANSPORT)
				.setSpaceType(12, 1, SpaceType.PAINTING_STATION)
				.setSpaceType(12, 2, SpaceType.PAINTING_STATION_WORKSPACE).setSpaceType(11, 1, SpaceType.TRACK)
				.setSpaceType(11, 2, SpaceType.TRACK).setSpaceType(13, 1, SpaceType.TRACK)
				.setSpaceType(13, 2, SpaceType.TRACK)
				// finally...
				.build();

	}
	
	protected void setup() {
		System.out.println("Grid Agent started");
		gridCommunicator = new GridCommunicator(this);
		this.grid = initGrid();
		addBehaviour(new StateResponder(this, getMessageTemplate(), gridCommunicator));
	}

	protected void takeDown() {

	}

	public Grid getGrid() {
		return this.grid;
	}
}
