package gseproject.robot.processing;

import gseproject.SpaceType;
import gseproject.grid.GridSpace;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.controller.IController;
import jade.core.AID;

public interface IRobotProcessor {

    public void connect(IController Controller);
    public void connect(ICommunicator Communicator);
    GridSpace[] buildWay(SpaceType spaceType, AID aid);
    public GridSpace getCurrentPosition();

}
