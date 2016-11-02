package gseproject.experiments.gui.testagents;

import gseproject.SpaceType;
import gseproject.grid.GridSpace;
import gseproject.infrastructure.serialization.SerializationController;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TestAgent extends Agent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private void registerService() {
	DFAgentDescription dfd = new DFAgentDescription();
	ServiceDescription sd = new ServiceDescription();
	sd.setName("testagent");
	sd.setType("gui");
	dfd.addServices(sd);
	try {
	    DFService.register(this, dfd);
	} catch (FIPAException e) {
	    e.printStackTrace();
	}
    }

    protected void setup() {
	registerService();
	addBehaviour(new InformGUIBehaviour());
    }

    protected void takeDown() {

    }

    private class InformGUIBehaviour extends CyclicBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SerializationController sc = SerializationController.Instance;

	@Override
	public void action() {
	    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
	    mt = MessageTemplate.and(mt, MessageTemplate.MatchContent("gui"));
	    ACLMessage requestFromGUI = blockingReceive(mt);
	    ACLMessage reply = requestFromGUI.createReply();
	    reply.setPerformative(ACLMessage.INFORM);
	    reply.setContent(serializedGridSpace());
	    send(reply);
	}

	private String serializedGridSpace() {
	    return sc.Serialize(new GridSpace(3, 3, SpaceType.NO_TRACK));
	}

    }

}
