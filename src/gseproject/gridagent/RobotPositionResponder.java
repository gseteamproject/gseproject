package gseproject.gridagent;

import gseproject.core.Direction;
import gseproject.core.grid.Grid;
import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.robot.domain.RobotState;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RobotPositionResponder extends CyclicBehaviour {

	private static final long serialVersionUID = -639699365150760529L;
	private static SerializationController sc = SerializationController.Instance;
	private MessageTemplate msgTemplate;
	private Grid grid;

	public RobotPositionResponder(Agent a, MessageTemplate msgTemplate, Grid grid) {
		super(a);
		this.msgTemplate = msgTemplate;
		this.grid = grid;
	}

	public RobotPositionResponder(Agent a, Grid grid) {
		super(a);
		msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		this.grid = grid;
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.blockingReceive(msgTemplate);
		ACLMessage reply = msg.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		RobotStateContract robotStateContract = sc.Deserialize(RobotStateContract.class, msg.getContent());
		System.out.println("state of robot: " + robotStateContract.toString());
		reply.setContent(sc.Serialize(updateRobotState(robotStateContract)));
		myAgent.send(reply);
	}

	private RobotStateContract updateRobotState(RobotStateContract state) {
		RobotStateContract newState = new RobotStateContract();

		newState.isCarryingBlock = state.isCarryingBlock;
		newState.direction = state.direction;
		newState.goal = state.goal;
		if (!canStickDirection(state.position, state.direction)) {
			newState.direction = changeDirection(state.direction);
		}
		newState.position = getNextPosition(state.position, state.direction);
		return newState;
	}

	private Position getNextPosition(Position start, Direction direction) {
		switch (direction) {
			case NORTH:
				return new Position(start.getX(), start.getY() - 1);
			case SOUTH:
				return new Position(start.getX(), start.getY() + 1);
			case EAST:
				return new Position(start.getX() + 1, start.getY());
			case WEST:
				return new Position(start.getX() - 1, start.getY());
		}
		return start;
	}

	private boolean canStickDirection(Position start, Direction direction) {
		Position newPosition = getNextPosition(start, direction);
		if (!grid.get(newPosition.getX(), newPosition.getY()).equals(SpaceType.NO_TRACK)) {
			return true;
		} else {
			return false;
		}
	}

	private Direction changeDirection(Direction direction) {
		switch (direction) {
			case NORTH:
				return Direction.EAST;
			case SOUTH:
				return Direction.WEST;
			case EAST:
				return Direction.SOUTH;
			case WEST:
				return Direction.NORTH;
		}
		return direction;
	}

}
