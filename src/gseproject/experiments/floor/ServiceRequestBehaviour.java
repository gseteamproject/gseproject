package gseproject.experiments.floor;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import gseproject.experiments.services.IServiceFinder;
import gseproject.experiments.services.ServiceFinder;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.robot.domain.TransportSkillBusinessObject;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

public class ServiceRequestBehaviour extends OneShotBehaviour {
    /**
     * 
     */
    private static final long serialVersionUID = -4150939492197532715L;
    private FloorState floorState;

    public ServiceRequestBehaviour(FloorState floorState) {
	this.floorState = floorState;
    }

    @Override
    public void action() {
	IServiceFinder serviceFinder = new ServiceFinder();
	String serviceName = null;
	try {
	    serviceName = floorState.getNextServiceName();
	} catch (FloorException e) {
	    e.printStackTrace();
	}
	List<AID> availableAgents = serviceFinder.searchAvailableAgent(this.myAgent, serviceName);

	ACLMessage request = new ACLMessage(ACLMessage.CFP);
	request.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
	request.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
	request.setContent(serviceName);
	serviceFinder.prepareMulticast(request, availableAgents);

	myAgent.addBehaviour(new ContractNetInitiator(myAgent, request) {

	    /**
	     * 
	     */
	    private static final long serialVersionUID = 6840781574254796354L;
	    private final SerializationController serializationController = SerializationController.Instance;

	    protected void handlePropose(ACLMessage propose, Vector acceptances) {
		TransportSkillBusinessObject tSBO = serializationController
			.Deserialize(TransportSkillBusinessObject.class, propose.getContent());
		System.out.println(
			"Agent " + propose.getSender().getName() + " proposed " + "SERIALIZATION NOT IMPLEMENTED");
	    }

	    protected void handleRefuse(ACLMessage refuse) {
		System.out.println("Agent " + refuse.getSender().getName() + " refused");
	    }

	    protected void handleFailure(ACLMessage failure) {
		System.out.println("Agent " + failure.getSender().getName() + " communication failed.");
	    }

	    protected void handleAllResponses(Vector responses, Vector acceptances) {
		SerializationController serializationController = SerializationController.Instance;
		AID bestProposer = null;
		ACLMessage accept = null;
		TransportSkillBusinessObject bestProposal = null;
		
		System.out.println("Count responders: " + responses.size());
		Enumeration e = responses.elements();
		if (e.hasMoreElements()) {
		    ACLMessage firstMsg = (ACLMessage) e.nextElement();
		    bestProposal = serializationController.Deserialize(TransportSkillBusinessObject.class,
			    firstMsg.getContent());
		}

		while (e.hasMoreElements()) {
		    ACLMessage msg = (ACLMessage) e.nextElement();
		    if (msg.getPerformative() == ACLMessage.PROPOSE) {
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
			acceptances.addElement(reply);
			/*
			TransportSkillBusinessObject proposal = serializationController
				.Deserialize(TransportSkillBusinessObject.class, msg.getContent());
			if (proposal.compareTo(bestProposal) == 1) {
			    bestProposal = proposal;
			    bestProposer = msg.getSender();
			    accept = reply;
			}*/
			accept = reply;
			bestProposer = msg.getSender();
		    }
		}

		if (accept != null) {
		    /*
		    System.out.println(
			    "Accepting proposal " + bestProposal + " from responder " + bestProposer.getName());
			    */
		    System.out.println("Accepting proposal from responder " + bestProposer.getName() + "\n");
		    accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
		}

	    }

	    protected void handleInform(ACLMessage inform) {
		System.out.println(
			"Agent " + inform.getSender().getName() + " successfully performed the requested action");
	    }

	});
    }

}
