package gseproject.robot.communicator;

import gseproject.core.ICallbackArgumented;
import gseproject.core.grid.Position;
import gseproject.infrastructure.contracts.RobotGoalContract;
import gseproject.infrastructure.serialization.SerializationController;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

public class RobotRouteInitiator extends SimpleAchieveREInitiator{

    private ICallbackArgumented<Position> _callback;
    private SerializationController _serializationController;

    public RobotRouteInitiator(Agent a, ACLMessage msg, ICallbackArgumented<Position> callback) {
        super(a, msg);
        _serializationController = SerializationController.Instance;
    }


    protected void handleAgree(ACLMessage msg){
        RobotGoalContract contract = _serializationController.Deserialize(RobotGoalContract.class, msg.getContent());
        System.out.print(contract.position.toString());

        _callback.invoke(contract.position);
    }
}
