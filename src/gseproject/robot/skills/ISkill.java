package gseproject.robot.skills;

import jade.core.Agent;

import java.util.UUID;

public interface ISkill {

    int getDuration();

    void setCost(Integer cost) ;

    void setDuration(Integer duration);

    //todo: Here should be instructions how to do current skill. Probably later we have to pass Callback into this method
    void doWork();
}
