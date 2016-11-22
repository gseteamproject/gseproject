package gseproject.infrastructure.contracts;

import java.io.Serializable;
import java.util.UUID;

public class RobotSkillContract implements Serializable {
	private static final long serialVersionUID = 94405095622337174L;
	public int cost;
	public int duration;
	public UUID id;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RobotSkillContract other = (RobotSkillContract) obj;
		if (cost != other.cost)
			return false;
		if (duration != other.duration)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
