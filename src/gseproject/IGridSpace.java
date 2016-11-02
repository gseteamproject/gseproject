package gseproject;

import java.util.List;
import gseproject.grid.*;

public interface IGridSpace {
	
	public int getXCoordinate();
	public int getYCoordinate();
	public SpaceType getSpaceType();
	public List<GridSpace> getSuccesors();
}
