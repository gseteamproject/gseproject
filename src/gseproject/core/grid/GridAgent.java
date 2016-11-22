package gseproject.core.grid;

import gseproject.core.Direction;
import gseproject.core.grid.Grid.GridBuilder;
import gseproject.core.grid.communicator.GridCommunicator;
import gseproject.core.grid.communicator.IGridCommunicator;
import gseproject.core.grid.communicator.RobotStateResponder;
import gseproject.core.grid.objects.GoalPalette;
import gseproject.core.grid.objects.SourcePalette;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.robot.domain.RobotState;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class GridAgent extends Agent {
	private static final long serialVersionUID = -9064311405865477921L;
	private Grid grid;
	private IGridCommunicator gridCommunicator;

	private static MessageTemplate getMessageTemplate() {
		return AchieveREResponder.createMessageTemplate(ProtocolTemplates.RobotProtocolTemplates.ROBOT_STATE_PROTOCOL);
	}

	private Grid initTestGrid() {
		return grid;
	}

	protected void setup() {
		System.out.println("Grid Agent started");
		gridCommunicator = new GridCommunicator(this);
		this.grid = initTestGrid();
		addBehaviour(new RobotStateResponder(this, getMessageTemplate(), gridCommunicator));
	}

	protected void takeDown() {

	}

	public Grid getGrid() {
		return this.grid;
	}
}
