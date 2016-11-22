package gseproject.infrastructure.contracts;

import java.io.Serializable;

import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;
import gseproject.core.grid.objects.GridObject;
import gseproject.passive.core.Floor;

public class FloorContract implements GridObject, Serializable {
	private static final long serialVersionUID = 5071392492833889864L;
	private int width;
	private int height;
	private Position position;
	private SpaceType tile;
	private Floor floor;

	public FloorContract(Position position, int width, int height, SpaceType tile, Floor floor) {
		this.width = width;
		this.height = height;
		this.position = position;
		this.tile = tile;
		this.floor = floor;
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
		return floor;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FloorContract other = (FloorContract) obj;
		if (floor == null) {
			if (other.floor != null)
				return false;
		} else if (!floor.equals(other.floor))
			return false;
		if (height != other.height)
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
