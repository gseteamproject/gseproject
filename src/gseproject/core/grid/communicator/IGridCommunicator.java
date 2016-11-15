package gseproject.core.grid.communicator;

import gseproject.core.grid.Grid;
import jade.lang.acl.ACLMessage;

public interface IGridCommunicator {
    public void notifyGUI(ACLMessage messageFromRobot);
    public ACLMessage agreeRobot(ACLMessage messageFromRobot);
}
