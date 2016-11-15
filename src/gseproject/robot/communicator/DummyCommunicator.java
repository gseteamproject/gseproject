package gseproject.robot.communicator;

import gseproject.core.grid.GridSpace;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import gseproject.robot.RobotAgent;
import gseproject.robot.domain.RobotState;
import gseproject.robot.processing.IProcessor;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.List;

public class DummyCommunicator implements ICommunicator {

    private RobotAgent _robot;
    private RobotStateSubscriptionResponder _stateSubscriptionResponder;

    public DummyCommunicator(RobotAgent robot) {
	_robot = robot;

	_stateSubscriptionResponder = new RobotStateSubscriptionResponder(robot);
	robot.addBehaviour(_stateSubscriptionResponder);

	initiateSerialization();
    }

    private void initiateSerialization() {
	RobotStateWriter writer = new RobotStateWriter();
	RobotStateReader reader = new RobotStateReader();

	SerializationController.Instance.RegisterSerializator(RobotStateContract.class, writer, reader);
    }

    public void notifyState(RobotState state) {
	RobotStateContract stateContract = new RobotStateContract();
	stateContract.isCarryingBlock = state.isCarryingBlock;
	stateContract.position = state.position;

	ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
	msg.setContent(SerializationController.Instance.Serialize(stateContract));
	_stateSubscriptionResponder.notify(msg);
    }

    public Object receiveReply() {
	return null;
    }

    public void broadcastPosition(GridSpace position) {

    }

    public void informAboutBestRobot(AID bestRobot, List<AID> allRobots) {

    }

    public void sendService() {

    }

    public void informGUIState(RobotState State) {

    }
}
