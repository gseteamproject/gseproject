package gseproject.robot.communicator;

import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import gseproject.robot.RobotAgent;
import gseproject.robot.domain.RobotState;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class DummyCommunicator implements ICommunicator {

    private RobotAgent _robot;

    private SerializationController _serializationController;

    public DummyCommunicator(RobotAgent robot) {
        _robot = robot;

        _serializationController = SerializationController.Instance;

        initiateSerialization();
    }

    private void initiateSerialization() {
        RobotStateWriter writer = new RobotStateWriter();
        RobotStateReader reader = new RobotStateReader();

        _serializationController.RegisterSerializator(RobotStateContract.class, writer, reader);
    }


    public void notifyGridAgent(RobotState state) {
        AID receiverAgent = new AID("GridAgent", AID.ISLOCALNAME);
        RobotStateContract contract = _robotStateConverter(state);
        String content = _serializationController.Serialize(contract);

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        msg.addReceiver(receiverAgent);
        msg.setContent(content);
        RobotStateInitiator robotStateInitiator = new RobotStateInitiator(_robot, msg);

        _robot.addBehaviour(robotStateInitiator);
    }

    private RobotStateContract _robotStateConverter(RobotState state){
        RobotStateContract contract = new RobotStateContract();
        contract.isCarryingBlock = state.isCarryingBlock;
        contract.position = state.position;
        contract.goal = state.goal;
        contract.direction = state.direction;
        return contract;
    }
}
