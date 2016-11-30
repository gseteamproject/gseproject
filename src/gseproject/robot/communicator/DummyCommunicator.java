package gseproject.robot.communicator;

import gseproject.core.ICallbackArgumented;
import gseproject.core.grid.Position;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.infrastructure.contracts.RobotGoalContract;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotGoalReader;
import gseproject.infrastructure.serialization.robot.RobotGoalWriter;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import gseproject.robot.RobotAgent;
import gseproject.robot.domain.RobotState;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

import java.io.IOException;

public class DummyCommunicator implements ICommunicator {

    private RobotAgent _robot;

    private SerializationController _serializationController;

    public DummyCommunicator(RobotAgent robot) {
        _robot = robot;

        _serializationController = SerializationController.Instance;

        //initiateSerialization();
    }

/*    private void initiateSerialization() {
        RobotStateWriter stateWriter = new RobotStateWriter();
        RobotStateReader stateReader = new RobotStateReader();
        _serializationController.RegisterSerializator(RobotStateContract.class, stateWriter, stateReader);

        RobotGoalWriter goalWriter = new RobotGoalWriter();
        RobotGoalReader goalReader = new RobotGoalReader();
        _serializationController.RegisterSerializator(RobotGoalContract.class, goalWriter, goalReader);
    }*/


    public void notifyGridAgent(RobotState state) {
        AID receiverAgent = new AID("GridAgent", AID.ISLOCALNAME);
        RobotStateContract contract = _robotStateConverter(state);

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(ProtocolTemplates.RobotProtocolTemplates.ROBOT_STATE_PROTOCOL);
        msg.addReceiver(receiverAgent);
        try{
            msg.setContentObject(contract);
        }
        catch (IOException e){

        }
        SimpleAchieveREInitiator robotStateInitiator = new SimpleAchieveREInitiator(_robot, msg);

        _robot.addBehaviour(robotStateInitiator);
    }

    public void getRoute(AID aid, RobotState state, ICallbackArgumented<Position> callback){
        RobotGoalContract contract = new RobotGoalContract(aid, state._position, null);

        AID receiverAgent = new AID("GridAgent", AID.ISLOCALNAME);
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(ProtocolTemplates.RobotProtocolTemplates.ROBOT_ROUTE_PROTOCOL);
        msg.addReceiver(receiverAgent);
        try{
            msg.setContentObject(contract);
        }
        catch (IOException e){

        }
        /*
        RobotRouteInitiator initiator = new RobotRouteInitiator(_robot, msg, callback);
        _robot.addBehaviour(initiator);
        
*/
    }

    private RobotStateContract _robotStateConverter(RobotState state){
        RobotStateContract contract = new RobotStateContract();
        contract.isCarryingBlock = state.isCarryingBlock;
        contract.position = state._position;
        return contract;
    }
}
