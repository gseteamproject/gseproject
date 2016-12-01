package gseproject.core.grid.communicator;

public interface IGridCommunicator {
	/**
	 * This method should wait for a message from robot and extract the state
	 * from the message. After extracting the state from the message it should
	 * update the grid.
	 */
	public void receiveRobotStateContract();

	/**
	 * This method should wait for a message from a floor and extract the state
	 * from the message. After extracting the state from the message it should
	 * update the grid.
	 */
	public void receiveFloorContract();

	/**
	 * This method should wait for a message from a palette and extract the
	 * state from the message. After extracting the state from the message it
	 * should update the grid.
	 */
	public void receivePaletteContract();

	/**
	 * This method should send the grid to the GUI-Agent.
	 */
	public void sendGridToGUIAgent();

}
