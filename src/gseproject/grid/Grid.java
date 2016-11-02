package gseproject.grid;

import java.util.ArrayList;
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
	
	/** 
	 * First Attempt at a Grid Initialization builds a 10*10 Grid currently without Working spaces for robots and paths
	 * off the track *derp* Will finish it on Sunday. Possibly lots of Refactoring needed. I go to sleep now^^.
	 */
	public void exampleGridInitialize() {
		
		ArrayList<Direction> onlyNowhere = new ArrayList<Direction>(2);
		onlyNowhere.add(Direction.NOWHERE);
		
		ArrayList<Direction> onlyForward = new ArrayList<Direction>(2);
		onlyForward.add(Direction.FORWARD);
		
		ArrayList<Direction> onlyRight = new ArrayList<Direction>(2);
		onlyRight.add(Direction.RIGHT);
		
		ArrayList<Direction> floorEntrance = new ArrayList<Direction>(2);
		floorEntrance.add(Direction.FORWARD);
		floorEntrance.add(Direction.RIGHT);
		
		Grid testGrid =  new Grid(10,10);
		
		for(int x = 0; x < testGrid.getXRange(); x++) {
			for(int y = 0 ; y < testGrid.getYRange(); y++) {
				
				if(testGrid.isCornerSpace(x, y)) {
					GridSpace space = new GridSpace(x,y,SpaceType.DEFAULT,onlyRight);
					testGrid.addGridSpace(space);
				} else if( x == 4 && y == 0 || x == 8 && y==0) {
					GridSpace space = new GridSpace(x,y,SpaceType.DEFAULT,floorEntrance);
					testGrid.addGridSpace(space);
				} else if(x == 0 || x==testGrid.getXRange() || y == 0 || y == testGrid.getYRange()) {
					GridSpace space = new GridSpace(x,y,SpaceType.DEFAULT,onlyForward);
					testGrid.addGridSpace(space);
				} else {
					GridSpace space = new GridSpace(x,y,SpaceType.NO_TRACK,onlyNowhere);
					testGrid.addGridSpace(space);
				}
					
			}	
		}
		
	}
	
	private boolean isCornerSpace(int x, int y) {
		
		if(x == 0 && y== 0)
			return true;
		if(x == this.getXRange() && y == 0)
			return true;
		if(x == this.getXRange() && y == this.getYRange())
			return true;
		if(x == 0 && y == this.getYRange())
			return true;
		return false;
	}
	
}
