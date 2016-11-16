package gseproject.passive.floor.communicator;

import java.io.IOException;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.passive.floor.core.Floor;
import gseproject.passive.floor.core.FloorException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;


/**
 * @author Tobias
 * The floor can receive REQUESTS and INFORM.
 * REQUESTS means that a robot is asking a floor to perform an action (GIVE_BLOCK, TAKE_BLOCK).
 * One a request came in, the floor checks whether the REQUEST is need or not.
 * 
 * If a REQUEST is needed, the floor sends an INFORM to the robot. The robot then knows, that he can start his action.
 * Lastly, the robot INFORM the floor that he has finished performing his action. 
 * The floor can then update the state by calling the specific method e.g. floor.takeblock().
 * 
 * If a REQUEST is not needed, the floor sends REFUSE to the robot. The robot then knows, that he cannot start his action.
 * 
 * If something fails, the floor sends a FAILURE to the robot. The robot then knows, that he cannot start his action. 
 */
public class FloorBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = -2955978898186249576L;
    private Floor floor;

    public FloorBehaviour(Agent a, Floor f) {
	super(a);
	this.floor = f;
    }

    private static MessageTemplate requestAndInform() {
	return MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
		MessageTemplate.MatchPerformative(ACLMessage.INFORM));
    }

    @Override
    public void action() {
	System.out.println("current floorstate: " + this.floor.toString());
	ACLMessage messageFromRobot = myAgent.blockingReceive(requestAndInform());
	handleMessageFromRobot(messageFromRobot);
    }

    private void handleMessageFromRobot(ACLMessage messageFromRobot) {
	if (messageFromRobot.getPerformative() == ACLMessage.REQUEST) {
	    replyRequest(messageFromRobot);
	} else if (messageFromRobot.getPerformative() == ACLMessage.INFORM) {
	    handleInform(messageFromRobot);
	}
    }

    private void replyRequest(ACLMessage request) {
	ACLMessage reply = request.createReply();
	ServiceType serviceType = null;
	try {
	    serviceType = (ServiceType) request.getContentObject();
	    System.out.println("Robot asked: " + serviceType.name());
	} catch (UnreadableException e) {
	    e.printStackTrace();
	}
	switch (serviceType) {
	case GIVE_BLOCK: {
	    if (!floor.hasBlock()) {
		reply.setPerformative(ACLMessage.AGREE);
	    } else {
		reply.setPerformative(ACLMessage.REFUSE);
	    }
	    myAgent.send(reply);
	    break;
	}
	case TAKE_BLOCK: {
	    if (floor.hasFinishedBlock()) {
		reply.setPerformative(ACLMessage.AGREE);
	    } else {
		reply.setPerformative(ACLMessage.REFUSE);
	    }
	    myAgent.send(reply);
	    break;
	}
	case FINISH_BLOCK: {
	    if(floor.hasBlock()){
		reply.setPerformative(ACLMessage.AGREE);
	    } else {
		reply.setPerformative(ACLMessage.REFUSE);
	    }
	}
	default:
	    break;
	}
    }

    private void handleInform(ACLMessage inform) {
	Block block = null;
	try {
	    block = (Block) inform.getContentObject();
	} catch (UnreadableException e) {
	    e.printStackTrace();
	}
	if (block == null) {
	    sendBlockToRobot(inform);
	} else {
	    System.out.println("Robot has sent me: " + block.Status);
	    try {
		floor.giveBlock(block);
	    } catch (FloorException e) {
		// TODO: send fail message
		e.printStackTrace();
	    }
	    System.out.println("New Floorstate: " + this.floor.toString());
	}

    }

    private void sendBlockToRobot(ACLMessage messageFromRobot) {
	ACLMessage msg = messageFromRobot.createReply();
	msg.setPerformative(ACLMessage.INFORM);
	Block b = null;
	try {
	    b = floor.takeBlock();
	} catch (FloorException fe) {
	    // TODO: send fail message
	    fe.printStackTrace();
	}
	try {
	    msg.setContentObject(b);
	} catch (IOException e) {
	    // TODO: send fail message
	    e.printStackTrace();
	}
	myAgent.send(msg);
    }

}
