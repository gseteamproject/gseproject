package gseproject.infrastructure.contracts;

import gseproject.core.Direction;
import gseproject.core.grid.Position;

public class RobotStateContract {

	public boolean isCarryingBlock;
	public Position position;
	public Position goal;
	public Direction direction;

	@Override
	public String toString() {
		return "RobotStateContract [isCarryingBlock=" + isCarryingBlock + ", position=" + position + ", goal=" + goal
				+ ", direction=" + direction + "]";
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
		if (direction != other.direction)
			return false;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
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
