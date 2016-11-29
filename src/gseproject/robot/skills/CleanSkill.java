package gseproject.robot.skills;

public class CleanSkill implements ISkill {

    private int _cost;
    private int _duration;

    //todo: remove when became unnecessarys
    public CleanSkill() {}

    @Override
    public int getCost() {
        return _cost;
    }
    
    @Override
    public int getDuration() {
        return _duration;
    }

    @Override
    public void setCost(Integer cost) { _cost = cost;
    }

    @Override
    public void setDuration(Integer duration) {
        _duration = duration;
    }

    @Override
    public String toString() {
        return "CleanSkill [_cost=" + _cost + ", _duration=" + _duration + "]";
    }
}
