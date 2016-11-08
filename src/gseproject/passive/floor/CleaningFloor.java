package gseproject.passive.floor;

import gseproject.core.Block;

public class CleaningFloor extends Floor {

    @Override
    public void giveBlock(Block block) throws FloorException {
	if (this.hasFinishedBlock) {
	    throw new FloorException("Cannot take another block. Finished block needs to be picked.");
	}
	if (this.block != null || this.hasBlock) {
	    throw new FloorException("Already have a block");
	}
	this.block = block;
	this.hasBlock = true;
	this.hasFinishedBlock = false;
    }

    @Override
    public Block takeBlock() throws FloorException {
	if (!this.block.Status.equals(Block.possibleBlockStatus.CLEANED)) {
	    throw new FloorException("block is not cleaned.");
	}
	if (!this.hasFinishedBlock) {
	    throw new FloorException("Do not have a finished block");
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
	if (this.block == null) {
	    throw new FloorException("there is no block that can be cleaned");
	}
	this.block.Status = Block.possibleBlockStatus.CLEANED;
	this.hasFinishedBlock = true;
	this.hasBlock = false;
    }

    @Override
    public String toString() {
	return "CleaningFloor [hasBlock=" + hasBlock + ", isOccupied=" + isOccupied + ", hasFinishedBlock="
		+ hasFinishedBlock + ", block=" + block + "]";
    }
    
    

}
