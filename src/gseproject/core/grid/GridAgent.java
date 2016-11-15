package gseproject.core.grid;

import gseproject.core.grid.communicator.GridCommunicator;
import gseproject.core.grid.communicator.IGridCommunicator;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class GridAgent extends Agent {
    private static final long serialVersionUID = -9064311405865477921L;
    private Grid grid;
    private IGridCommunicator gridCommunicator;

    private static MessageTemplate getMessageTemplate() {
	return AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
    }

    protected void setup() {
	/*
	 * grid = new GridBuilder(5, 5).setSpaceType(0, 0,
	 * SpaceType.TRACK).setSpaceType(1, 0, SpaceType.TRACK) .setSpaceType(2,
	 * 0, SpaceType.TRACK).setSpaceType(3, 0, SpaceType.TRACK)
	 * .setSpaceType(4, 0, SpaceType.TRACK).setSpaceType(4, 1,
	 * SpaceType.TRACK) .setSpaceType(4, 2, SpaceType.TRACK).setSpaceType(4,
	 * 3, SpaceType.TRACK) .setSpaceType(4, 4,
	 * SpaceType.TRACK).setSpaceType(3, 4, SpaceType.TRACK) .setSpaceType(2,
	 * 4, SpaceType.TRACK).setSpaceType(1, 4, SpaceType.TRACK)
	 * .setSpaceType(0, 4, SpaceType.TRACK).setSpaceType(0, 3,
	 * SpaceType.TRACK) .setSpaceType(0, 2, SpaceType.TRACK).setSpaceType(0,
	 * 1, SpaceType.TRACK).build();
	 */
	gridCommunicator = new GridCommunicator(this);
	addBehaviour(new RobotStateResponder(this, getMessageTemplate(), gridCommunicator));

    }

    protected void takeDown() {

    }
}
