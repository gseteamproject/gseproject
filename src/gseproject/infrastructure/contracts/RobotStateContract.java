package gseproject.infrastructure.contracts;

import gseproject.core.grid.Position;

import java.io.Serializable;

public class RobotStateContract implements Serializable {
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

}
