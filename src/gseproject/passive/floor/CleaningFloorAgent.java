package gseproject.passive.floor;

import java.io.IOException;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class CleaningFloorAgent extends FloorAgent {

    /**
     * 
     */
    private static final long serialVersionUID = -7283341637605998716L;

    private static MessageTemplate requestAndInform() {
	return MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
		MessageTemplate.MatchPerformative(ACLMessage.INFORM));
    }

    @Override
    protected void setup() {
	this.floor = new CleaningFloor();
	this.addBehaviour(new CyclicBehaviour(this) {

	    private static final long serialVersionUID = 1404030294876210389L;

	    @Override
	    public void action() {
		ACLMessage messageFromRobot = blockingReceive(requestAndInform());
		handleMessageFromRobot(messageFromRobot);
	    }

	});

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
	} catch (UnreadableException e) {
	    e.printStackTrace();
	}
	switch (serviceType) {
	case GIVE_BLOCK_DIRTY: {
	    if (!floor.hasBlock) {
		reply.setPerformative(ACLMessage.AGREE);
	    } else {
		reply.setPerformative(ACLMessage.REFUSE);
	    }
	    send(reply);
	    break;
	}
	case TAKE_BLOCK: {
	    if (!floor.hasFinishedBlock) {
		reply.setPerformative(ACLMessage.AGREE);
	    } else {
		reply.setPerformative(ACLMessage.REFUSE);
	    }
	    send(reply);
	    break;
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
	    try {
		floor.giveBlock(block);
	    } catch (FloorException e) {
		// TODO: send fail message
		e.printStackTrace();
	    }
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
	send(msg);
    }

    @Override
    protected void takeDown() {

    }

}
