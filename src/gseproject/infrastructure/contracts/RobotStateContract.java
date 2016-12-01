package gseproject.infrastructure.contracts;

import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;
import gseproject.core.grid.objects.GridObject;

import java.io.Serializable;

public class RobotStateContract implements Serializable, GridObject {
	private static final long serialVersionUID = -6249435040366731123L;
	public boolean isCarryingBlock;
	public Position position;

	@Override
	public String toString() {
		return "RobotStateContract [isCarryingBlock=" + isCarryingBlock + ", position=" + position + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RobotStateContract other = (RobotStateContract) obj;
		if (isCarryingBlock != other.isCarryingBlock)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	//Not entirely sure if this makes sense
	@Override
	public int getWidth() {
		return position.getY();
	}

	@Override
	public int getHeight() {
		return position.getX();
	}

	@Override
	public SpaceType getTile() {
		return SpaceType.ROBOT;
	}

	@Override
	public Object getState() {
		return isCarryingBlock;
	}

}
