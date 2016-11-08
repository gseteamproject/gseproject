package gseproject.passive.floor;

import jade.core.Agent;

public abstract class FloorCommunicator extends Agent{

    /**
     * 
     */
    private static final long serialVersionUID = -5380485359391127163L;
    private Floor floor;
    
    protected abstract void setup();
    
    protected abstract void takeDown();

    
}
