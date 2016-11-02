package gseproject.experiments.floor;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class FloorWorkflowBehaviour extends TickerBehaviour {
    /**
     * 
     */
    private static final long serialVersionUID = 843233242984948009L;
    private FloorState floorState;

    public FloorWorkflowBehaviour(Agent a, long period, FloorState floorState) {
	super(a, period);
	this.floorState = floorState;
    }

    @Override
    protected void onTick() {
	try {
	    System.out.println("need service:" + floorState.getNextServiceName());
	} catch (FloorException e) {
	    e.printStackTrace();
	}
	myAgent.addBehaviour(new ServiceRequestBehaviour(floorState));
    }
}
