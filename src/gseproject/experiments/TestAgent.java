package gseproject.experiments;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class TestAgent extends Agent {

    /**
     * 
     */
    private static final long serialVersionUID = 655794952650375591L;
    protected String serviceType;
    protected String serviceName;

    /**
     * CANNOT REGISTER SERVICE IN CONSTRUCTOR NEED TO BE DONE IN SETUP-ROUTINE
     */

    protected void setup() {
	registerService(serviceName, serviceType);
	MessageTemplate mt =

		AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
	addBehaviour(new AchieveREResponder(this, mt) {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = 657983227667430123L;

	    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
		System.out.println("I do my service for " + request.getSender().getLocalName());
		ACLMessage informDone = request.createReply();
		informDone.setPerformative(ACLMessage.INFORM);
		informDone.setContent("inform done");
		return informDone;
	    }
	});
    }

    protected void takedown() {

    }

    private void registerService(String serviceName, String type) {
	ServiceDescription serviceDescription = new ServiceDescription();
	serviceDescription.setName(serviceName);
	serviceDescription.setType(type);
	DFAgentDescription agentDescription = new DFAgentDescription();
	agentDescription.setName(getAID());
	agentDescription.addServices(serviceDescription);
	try {
	    DFService.register(this, agentDescription);
	} catch (FIPAException exception) {
	    exception.printStackTrace();
	}
    }

    private void deregisterService() {
	try {
	    DFService.deregister(this);
	} catch (FIPAException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
