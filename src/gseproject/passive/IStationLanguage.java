package gseproject.passive;

import gseproject.grid.Block;

public interface IStationLanguage {
	
	public boolean hasBlock();
	public boolean isOccupied();
	
	public Boolean giveBlock(Block block);
	public Boolean iOccuppy();
	public void iLeave();
	public Block takeBlock();
}
