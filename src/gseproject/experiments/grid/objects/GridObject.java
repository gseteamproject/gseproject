package gseproject.experiments.grid.objects;

import gseproject.experiments.grid.Position;
import gseproject.experiments.grid.Tile;

public interface GridObject {

	public Position getPosition();
	public int getWidth();
	public int getHeight();
	public Tile getTile();
}
