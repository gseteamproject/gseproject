package gseproject.tests.interaction;

import java.io.IOException;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class TestAgentClean extends Agent {
    private static final long serialVersionUID = 3931523208882877185L;
    private Block block;

    protected void setup() {
	addBehaviour(new RequestCleaningFloor(ServiceType.GIVE_BLOCK_DIRTY));
    }

    protected void takeDown() {

    }

    private class RequestCleaningFloor extends OneShotBehaviour {
	private static final long serialVersionUID = 4747294158433105696L;

	private ServiceType serviceType;

	public RequestCleaningFloor(ServiceType serviceType) {
	    this.serviceType = serviceType;
	}

	@Override
	public void action() {
	    sendRequest();
	    ACLMessage msg = blockingReceive();
	    System.out.println("Cleaning-Floor answered: " + ACLMessage.getPerformative(msg.getPerformative()));
	    addBehaviour(new GiveBlockBehaviour());
	    /*
	    if (serviceType.equals(ServiceType.TAKE_BLOCK)) {
		addBehaviour(new TakeBlockBehaviour());
	    } else {
		addBehaviour(new GiveBlockBehaviour());
	    }
	    */

	}

	private void sendRequest() {
	    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
	    request.addReceiver(new AID("CleaningFloor", AID.ISLOCALNAME));
	    try {
		request.setContentObject(serviceType);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    send(request);
	}
    }

    private class GiveBlockBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = -9154056971967926813L;

	@Override
	public void action() {
	    Block b = new Block();
	    b.Status = Block.possibleBlockStatus.DIRTY;
	    ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
	    try {
		inform.setContentObject(b);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    inform.addReceiver(new AID("CleaningFloor", AID.ISLOCALNAME));
	    send(inform);
	    //addBehaviour(new RequestCleaningFloor(ServiceType.TAKE_BLOCK));
	}
    }

    private class TakeBlockBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = -9154056971967926813L;

	@Override
	public void action() {
	    ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
	    inform.addReceiver(new AID("CleaningFloor", AID.ISLOCALNAME));
	    send(inform);
	    ACLMessage replyWithBlock = blockingReceive();
	    try {
		block = (Block) replyWithBlock.getContentObject();
	    } catch (UnreadableException e) {
		e.printStackTrace();
	    }
	    System.out.println("Received block from cleaning-station:" + block.toString());
	}
    }
}
