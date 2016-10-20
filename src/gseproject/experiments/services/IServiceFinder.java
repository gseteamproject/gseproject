package gseproject.experiments.services;

import java.util.List;
import java.util.Map;

import gseproject.robot.domain.ISkill;
import gseproject.robot.domain.TransportSkillBusinessObject;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public interface IServiceFinder {
    public List<AID> searchAvailableAgent(Agent searcher, String serviceType);
    public void prepareMulticast(ACLMessage multicastMessage, List<AID> receivers);
}
