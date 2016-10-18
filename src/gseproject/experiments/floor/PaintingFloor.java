package gseproject.experiments.floor;

public class PaintingFloor extends Floor {

    /**
     * 
     */
    private static final long serialVersionUID = -5047592003566880421L;
    
    public PaintingFloor() {
	super();
    }

    public PaintingFloor(FloorState floorState) {
	super(floorState);
    }

    @Override
    protected String trace(String msg) {
	return "[Painting-Floor] " + msg;
    }

}
