package gseproject.core.grid.communicator;

import gseproject.core.grid.Grid;
import jade.core.Agent;

public class GridCommunicator implements IGridCommunicator {
	private Agent gridAgent;
	private Grid grid;

	public GridCommunicator(Agent gridAgent, Grid grid) {
		this.gridAgent = gridAgent;
		this.grid = grid;
	}

	@Override
	public void receiveRobotStateContract() {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveFloorContract() {
		// TODO Auto-generated method stub

	}

	@Override
	public void receivePaletteContract() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendGridToGUIAgent() {
		// TODO Auto-generated method stub

	}

}
