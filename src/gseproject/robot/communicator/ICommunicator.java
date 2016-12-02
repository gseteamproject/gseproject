package gseproject.robot.communicator;

import gseproject.core.ICallbackArgumented;
import gseproject.grid.Position;
import gseproject.robot.domain.RobotState;
import jade.core.AID;

public interface ICommunicator {

    void notifyGridAgent(RobotState state);

    void getRoute(AID aid, RobotState state, ICallbackArgumented<Position> callback);
}
