package gseproject.grid;

import java.util.*;

import gseproject.Direction;
import gseproject.IGridSpace;
import gseproject.SpaceType;

public class GridSpace implements IGridSpace {
	
	private int xCoordinate;
	private int yCoordinate;
	private SpaceType description;
	private List<GridSpace> succesors;
	
	
	public GridSpace(int x, int y, SpaceType description) {
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.description = description;
		this.succesors =  new ArrayList<GridSpace> (2);
	}
	public void setSuccesor(GridSpace succesor) {
		
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
	public List<GridSpace> getSuccesors() {
		return this.succesors;
	}

	public void setSpaceType() {
		
	}
}
