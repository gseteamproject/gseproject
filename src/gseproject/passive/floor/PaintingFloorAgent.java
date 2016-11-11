package gseproject.passive.floor;

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
