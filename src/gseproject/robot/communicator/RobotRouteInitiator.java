package gseproject.robot.communicator;

import gseproject.core.ICallbackArgumented;
import gseproject.core.grid.Position;
import gseproject.infrastructure.contracts.RobotGoalContract;
import gseproject.infrastructure.serialization.SerializationController;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREInitiator;

public class RobotRouteInitiator extends SimpleAchieveREInitiator{

    private ICallbackArgumented<Position> _callback;
    private SerializationController _serializationController;

    public RobotRouteInitiator(Agent a, ACLMessage msg, ICallbackArgumented<Position> callback) {
        super(a, msg);
        _serializationController = SerializationController.Instance;
        _callback = callback;
    }


    protected void handleInform(ACLMessage msg){
        RobotGoalContract contract = null;

        try {
            contract = (RobotGoalContract) msg.getContentObject();
        }
        catch (UnreadableException e){}

        if (contract == null)
            return;

        System.out.print(contract.goal.toString());

        _callback.invoke(contract.goal);
    }
}
