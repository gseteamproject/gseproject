package gseproject.core.grid;

import gseproject.core.grid.communicator.IGridCommunicator;
import jade.core.Agent;

public class GridAgent extends Agent {
	private static final long serialVersionUID = -9064311405865477921L;
	private Grid grid;
	private IGridCommunicator gridCommunicator;

	private Grid initTestGrid() {
		return grid;
	}

	protected void setup() {
		System.out.println("Grid Agent started");
		this.grid = initTestGrid();
	}

	protected void takeDown() {

	}

	public Grid getGrid() {
		return this.grid;
	}
}
