package gseproject.robot.skills;

public interface ISkill {

    int getDuration();
    
    int getCost();

    void setCost(Integer cost) ;

    void setDuration(Integer duration);
}
