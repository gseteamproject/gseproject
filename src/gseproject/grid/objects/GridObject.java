package gseproject.grid.objects;

import gseproject.grid.Position;
import gseproject.grid.SpaceType;

public interface GridObject {

	public Position getPosition();
	public int getWidth();
	public int getHeight();
	public SpaceType getTile();
	public Object getState();
}
