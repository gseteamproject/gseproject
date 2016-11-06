package gseproject.robot.skills;

import jade.core.Agent;

import java.util.UUID;

public interface ISkill {

    UUID getId();

    int getCost();

    int getDuration();

    void registerService(Agent agent);

    void deregisterService(Agent agent);

    //todo: Here should be instructions how to do current skill. Probably later we have to pass Callback into this method
    void doWork();
}
