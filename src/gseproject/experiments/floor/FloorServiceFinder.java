package gseproject.experiments.floor;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class FloorServiceFinder implements IServiceFinder {

    @Override
    public AID[] searchAvailableRobots(Agent a, String serviceType) {
	DFAgentDescription template = new DFAgentDescription();
	ServiceDescription sd = new ServiceDescription();
	sd.setType(serviceType);
	template.addServices(sd);
	AID[] availableRobots = null;
	try {
	    DFAgentDescription[] result = DFService.search(a, template);
	    availableRobots = new AID[result.length];
	    for (int i = 0; i < result.length; ++i) {
		availableRobots[i] = result[i].getName();
	    }
	} catch (FIPAException fe) {
	    fe.printStackTrace();
	}
	return availableRobots;
    }

    @Override
    public AID bestRobot(AID[] robots) {
	if (robots.length < 1) {
	    System.out.println("no available robots");
	    return null;
	} else {
	    //TODO: decision making
	    return robots[0];
	}
    }
}
