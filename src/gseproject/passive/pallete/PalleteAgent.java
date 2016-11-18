package gseproject.passive.pallete;

import gseproject.passive.communicator.IStationCommunicator;
import gseproject.passive.pallete.core.Pallete;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;

public abstract class PalleteAgent extends Agent{
    private static final long serialVersionUID = -5380485359391127163L;
    protected Pallete pallete;
    protected MessageTemplate robotServiceRequestTemplate;
    protected IStationCommunicator stationCommunicator;
    
    public PalleteAgent(){

    }
    
    protected abstract void setup();
    
    protected abstract void takeDown();

    
}
