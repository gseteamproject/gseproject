package gseproject.robot.communicator;

import gseproject.core.grid.GridSpace;
import gseproject.robot.domain.RobotState;
import gseproject.robot.processing.IProcessor;

public interface ICommunicator {

    void broadcastPosition(GridSpace position);

    void notifyState(RobotState state);
}
