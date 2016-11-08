package gseproject.robot.communicator;

import gseproject.core.State;
import gseproject.core.grid.GridSpace;
import gseproject.robot.processing.IProcessor;
import jade.core.AID;
import jade.core.Agent;

import java.util.List;


public class DummyCommunicator extends Agent implements ICommunicator{

    /* Processor */
    private IProcessor _processor;
    private Agent _robot;

    public void DummyCommunicator(Agent robot){
        _robot = robot;
    }

    public void initiate(IProcessor Processor){
        _processor = Processor;
    }



    public Object receiveReply() {
        return null;
    }

    public void informState(Object info) {

    }

    public void broadcastPosition(GridSpace position) {

    }

    public void informAboutBestRobot(AID bestRobot, List<AID> allRobots) {

    }

    public void sendService() {

    }

    public GridSpace receivePosition() {

        return _processor.getCurrentPosition();
    }

    public void informGUIState(State State) {

    }
}
