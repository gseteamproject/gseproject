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

	public interface TrackProtocolTemplate {
		String TRACK_ROBOT_STATE_PROTOCOL = "track-robot-state-protocol";
		String TRACK_FLOOR_PROTOCOL = "track-floor-protocol";
		String TRACK_PALETTE_PROTOCOL = "track-palette-protocol";
	}
	
	public interface GUIProtocolTemplate {
		String GUI_TRACK_PROTOCOL = "gui-track-protocol";
	}

}
