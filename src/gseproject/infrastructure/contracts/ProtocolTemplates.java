package gseproject.infrastructure.contracts;

public interface ProtocolTemplates {

	public interface RobotProtocolTemplates {

		String ROBOT_STATE_PROTOCOL = "robot-state-protocol";

		String ROBOT_ROUTE_PROTOCOL = "robot-route-protocol";

	}

	public interface ServiceTypeProtocolTemplate {
		String ROBOT_CLEANING_FLOOR_PROTOCOL = "robot-cleaning-floor-protocol";
		String ROBOT_PAINTING_FLOOR_PROTOCOL = "robot-painting-floor-protocol";
		String ROBOT_SOURCE_PALETTE_PROTOCOL = "robot-source-palette-protocol";
		String ROBOT_GOAL_PALETTE_PROTOCOL = "robot-goal-palette-protocol";
	}

	public interface GridAgentProtocolTemplate {
		String GRID_ROBOT_STATE_PROTOCOL = "gui-robot-state-protocol";
		String ROBOT_GRIDAGENT_PROTOCOL = "robot_gridagent_protocol";
		String FLOOR_GRIDAGENT_PROTOCOL = "floor_gridagent_protocol";
		String PALETTE_GRIDAGENT_PROTOCOL = "palette_gridagent_protocol";
	}

}
