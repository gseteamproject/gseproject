package gseproject.robot.skills;

import java.util.Random;

public class TransportSkill implements ISkill, Comparable<TransportSkill> {

    private int _cost;
    private int _duration;

    public TransportSkill() {
        Random random = new Random();
        _cost = random.nextInt(1000 - 1 + 1) + 1;
        _duration = random.nextInt(1000 - 1 + 1) + 1;
    }

    public TransportSkill(int duration, int cost) {
        _duration = duration;
        _cost = cost;
    }

    public int getCost() {
        return _cost;
    }

    public int getDuration() {
        return _duration;
    }

    @Override
    public int compareTo(TransportSkill o) {
        if ((this.getCost() * this.getDuration()) > (o.getCost() * o.getDuration())) {
            return 1;
        } else if ((this.getCost() * this.getDuration()) > (o.getCost() * o.getDuration())) {
            return -1;
        }
        return 0;
    }


    @Override
    public String toString() {
        return "TransportSkill [_cost=" + _cost + ", _duration=" + _duration + "]";
    }
}
