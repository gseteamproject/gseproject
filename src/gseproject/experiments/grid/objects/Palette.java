package gseproject.experiments.grid.objects;

import gseproject.experiments.grid.Position;
import gseproject.experiments.grid.Tile;

public abstract class Palette implements GridObject {
	
	private Position position;
	private int width;
	private int height;
	private Tile tile;
	
	public Palette(Position position, int width, int height, Tile tile) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.tile = tile;
	}
	
	@Override
	public Position getPosition() {
		return position;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public Tile getTile() {
		return tile;
	}
}
