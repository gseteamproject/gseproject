package gseproject.experiments.floor;

import gseproject.experiments.floor.behaviours.FloorWorkflowBehaviour;
import jade.core.Agent;

public abstract class Floor extends Agent {

    /**
     * 
     */
    private static final long serialVersionUID = -4729244722766418856L;

    protected abstract String trace(String msg);

    private FloorState floorState;

    public Floor(FloorState floorState) {
	this.floorState = floorState;
    }

    public Floor() {
	this.floorState = new FloorState();
    }

    private void initializeBehaviour() {
	addBehaviour(new FloorWorkflowBehaviour(this, 10000, this.floorState));
    }

    protected void setup() {
	trace("Initialized");
	initializeBehaviour();
    }

    protected void takedown() {
	trace("Shut down");
    }

}
