package gseproject.experiments.gui.testagents;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
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
	ParallelBehaviour par = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
	par.addSubBehaviour(new RequestBehaviour(this, 1000));
	par.addSubBehaviour(new ReceiveStateBehaviour());
    }

    protected void takeDown() {

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
	    ACLMessage positionRequest = new ACLMessage(ACLMessage.REQUEST);
	    positionRequest.setContent("state");
	    for(AID aid : getRobots()){
		positionRequest.addReceiver(aid);
	    }
	    send(positionRequest);
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

	@Override
	public void action() {
	    MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	    ACLMessage responseFromRobot = blockingReceive(mt);
	    if(responseFromRobot != null){
		//TODO: update from gui from here
	    }
	}
	
    }
}
