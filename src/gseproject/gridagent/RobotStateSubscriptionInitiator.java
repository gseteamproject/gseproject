package gseproject.gridagent;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class RobotStateSubscriptionInitiator extends SubscriptionInitiator {
    private static final long serialVersionUID = 6676343847002333272L;

    public RobotStateSubscriptionInitiator(Agent a, ACLMessage msg) {
	super(a, msg);
    }

    protected void handleAgree(ACLMessage agree) {

    }

    protected void handleRefuse(ACLMessage refuse) {

    }

    protected void handleInform(ACLMessage inform) {

    }

    protected void handleAllResponses(Vector responses) {

    }

}
