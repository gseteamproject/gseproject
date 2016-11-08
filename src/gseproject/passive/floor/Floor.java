package gseproject.passive.floor;

import gseproject.core.Block;

public abstract class Floor {
    protected boolean hasBlock;
    protected boolean isOccupied;
    protected boolean hasFinishedBlock;
    protected Block block;

    public Floor() {
	this.hasBlock = false;
	this.isOccupied = false;
	this.hasFinishedBlock = false;
	this.block = null;
    }

    /* GETTER */
    public boolean hasBlock() {
	return this.hasBlock;
    }

    public boolean isOccupied() {
	return this.isOccupied;
    }

    public boolean hasFinishedBlock() {
	return this.hasFinishedBlock;
    }
    /*END GETTER*/

    /* SETTER */
    public void iOccupy() throws FloorException {
	if (isOccupied) {
	    throw new FloorException("I am already occupied");
	}
	this.isOccupied = true;
    }

    public void iLeave() throws FloorException {
	if (!isOccupied) {
	    throw new FloorException("I am not occupied");
	}
	this.isOccupied = false;
    }

    public abstract void giveBlock(Block block) throws FloorException;

    public abstract Block takeBlock() throws FloorException;

    public abstract void finishBlock() throws FloorException;
    
    /*END SETTER*/

    @Override
    public String toString() {
	return "Floor [hasBlock=" + hasBlock + ", isOccupied=" + isOccupied + ", hasFinishedBlock=" + hasFinishedBlock
		+ ", block=" + block + "]";
    }
}
