package gseproject.experiments.services;

import jade.core.AID;
import jade.core.Agent;

public interface IServiceFinder {
    public AID[] searchAvailableRobots (Agent searcher, String serviceType);
    public AID bestRobot(AID[] robots);
}
