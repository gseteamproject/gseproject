package gseproject.infrastructure.contracts;

import java.io.Serializable;

import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;
import gseproject.core.grid.objects.GridObject;
import gseproject.passive.core.Palette;

public class PaletteContract implements GridObject, Serializable {
	private static final long serialVersionUID = 5718243044639208547L;
	private Position position;
	private int height, width;
	private SpaceType tile;
	private Palette palette;

	public PaletteContract(Position position, int height, int width, SpaceType tile, Palette palette) {
		this.position = position;
		this.height = height;
		this.width = width;
		this.tile = tile;
		this.palette = palette;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public SpaceType getTile() {
		return this.tile;
	}

	@Override
	public Object getState() {
		return this.palette;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaletteContract other = (PaletteContract) obj;
		if (height != other.height)
			return false;
		if (palette == null) {
			if (other.palette != null)
				return false;
		} else if (!palette.equals(other.palette))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (tile != other.tile)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

}
