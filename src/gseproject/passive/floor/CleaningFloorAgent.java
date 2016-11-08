package gseproject.passive.floor;

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

	    /**
	     * 
	     */
	    private static final long serialVersionUID = 1404030294876210389L;

	    @Override
	    public void action() {
		ACLMessage messageFromRobot = blockingReceive(requestAndInform());

	    }

	});

    }

    private void handleMessageFromRobot(ACLMessage msg) {
	if (msg.getPerformative() == ACLMessage.REQUEST) {
	    replyRequestMessage(msg);
	} else if (msg.getPerformative() == ACLMessage.INFORM) {
	    handleInformMessage(msg);
	}
    }

    private void replyRequestMessage(ACLMessage request) {

    }

    private void handleInformMessage(ACLMessage inform) {

    }
    


    @Override
    protected void takeDown() {
	// TODO Auto-generated method stub

    }

}
