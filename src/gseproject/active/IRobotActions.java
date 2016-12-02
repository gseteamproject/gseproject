package gseproject.active;

import gseproject.core.Block;
import gseproject.grid.GridSpace;

public interface IRobotActions {
	public void move();
	public Block pick(GridSpace position);
	public void drop(GridSpace dropPosition);
	public void doWork(GridSpace workPosition);
}
