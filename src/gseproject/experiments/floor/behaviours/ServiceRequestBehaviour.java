package gseproject.experiments.floor.behaviours;

import gseproject.experiments.floor.FloorException;
import gseproject.experiments.services.IServiceFinder;
import gseproject.experiments.services.ServiceFinder;
import gseproject.experiments.floor.FloorState;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

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
	AID bestRobot = null;
	try {
	    bestRobot = serviceFinder
		    .bestRobot(serviceFinder.searchAvailableRobots(myAgent, floorState.getNextServiceName()));
	} catch (FloorException e) {
	    e.printStackTrace();
	}
	doRequest(bestRobot);
    }

    private void doRequest(AID receiver) {
	if (receiver != null) {
	    ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
	    request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	    request.addReceiver(new AID(receiver.getLocalName(), AID.ISLOCALNAME));
	    myAgent.addBehaviour(new AchieveREInitiator(myAgent, request) {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7676777550031980354L;

		protected void handleInform(ACLMessage inform) {
		    System.out.println(inform.getSender().getLocalName() + " does the service.\n");
		    /**
		     * critical section
		     */
		    floorState.updateState();
		    /**
		     * end of critical section
		     */
		    done();
		}

		protected void handleRefuse(ACLMessage refuse) {
		    System.out.println(refuse.getSender() + " refused to bring me the block.");
		}
	    });
	}
    }

}
