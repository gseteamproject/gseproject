package gseproject.passive.floor;

import gseproject.passive.communicator.IStationCommunicator;
import gseproject.passive.floor.core.Floor;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;

public abstract class FloorAgent extends Agent{
    private static final long serialVersionUID = -5380485359391127163L;
    protected Floor floor;
    protected MessageTemplate robotServiceRequestTemplate;
    protected IStationCommunicator stationCommunicator;
    
    public FloorAgent(){

    }
    
    protected abstract void setup();
    
    protected abstract void takeDown();
    
}
