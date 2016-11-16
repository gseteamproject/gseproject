package gseproject.passive.floor;

import java.awt.Point;

import gseproject.passive.floor.core.Floor;
import jade.core.Agent;

public abstract class FloorAgent extends Agent{
    private static final long serialVersionUID = -5380485359391127163L;
    protected Floor floor;
    protected Point position;
    
    protected abstract void setup();
    
    protected abstract void takeDown();

    
}
