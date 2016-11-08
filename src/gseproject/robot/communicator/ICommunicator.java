package gseproject.robot.communicator;

import gseproject.core.grid.GridSpace;
import gseproject.robot.processing.IProcessor;

public interface ICommunicator {

    void initiate(IProcessor processor);

    void broadcastPosition(GridSpace position);

}