package gseproject.robot.communicator;

import java.util.Vector;

import gseproject.infrastructure.subscriptions.RobotSubscriptionServices;
import gseproject.robot.RobotAgent;
import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;

public class RobotStateSubscriptionResponder extends SubscriptionResponder {

    private static final long serialVersionUID = 5702868272785871637L;

    // <editor-fold desc="Private constructors">
    private RobotStateSubscriptionResponder(Agent a, MessageTemplate mt) {
	super(a, mt);
    }

    private RobotStateSubscriptionResponder(Agent a, MessageTemplate mt, SubscriptionResponder.SubscriptionManager sm) {
	super(a, mt, sm);
    }

    private RobotStateSubscriptionResponder(Agent a, MessageTemplate mt, SubscriptionResponder.SubscriptionManager sm,
	    DataStore store) {
	super(a, mt, sm, store);
    }
    // </editor-fold>

    public RobotStateSubscriptionResponder(RobotAgent robot) {
	super(robot, MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE),
		MessageTemplate.MatchContent(RobotSubscriptionServices.Status)));
    }

    public ACLMessage handleSubscription(ACLMessage subscription_msg) {
	createSubscription(subscription_msg);
	ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
	return msg;
    }

    public void notify(ACLMessage inform) {
	Vector<SubscriptionResponder.Subscription> subscriptions = getSubscriptions();
	for (SubscriptionResponder.Subscription subscription : subscriptions)
	    subscription.notify(inform);
    }
}
