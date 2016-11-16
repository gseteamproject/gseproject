package gseproject.tests.interaction.robot_grid;

import gseproject.core.Direction;
import gseproject.core.grid.Position;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class TestRobotMoveAgent extends Agent {

    private static final long serialVersionUID = -3909413982115309701L;
    private RobotStateContract robotState;
    private SerializationController sc = SerializationController.Instance;

    private void initializeRobotState() {
	robotState = new RobotStateContract();
	robotState.direction = Direction.EAST;
	robotState.position = new Position(1, 1);
	robotState.goal = new Position(5, 1);
	robotState.isCarryingBlock = false;
	System.out.println(robotState);
    }

    protected void setup() {
	initializeRobotState();
	addBehaviour(new CyclicBehaviour(this) {

	    private static final long serialVersionUID = -3437551186043229457L;
	    

	    @Override
	    public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID("GridAgent", AID.ISLOCALNAME));
		if(robotState == null){
		    System.out.println("If you read this you are stupid AS FUCK");
		}
		msg.setContent(sc.Serialize(robotState));
		if(sc.Deserialize(RobotStateContract.class, sc.Serialize(robotState)) == null){
		    System.out.println("why is that null....");
		}
		 
		send(msg);
		ACLMessage replyFromGridAgent = blockingReceive();
		if(replyFromGridAgent.getContent() == null){
		    System.out.println("reply-content from gridagent is null");
		}
		RobotStateContract reply = sc.Deserialize(RobotStateContract.class, replyFromGridAgent.getContent());
		if (reply.equals(robotState)) {
		    System.out.println("reached goal:" + robotState);
		    myAgent.doDelete();
		} else {
		    System.out.println(robotState);
		}

	    }

	});
    }

    protected void takeDown() {

    }
}
