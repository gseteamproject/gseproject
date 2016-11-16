package gseproject.core.grid.communicator;

import gseproject.infrastructure.contracts.RobotStateContract;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public interface IGridCommunicator {
	public void sendRobotStateToGUI(RobotStateContract robotState);
	public ACLMessage sendInitialGrid(ACLMessage messageFromGUI);
	public ACLMessage agreeRobot(ACLMessage messageFromRobot);
	public AID GUI_AID();
}
