package gseproject.experiments.gui.testagents;

import java.util.ArrayList;
import java.util.List;

import gseproject.core.grid.GridSpace;
import gseproject.infrastructure.serialization.SerializationController;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GUIAgent extends Agent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected void setup() {
	System.out.println("GUIAgent started");
	addBehaviour(new RequestBehaviour(this, 1000));
	addBehaviour(new ReceiveStateBehaviour());
    }

    protected void takeDown() {
	System.out.println("GUIAgent down!");
    }

    private class RequestBehaviour extends TickerBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestBehaviour(Agent a, long period) {
	    super(a, period);
	}

	@Override
	protected void onTick() {
	    System.out.println("tick");
	    ACLMessage positionRequest = new ACLMessage(ACLMessage.REQUEST);
	    positionRequest.setContent("gui");
	    List<AID> robots = getRobots();
	    if (robots.size() < 1) {
		System.out.println("no robots");
	    } else {
		for (AID aid : robots) {
		    positionRequest.addReceiver(aid);
		}
		System.out.println("robots found");
		send(positionRequest);
	    }

	}

	private List<AID> getRobots() {
	    DFAgentDescription dfd = new DFAgentDescription();
	    ServiceDescription sd = new ServiceDescription();
	    sd.setType("gui");
	    dfd.addServices(sd);
	    List<AID> result = new ArrayList<AID>();
	    try {
		DFAgentDescription[] dfds = DFService.search(this.myAgent, dfd);
		for (int i = 0; i < dfds.length; i++) {
		    result.add(dfds[i].getName());
		}
	    } catch (FIPAException e) {
		e.printStackTrace();
	    }
	    return result;
	}

    }

    private class ReceiveStateBehaviour extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SerializationController sc = SerializationController.Instance;

	@Override
	public void action() {
	    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	    ACLMessage responseFromRobot = blockingReceive(mt);
	    if (responseFromRobot != null) {
		GridSpace gs = sc.Deserialize(GridSpace.class, responseFromRobot.getContent());
		if (gs != null) {
		    System.out.println(gs.getXCoordinate());
		    System.out.println(gs.getYCoordinate());
		    System.out.println(gs.getSpaceType());
		} else {
		    System.out.println("Serialization failed.");
		}

	    }
	}

    }
}
