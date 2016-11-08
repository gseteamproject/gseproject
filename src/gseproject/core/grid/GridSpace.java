package gseproject.core.grid;

import java.io.Serializable;

import jade.core.AID;

public class GridSpace implements Serializable {
	private static final long serialVersionUID = -7955993637810412462L;
	private final SpaceType spacetype;
	private AID robot;
	
	public GridSpace(SpaceType spacetype, AID robot) {
		this.spacetype = spacetype;
		this.robot = robot;
	}
	
	public GridSpace(SpaceType spacetype) {
		this(spacetype, null);
	}
	
	public SpaceType getSpaceType() {
		return spacetype;
	}
	
	public void occupy(AID robot) {
		this.robot = robot;
	}
	
	public boolean isOccupied() {
		return robot == null;
	}
}
