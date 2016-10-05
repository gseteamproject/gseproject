package gseproject.robot.domain;

import gseproject.infrastructure.BlockDto;

public class TransportBusinessObject implements ISkill{

    private int _cost;
    private int _duration;

    public TransportBusinessObject(int duration, int cost){
        _duration = duration;
        _cost = cost;
    }

    public int getCost() {
        return _cost;
    }

    public int getDuration() {
        return _duration;
    }
}
