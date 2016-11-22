package gseproject.passive;

import gseproject.passive.communicator.FloorCommunicator;
import gseproject.passive.communicator.ServiceTypeResponder;
import gseproject.passive.core.CleaningFloor;

public class CleaningFloorAgent extends FloorAgent {
    private static final long serialVersionUID = -7283341637605998716L;

    @Override
    protected void setup() {
	this.floor = new CleaningFloor();
	this.stationCommunicator = new FloorCommunicator(this.floor);
	this.addBehaviour(new ServiceTypeResponder(this, this.robotServiceRequestTemplate, stationCommunicator));
    }

    @Override
    protected void takeDown() {

    }

}
