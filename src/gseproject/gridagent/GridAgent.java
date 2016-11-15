package gseproject.gridagent;

import gseproject.core.grid.Grid;
import gseproject.core.grid.Grid.GridBuilder;
import gseproject.core.grid.SpaceType;
import jade.core.Agent;

public class GridAgent extends Agent {
    private static final long serialVersionUID = -9064311405865477921L;
    private Grid grid;

    protected void setup() {
	grid = new GridBuilder(5, 5).setSpaceType(0, 0, SpaceType.TRACK).setSpaceType(1, 0, SpaceType.TRACK)
		.setSpaceType(2, 0, SpaceType.TRACK).setSpaceType(3, 0, SpaceType.TRACK)
		.setSpaceType(4, 0, SpaceType.TRACK).setSpaceType(4, 1, SpaceType.TRACK)
		.setSpaceType(4, 2, SpaceType.TRACK).setSpaceType(4, 3, SpaceType.TRACK)
		.setSpaceType(4, 4, SpaceType.TRACK).setSpaceType(3, 4, SpaceType.TRACK)
		.setSpaceType(2, 4, SpaceType.TRACK).setSpaceType(1, 4, SpaceType.TRACK)
		.setSpaceType(0, 4, SpaceType.TRACK).setSpaceType(0, 3, SpaceType.TRACK)
		.setSpaceType(0, 2, SpaceType.TRACK).setSpaceType(0, 1, SpaceType.TRACK).build();
	addBehaviour(new RobotPositionResponder(this, grid));

    }

    protected void takeDown() {

    }

    public void sendPosition() {

    }

}
