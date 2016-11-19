package gseproject.passive.floor.core;

import gseproject.core.Block;
import gseproject.passive.core.IGive;
import gseproject.passive.core.ITake;
import gseproject.passive.core.StationException;

public class CleaningFloor extends Floor {

    @Override
    public void giveBlock(Block block) throws StationException {
	if (this.block != null || this.hasBlock) {
	    throw new StationException("Already have a block");
	}
	if (!block.Status.equals(Block.possibleBlockStatus.DIRTY)) {
	    throw new StationException("Cleaning floor can only take dirty blocks. Your block is: " + block.toString());
	}
	if (this.hasFinishedBlock) {
	    throw new StationException("Need to get rid of finished block before you can give me another.");
	}
	this.block = block;
	this.hasBlock = true;
	this.hasFinishedBlock = false;
    }

    @Override
    public Block takeBlock() throws StationException {
	if (!this.block.Status.equals(Block.possibleBlockStatus.CLEANED) || !this.hasFinishedBlock) {
	    throw new StationException("Occupier needs to finish block first.");
	}
	Block toReturn = new Block();
	toReturn.Status = this.block.Status;
	this.hasFinishedBlock = false;
	this.hasBlock = false;
	this.block = null;
	return toReturn;
    }

    @Override
    public void finishBlock() throws FloorException {
	if (!this.block.Status.equals(Block.possibleBlockStatus.DIRTY)) {
	    throw new FloorException("Block is not dirty. So it cannot be cleaned.");
	}
	if (this.block == null || !this.hasBlock) {
	    throw new FloorException("there is no block that can be cleaned.");
	}
	this.block.Status = Block.possibleBlockStatus.CLEANED;
	this.hasFinishedBlock = true;
	this.hasBlock = false;
    }

    @Override
    public String toString() {
	return "CleaningFloor [hasBlock=" + hasBlock + ", isOccupied=" + occupied + ", hasFinishedBlock="
		+ hasFinishedBlock + ", block=" + block + "]";
    }

}
