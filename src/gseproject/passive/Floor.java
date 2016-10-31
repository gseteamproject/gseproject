package gseproject.passive;

import gseproject.Block;


public class Floor implements IFloorLanguage, IStation {

	
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
	public void giveBlock(Block block) {
		// TODO Auto-generated method stub

	}

	@Override
	public void iOccuppy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void iLeave() {
		// TODO Auto-generated method stub

	}

	@Override
	public void takeBlock() {
		// TODO Auto-generated method stub

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
