package gseproject.robot.domain;

public class TransportSkillBusinessObject implements ISkill{

    private int _cost;
    private int _duration;

    public TransportSkillBusinessObject(int duration, int cost){
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
