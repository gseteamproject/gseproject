package gseproject.core.grid;

import java.io.Serializable;
import jade.core.AID;

public class GridSpace implements Serializable {
	private static final long serialVersionUID = -7955993637810412462L;
	private final SpaceType tile;
	private AID robot;
	
	public GridSpace(SpaceType tile, AID robot) {
		this.tile  = tile;
		this.robot = robot;
	}
	
	public GridSpace(SpaceType spacetype) {
		this(spacetype, null);
	}
	
	public SpaceType getTile() {
		return tile;
	}
	
	public void occupy(AID robot) {
		this.robot = robot;
	}
	
	public boolean isOccupied() {
		return robot == null;
	}
}