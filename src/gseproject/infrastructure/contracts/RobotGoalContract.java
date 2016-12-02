package gseproject.infrastructure.contracts;

import jade.core.AID;

import java.io.Serializable;

import gseproject.grid.Position;

public class RobotGoalContract implements Serializable {
	private static final long serialVersionUID = 8120601045156551769L;

	public AID aid;

	public Position goal;

	public Position currentPosition;

	public RobotGoalContract() {
	}

	public RobotGoalContract(AID aid, Position currentPosition, Position goal) {
		this.aid = aid;
		this.goal = goal;
		this.currentPosition = currentPosition;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RobotGoalContract other = (RobotGoalContract) obj;
		if (aid == null) {
			if (other.aid != null)
				return false;
		} else if (!aid.equals(other.aid))
			return false;
		if (currentPosition == null) {
			if (other.currentPosition != null)
				return false;
		} else if (!currentPosition.equals(other.currentPosition))
			return false;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
		return true;
	}

}
