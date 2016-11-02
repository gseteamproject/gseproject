package gseproject.grid;

import java.util.*;

public class GridSpace implements IGridSpace {

	private int xCoordinate;
	private int yCoordinate;
	private SpaceType description;
	private List<Direction> directions;

	public GridSpace(int x, int y, SpaceType description, ArrayList<Direction> directions) {
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.description = description;
		this.directions = directions;
	}

	@Override
	public int getXCoordinate() {
		return this.xCoordinate;
	}

	@Override
	public int getYCoordinate() {
		return this.yCoordinate;
	}

	@Override
	public SpaceType getSpaceType() {
		return this.description;
	}

	@Override
	public List<Direction> getDirections() {
		return this.directions;
	}

	public void setSpaceType(SpaceType description) {
		this.description = description;
	}

}
