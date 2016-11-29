package gseproject.robot.skills;

import gseproject.core.interaction.IActuator;
import jade.core.Agent;

import java.util.Random;
import java.util.UUID;

public class TransportSkill implements ISkill {

    private int _cost;
    private int _duration;

    //todo: remove when became unnecessary
    public TransportSkill() {
    }

    public void setCost(Integer cost) { _cost = cost;}

    public void setDuration(Integer duration) {
        _duration = duration;
    }

    public int getCost() {
        return _cost;
    }

    public int getDuration() {
        return _duration;
    }

    public void doWork() {

    }

    @Override
    public String toString() {
        return "TransportSkill [_cost=" + _cost + ", _duration=" + _duration + "]";
    }
}
