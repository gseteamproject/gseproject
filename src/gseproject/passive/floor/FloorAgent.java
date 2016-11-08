package gseproject.passive.floor;

import jade.core.Agent;

public abstract class FloorAgent extends Agent{

    /**
     * 
     */
    private static final long serialVersionUID = -5380485359391127163L;
    protected Floor floor;
    
    protected abstract void setup();
    
    protected abstract void takeDown();

    
}
