package gseproject.robot.communicator;

import gseproject.IGridSpace;
import gseproject.State;
import gseproject.active.IRobotToRobotComm;
import gseproject.active.IRobotToStationComm;
import gseproject.IAgentToGUIComm;
import gseproject.active.Robot;
import gseproject.robot.processing.IRobotProcessor;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import java.util.List;


public class DummyCommunicator extends Agent implements ICommunicator, IAgentToGUIComm, IRobotToRobotComm, IRobotToStationComm {

    /* Processor */
    private IRobotProcessor _processor;

    public void connect(IRobotProcessor Processor){
        _processor = Processor;
    }
    private Robot robot;
    private List<Behaviour> behaviours;
    private static final long serialVersionUID = 669015027861824282L;

    public Object receiveReply() {
        return null;
    }

    public void informState(Object info) {

    }

    public void broadCastPosition(IGridSpace Position) {
    }

    public void informAboutBestRobot(AID bestRobot, List<AID> allRobots) {

    }

    public void sendService() {

    }

    public IGridSpace receivePosition() {

        return _processor.getCurrentPosition();
    }

    public void informGUIState(State State) {

    }


}
