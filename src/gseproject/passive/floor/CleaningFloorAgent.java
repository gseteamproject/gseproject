package gseproject.passive.floor;

public class CleaningFloorAgent extends FloorAgent {
    private static final long serialVersionUID = -7283341637605998716L;

    @Override
    protected void setup() {
	this.floor = new CleaningFloor();
	this.addBehaviour(new FloorBehaviour(this, floor));
    }

    @Override
    protected void takeDown() {

    }

}
