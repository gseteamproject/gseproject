package gseproject.robot.skills;

import gseproject.core.interaction.IActuator;
import jade.core.Agent;

import java.util.Random;
import java.util.UUID;

public class TransportSkill implements ISkill {

    private int _cost;
    private int _duration;
    private final UUID _id = UUID.fromString("389f4a2b-b71e-4c3a-bfe9-b5b526035261");
    private IActuator _actuator;

    //todo: remove when became unnecessary
    public TransportSkill(IActuator actuator) {
        _actuator = actuator;
    }

    public void setCost(Integer cost) { _cost = cost;}

    public void setDuration(Integer duration) {
        _duration = duration;
    }

    public UUID getId() {
        return _id;
    }

    public int getCost() {
        return _cost;
    }

    public int getDuration() {
        return _duration;
    }

    public void registerService(Agent agent) {

    }

    public void deregisterService(Agent agent) {

    }

    public void doWork() {

    }

    @Override
    public String toString() {
        return "TransportSkill [_cost=" + _cost + ", _duration=" + _duration + "]";
    }
}
