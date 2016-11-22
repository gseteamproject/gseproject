package gseproject.passive;

import gseproject.passive.communicator.FloorCommunicator;
import gseproject.passive.communicator.ServiceTypeResponder;
import gseproject.passive.core.PaintingFloor;

public class PaintingFloorAgent extends FloorAgent {
    private static final long serialVersionUID = -1104113812018831544L;

    @Override
    protected void setup() {
	this.floor = new PaintingFloor();
	this.stationCommunicator = new FloorCommunicator(floor);
	this.addBehaviour(new ServiceTypeResponder(this, this.robotServiceRequestTemplate, this.stationCommunicator));
    }

    @Override
    protected void takeDown() {

    }

}
