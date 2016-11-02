package gseproject.experiments.floor;

public class CleaningFloor extends Floor {

    public CleaningFloor() {
	super();
    }

    public CleaningFloor(FloorState floorState) {
	super(floorState);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -2510939849278803682L;

    @Override
    protected String trace(String msg) {
	return "[Cleaning-Floor] " + msg;
    }

}
