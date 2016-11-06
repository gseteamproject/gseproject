package gseproject.infrastructure.contracts;

import java.util.UUID;

public class RobotSkillContract implements IContract, Comparable<RobotSkillContract> {

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
}
