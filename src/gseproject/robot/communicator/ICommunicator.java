package gseproject.robot.communicator;

import gseproject.robot.domain.RobotState;

public interface ICommunicator {

    void notifyGridAgent(RobotState state);
}
