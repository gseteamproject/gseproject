package gseproject.core.grid.communicator;

import gseproject.passive.core.Floor;
import gseproject.passive.core.Palette;
import gseproject.robot.domain.RobotState;

public interface ITrackCommunicator {
	/**
	 * This method should wait for a message from robot and extract the state
	 * from the message. After extracting the state from the message it should
	 * update the grid.
	 */
	public RobotState receiveRobotState();

	/**
	 * This method should wait for a message from a floor and extract the state
	 * from the message. After extracting the state from the message it should
	 * update the grid.
	 */
	public Floor receiveFloor();

	/**
	 * This method should wait for a message from a palette and extract the
	 * state from the message. After extracting the state from the message it
	 * should update the grid.
	 */
	public Palette receivePalette();

}
