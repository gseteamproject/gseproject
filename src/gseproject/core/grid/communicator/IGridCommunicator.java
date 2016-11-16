package gseproject.core.grid.communicator;

import gseproject.core.grid.Grid;
import gseproject.infrastructure.contracts.RobotStateContract;
import jade.lang.acl.ACLMessage;

public interface IGridCommunicator {
	public void sendRobotStateToGUI(RobotStateContract robotState);
	public void sendInitialGridToGUI(Grid grid);
	public ACLMessage agreeRobot(ACLMessage messageFromRobot);
}
