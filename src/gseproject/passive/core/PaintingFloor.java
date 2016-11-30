package gseproject.passive.core;

import gseproject.core.Block;

public class PaintingFloor extends Floor {

	@Override
	public synchronized void giveBlock(Block block) throws StationException {
		if (this.block != null || this.hasBlock) {
			throw new StationException("Already have a block");
		}
		if (!block.Status.equals(Block.possibleBlockStatus.CLEANED)) {
			throw new StationException(
					"Painting floor can only take cleaned blocks. Your block is: " + block.toString());
		}
		if (this.hasFinishedBlock) {
			throw new StationException("Need to get rid of finished block before you can give me another.");
		}
		this.block = block;
		this.hasBlock = true;
		this.hasFinishedBlock = false;
		System.out.println("give Block " + this);
	}

	@Override
	public synchronized Block takeBlock() throws StationException {
		if (!this.block.Status.equals(Block.possibleBlockStatus.PAINTED) || !this.hasFinishedBlock) {
			throw new StationException("Occupier needs to finish block first.");
		}
		Block toReturn = new Block();
		toReturn.Status = this.block.Status;
		this.hasFinishedBlock = false;
		this.hasBlock = false;
		this.block = null;
		System.out.println("takeBlock" + this);
		return toReturn;

	}

	@Override
	public synchronized void finishBlock() throws FloorException {
		if (!this.block.Status.equals(Block.possibleBlockStatus.CLEANED)) {
			throw new FloorException("Block is not cleaned. So it cannot be painted.");
		}
		if (!this.hasBlock || this.block == null) {
			throw new FloorException("there is no block that can be painted");
		}
		this.block.Status = Block.possibleBlockStatus.PAINTED;
		this.hasFinishedBlock = true;
		this.hasBlock = false;
		System.out.println("finish block " + this);
	}

	@Override
	public String toString() {
		return "PaintingFloor [hasBlock=" + hasBlock + ", isOccupied=" + occupied + ", hasFinishedBlock="
				+ hasFinishedBlock + ", block=" + block + "]";
	}

}
