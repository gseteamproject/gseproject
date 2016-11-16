package gseproject.infrastructure.contracts;

import java.util.UUID;

public class RobotSkillContract implements Comparable<RobotSkillContract> {

    public int cost;
    public int duration;
    public UUID id;

    @Override
    public int compareTo(RobotSkillContract o) {
	int firstMetric = cost * duration;
	int secondMetric = o.cost * o.duration;

	if (firstMetric > secondMetric) {
	    return 1;
	} else if (firstMetric > secondMetric) {
	    return -1;
	}
	return 0;
    }

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
