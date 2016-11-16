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
import gseproject.infrastructure.serialization.grid.GridReader;
import gseproject.infrastructure.serialization.grid.GridWriter;
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
	
	private Grid initGrid() {
		return new GridBuilder(20, 11)
				// source palettes
				.setSpaceType(0, 0, SpaceType.PALETTE_SOURCE).setSpaceType(1, 0, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 1, SpaceType.PALETTE_SOURCE).setSpaceType(1, 1, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 2, SpaceType.PALETTE_SOURCE).setSpaceType(1, 2, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 4, SpaceType.PALETTE_SOURCE).setSpaceType(1, 4, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 5, SpaceType.PALETTE_SOURCE).setSpaceType(1, 5, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 6, SpaceType.PALETTE_SOURCE).setSpaceType(1, 6, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 8, SpaceType.PALETTE_SOURCE).setSpaceType(1, 8, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 9, SpaceType.PALETTE_SOURCE).setSpaceType(1, 9, SpaceType.PALETTE_SOURCE)
				.setSpaceType(0, 10, SpaceType.PALETTE_SOURCE).setSpaceType(1, 10, SpaceType.PALETTE_SOURCE)
				// goal palettes
				.setSpaceType(18, 0, SpaceType.PALETTE_GOAL).setSpaceType(19, 0, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 1, SpaceType.PALETTE_GOAL).setSpaceType(19, 1, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 2, SpaceType.PALETTE_GOAL).setSpaceType(19, 2, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 4, SpaceType.PALETTE_GOAL).setSpaceType(19, 4, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 5, SpaceType.PALETTE_GOAL).setSpaceType(19, 5, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 6, SpaceType.PALETTE_GOAL).setSpaceType(19, 6, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 8, SpaceType.PALETTE_GOAL).setSpaceType(19, 8, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 9, SpaceType.PALETTE_GOAL).setSpaceType(19, 9, SpaceType.PALETTE_GOAL)
				.setSpaceType(18, 10, SpaceType.PALETTE_GOAL).setSpaceType(19, 10, SpaceType.PALETTE_GOAL)
				// main track
				.setSpaceType(3, 0, SpaceType.TRACK).setSpaceType(4, 0, SpaceType.TRACK)
				.setSpaceType(5, 0, SpaceType.TRACK).setSpaceType(6, 0, SpaceType.TRACK)
				.setSpaceType(7, 0, SpaceType.TRACK).setSpaceType(8, 0, SpaceType.TRACK)
				.setSpaceType(9, 0, SpaceType.TRACK).setSpaceType(10, 0, SpaceType.TRACK)
				.setSpaceType(11, 0, SpaceType.TRACK).setSpaceType(12, 0, SpaceType.TRACK)
				.setSpaceType(13, 0, SpaceType.TRACK).setSpaceType(14, 0, SpaceType.TRACK)
				.setSpaceType(15, 0, SpaceType.TRACK).setSpaceType(3, 10, SpaceType.TRACK)
				.setSpaceType(4, 10, SpaceType.TRACK).setSpaceType(5, 10, SpaceType.TRACK)
				.setSpaceType(6, 10, SpaceType.TRACK).setSpaceType(7, 10, SpaceType.TRACK)
				.setSpaceType(8, 10, SpaceType.TRACK).setSpaceType(9, 10, SpaceType.TRACK)
				.setSpaceType(10, 10, SpaceType.TRACK).setSpaceType(11, 10, SpaceType.TRACK)
				.setSpaceType(12, 10, SpaceType.TRACK).setSpaceType(13, 10, SpaceType.TRACK)
				.setSpaceType(14, 10, SpaceType.TRACK).setSpaceType(15, 10, SpaceType.TRACK)
				.setSpaceType(3, 1, SpaceType.TRACK).setSpaceType(3, 2, SpaceType.TRACK)
				.setSpaceType(3, 3, SpaceType.TRACK).setSpaceType(3, 4, SpaceType.TRACK)
				.setSpaceType(3, 5, SpaceType.TRACK).setSpaceType(3, 6, SpaceType.TRACK)
				.setSpaceType(3, 7, SpaceType.TRACK).setSpaceType(3, 8, SpaceType.TRACK)
				.setSpaceType(3, 9, SpaceType.TRACK).setSpaceType(3, 10, SpaceType.TRACK)
				.setSpaceType(16, 0, SpaceType.TRACK).setSpaceType(16, 1, SpaceType.TRACK)
				.setSpaceType(16, 2, SpaceType.TRACK).setSpaceType(16, 3, SpaceType.TRACK)
				.setSpaceType(16, 4, SpaceType.TRACK).setSpaceType(16, 5, SpaceType.TRACK)
				.setSpaceType(16, 6, SpaceType.TRACK).setSpaceType(16, 7, SpaceType.TRACK)
				.setSpaceType(16, 8, SpaceType.TRACK).setSpaceType(16, 9, SpaceType.TRACK)
				.setSpaceType(16, 10, SpaceType.TRACK)
				// cleaning station
				.setSpaceType(7, 0, SpaceType.CLEANING_STATION_TRANSPORT).setSpaceType(7, 1, SpaceType.CLEANING_STATION)
				.setSpaceType(7, 2, SpaceType.CLEANING_STATION_WORKSPACE).setSpaceType(6, 1, SpaceType.TRACK)
				.setSpaceType(6, 2, SpaceType.TRACK).setSpaceType(8, 1, SpaceType.TRACK)
				.setSpaceType(8, 2, SpaceType.TRACK)
				// painting station
				.setSpaceType(12, 0, SpaceType.PAINTING_STATION_TRANSPORT)
				.setSpaceType(12, 1, SpaceType.PAINTING_STATION)
				.setSpaceType(12, 2, SpaceType.PAINTING_STATION_WORKSPACE).setSpaceType(11, 1, SpaceType.TRACK)
				.setSpaceType(11, 2, SpaceType.TRACK).setSpaceType(13, 1, SpaceType.TRACK)
				.setSpaceType(13, 2, SpaceType.TRACK)
				// finally...
				.build();

	}

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
	
	private void initiateSerialization() {
        GridWriter writer = new GridWriter();
        GridReader reader = new GridReader();

        sc.RegisterSerializator(GridContract.class, writer, reader);
    }
	
	protected void setup() {
		initiateSerialization();
		System.out.println("GUIAgent started");
		//ACLMessage request = prepareBroadcast(getRequestTemplate());
		this.addBehaviour(new AchieveREInitiator(this, getRequestTemplate()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -4614760831840986042L;

			protected void handleInform(ACLMessage inform) {
				GridContract gc = (GridContract) sc.Deserialize(GridContract.class, inform.getContent());
				grid = gc.getGrid();
				System.out.println(Arrays.deepToString(gc.getGrid().getSpaces()));
				GridContract expected = new GridContract(initGrid());
				System.out.println(Arrays.deepToString(gc.getGrid().getSpaces()).equals(Arrays.deepToString(expected.getGrid().getSpaces())));
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
