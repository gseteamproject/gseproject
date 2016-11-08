package gseproject.passive.floor;

import java.awt.Point;
import java.io.IOException;

import gseproject.core.ServiceType;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CleaningFloorAgent extends FloorAgent {

    /**
     * 
     */
    private static final long serialVersionUID = -7283341637605998716L;
    private Point position;
    
    @Override
    protected void setup() {
	this.floor = new CleaningFloor();
	this.addBehaviour(new FloorBehaviour(this, floor));
    }

    @Override
    protected void takeDown() {
	
    }
   

}
