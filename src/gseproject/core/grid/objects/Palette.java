package gseproject.core.grid.objects;

import java.io.Serializable;

import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;
import gseproject.core.interaction.IState;

public abstract class Palette implements GridObject, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5928346278918584657L;
	private Position position;
	private int width;
	private int height;
	private SpaceType tile;
	
	public Palette(Position position, int width, int height, SpaceType tile) {
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
	public SpaceType getTile() {
		return tile;
	}
	
	@Override
	public IState getState() {
		return null;
	}
}
