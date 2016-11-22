package gseproject.experiments.gui.testagents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gseproject.core.grid.Grid;
import gseproject.core.grid.SpaceType;
import gseproject.core.grid.Grid.GridBuilder;
import gseproject.infrastructure.contracts.GridContract;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class GUIAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid grid;
	private SerializationController sc = SerializationController.Instance;

	private static ACLMessage getRequestTemplate() {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.addReceiver(new AID("GridAgent", AID.ISLOCALNAME));
		return request;
	};

	private ACLMessage prepareBroadcast(ACLMessage msg) {
		List<AID> agents = getAgents();
		for (int i = 0; i < agents.size(); i++) {
			msg.addReceiver(agents.get(i));
		}
		return msg;
	}

	private List<AID> getAgents() {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("gui");
		dfd.addServices(sd);
		List<AID> result = new ArrayList<AID>();
		try {
			DFAgentDescription[] dfds = DFService.search(this, dfd);
			for (int i = 0; i < dfds.length; i++) {
				result.add(dfds[i].getName());
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	protected void setup() {
		System.out.println("GUIAgent started");
		//ACLMessage request = prepareBroadcast(getRequestTemplate());
		this.addBehaviour(new AchieveREInitiator(this, getRequestTemplate()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4614760831840986042L;

			protected void handleInform(ACLMessage inform) {
			}
		});
	}

	protected void takeDown() {
		System.out.println("GUIAgent down!");
	}

	
	public Grid getGrid() {
		return grid;
	}
}
