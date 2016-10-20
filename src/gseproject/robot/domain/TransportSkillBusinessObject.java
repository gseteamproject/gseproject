package gseproject.robot.domain;

import java.util.Random;

public class TransportSkillBusinessObject implements ISkill, Comparable<TransportSkillBusinessObject> {

    public int _cost;
    public int _duration;

    public TransportSkillBusinessObject() {
	Random random = new Random();
	_cost = random.nextInt(1000 - 1 + 1) + 1;
	_duration = random.nextInt(1000 - 1 + 1) + 1;
    }

    public TransportSkillBusinessObject(int duration, int cost) {
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
    public int compareTo(TransportSkillBusinessObject o) {
	if ((this.getCost() * this.getDuration()) > (o.getCost() * o.getDuration())) {
	    return 1;
	} else if ((this.getCost() * this.getDuration()) > (o.getCost() * o.getDuration())) {
	    return -1;
	}
	return 0;
    }
    

    @Override
    public String toString() {
	return "TransportSkillBusinessObject [_cost=" + _cost + ", _duration=" + _duration + "]";
    }
}
