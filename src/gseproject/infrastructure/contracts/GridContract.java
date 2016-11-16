package gseproject.infrastructure.contracts;

import gseproject.core.grid.Grid;

public class GridContract {
	
	private Grid grid;

	public GridContract(Grid grid) {
		this.grid = grid;
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
}