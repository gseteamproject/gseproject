package gseproject.passive.floor;

import gseproject.core.Block;

public class PaintingFloor extends Floor {

    @Override
    public void giveBlock(Block block) throws FloorException {
	if (this.block != null || this.hasBlock) {
	    throw new FloorException("Already have a block");
	}
	if (!block.Status.equals(Block.possibleBlockStatus.PAINTED)) {
	    throw new FloorException("Painting floor can only take cleaned blocks. Your block is: " + block.toString());
	}
	if (this.hasFinishedBlock) {
	    throw new FloorException("Need to get rid of finished block before you can give me another.");
	}
	this.block = block;
	this.hasBlock = true;
	this.hasFinishedBlock = false;
    }

    @Override
    public Block takeBlock() throws FloorException {
	if (!this.block.Status.equals(Block.possibleBlockStatus.PAINTED) || !this.hasFinishedBlock) {
	    throw new FloorException("Occupier needs to finish block first.");
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
	if (!this.block.Status.equals(Block.possibleBlockStatus.CLEANED)) {
	    throw new FloorException("Block is not cleaned. So it cannot be painted.");
	}
	if (!this.hasBlock || this.block == null) {
	    throw new FloorException("there is no block that can be painted");
	}
	this.block.Status = Block.possibleBlockStatus.PAINTED;
	this.hasFinishedBlock = true;
	this.hasBlock = false;
    }

    @Override
    public String toString() {
	return "PaintingFloor [hasBlock=" + hasBlock + ", isOccupied=" + occupied + ", hasFinishedBlock="
		+ hasFinishedBlock + ", block=" + block + "]";
    }
}
