package gseproject.passive;

import gseproject.core.Block;

public class Floor implements IFloorLanguage, IStationLanguage {

	@Override
	public boolean hasBlock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOccupied() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean giveBlock(Block block) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean iOccuppy() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void iLeave() {
		// TODO Auto-generated method stub

	}

	@Override
	public Block takeBlock() {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public boolean hasFinishedBlock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFinishedBlock() {
		// TODO Auto-generated method stub
		return false;
	}

}
