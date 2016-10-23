package gseproject.experiments.services;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class ServiceFinder implements IServiceFinder {
    
    @Override
    public List<AID> searchAvailableAgent(Agent searcher, String serviceType) {
	DFAgentDescription template = new DFAgentDescription();
	ServiceDescription sd = new ServiceDescription();
	sd.setType(serviceType);
	template.addServices(sd);
	List<AID> availableAgents = new ArrayList<>();
	try {
	    DFAgentDescription[] result = DFService.search(searcher, template);
	    for (int i = 0; i < result.length; ++i) {
		availableAgents.add(result[i].getName());
	    }
	} catch (FIPAException fe) {
	    fe.printStackTrace();
	}
	return availableAgents;
    }

    @Override
    public void prepareMulticast(ACLMessage msg, List<AID> receivers) {
	for(AID receiver : receivers){
	    msg.addReceiver(receiver);
	}
    }
}
