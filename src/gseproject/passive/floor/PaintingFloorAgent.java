package gseproject.passive.floor;

import gseproject.passive.floor.communicator.FloorBehaviour;
import gseproject.passive.floor.core.PaintingFloor;

public class PaintingFloorAgent extends FloorAgent {
    private static final long serialVersionUID = -1104113812018831544L;

    @Override
    protected void setup() {
	this.floor = new PaintingFloor();
	this.addBehaviour(new FloorBehaviour(this, floor));
    }

    @Override
    protected void takeDown() {

    }

}
