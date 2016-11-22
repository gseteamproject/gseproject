package gseproject.core.grid.objects;

import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;

public interface GridObject {

	public Position getPosition();
	public int getWidth();
	public int getHeight();
	public SpaceType getTile();
	public Object getState();
}
