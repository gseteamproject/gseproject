package gseproject.core.grid.communicator;

import gseproject.infrastructure.contracts.RobotStateContract;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public interface IGridCommunicator {
    public ACLMessage sendInitialGrid(ACLMessage messageFromGUI);
    
    public RobotStateContract parseRobotStateFromMessage(ACLMessage messageFromRobot);

    public ACLMessage sendRobotStateToGUI(RobotStateContract robotState);

    public ACLMessage agreeRobot(ACLMessage messageFromRobot);

    public AID GUI_AID();
}
