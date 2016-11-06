package gseproject.grid;

import java.util.List;

public interface IGridSpace {
	
	public int getXCoordinate();
	public int getYCoordinate();
	public SpaceType getSpaceType();
	public List<GridSpace> getSuccesors();
}
