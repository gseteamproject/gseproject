package gseproject.grid;

import java.util.List;

public class Grid {
	
	private int xRange;
	private int yRange;
	private List<List<GridSpace>> spaces;
	
	public Grid(int xRange, int yRange) {
		this.xRange = xRange;
		this.yRange = yRange;
	}
	
	public int getXRange() {
		return this.xRange;
	}
	
	public int getYRange() {
		return this.yRange;
	}
	
	public void addGridSpace(GridSpace gs) {
		spaces.get(gs.getXCoordinate()).set(gs.getYCoordinate(), gs);
	}
	
	
	
}
