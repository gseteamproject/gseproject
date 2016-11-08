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

public class TestAgentClean extends Agent {
    private static final long serialVersionUID = 3931523208882877185L;

    protected void setup() {
	addBehaviour(new RequestCleaningFloor());
    }

    protected void takeDown() {

    }

    private class RequestCleaningFloor extends OneShotBehaviour {
	private static final long serialVersionUID = 4747294158433105696L;

	@Override
	public void action() {
	    sendRequest();
	    ACLMessage msg = blockingReceive();
	    System.out.println("Cleaning-Floor answered: " + ACLMessage.getPerformative(msg.getPerformative()));
	    addBehaviour(new GiveBlockBehaviour());
	}

	private void sendRequest() {
	    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
	    request.addReceiver(new AID("CleaningFloor", AID.ISLOCALNAME));
	    ServiceType serviceType = ServiceType.GIVE_BLOCK_DIRTY;
	    try {
		request.setContentObject(serviceType);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    send(request);
	}
    }
    
    private class GiveBlockBehaviour extends Behaviour {
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
	}

	@Override
	public boolean done() {
	    return true;
	}
	
    }
}
