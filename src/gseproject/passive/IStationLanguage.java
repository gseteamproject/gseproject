package gseproject.passive;

import gseproject.*;

public interface IStationLanguage {
	
	public boolean hasBlock();
	public boolean isOccupied();
	
	public void giveBlock(Block block);
	public void iOccuppy();
	public void iLeave();
	public void takeBlock();
}
