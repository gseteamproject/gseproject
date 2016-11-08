package gseproject.passive.floor;

public class PaintingFloorAgent extends FloorAgent{

    /**
     * 
     */
    private static final long serialVersionUID = -1104113812018831544L;

    @Override
    protected void setup() {
	this.floor = new PaintingFloor();
	
    }

    @Override
    protected void takeDown() {
	// TODO Auto-generated method stub
	
    }

}
