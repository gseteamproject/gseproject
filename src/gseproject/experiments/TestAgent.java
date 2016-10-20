package gseproject.experiments;

import java.awt.List;
import java.util.ArrayList;

import org.omg.CORBA.Any;

import gseproject.infrastructure.serialization.SerializationController;
import gseproject.robot.domain.TransportSkillBusinessObject;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetResponder;

public class TestAgent extends Agent {

    /**
     * 
     */
    private static final long serialVersionUID = 655794952650375591L;
    protected String serviceType;
    protected String serviceName;
    private TransportSkillBusinessObject transportSkillBO;
    private final SerializationController serializationController = SerializationController.Instance;

    public TestAgent() {
	this.transportSkillBO = new TransportSkillBusinessObject();
    }

    /**
     * CANNOT REGISTER SERVICE IN CONSTRUCTOR NEED TO BE DONE IN SETUP-ROUTINE
     */

    protected void setup() {
	registerService(serviceName, serviceType);
	MessageTemplate template = MessageTemplate.and(
		MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
		MessageTemplate.MatchPerformative(ACLMessage.CFP));

	addBehaviour(new ContractNetResponder(this, template) {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = 657983227667430123L;

	    protected ACLMessage prepareResponse(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
		System.out.println("Agent " + getLocalName() + ": CFP received from " + cfp.getSender().getName()
			+ ". Action is " + cfp.getContent());
		String proposal = serializationController.Serialize(transportSkillBO);
		System.out.println("Agent " + getLocalName() + ": Proposing " + transportSkillBO.toString());
		ACLMessage propose = cfp.createReply();
		propose.setPerformative(ACLMessage.PROPOSE);
		propose.setContent(proposal);
		return propose;
	    }

	    protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
		    throws FailureException {
		System.out.println("Agent " + getLocalName() + ": Proposal accepted");
		System.out.println("Agent " + getLocalName() + ": Action successfully performed");
		ACLMessage inform = accept.createReply();
		inform.setPerformative(ACLMessage.INFORM);
		return inform;
	    }

	    protected void handleRejectProposal(ACLMessage reject) {
		System.out.println("Agent " + getLocalName() + ": Proposal rejected");
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
	    e.printStackTrace();
	}

    }
}
