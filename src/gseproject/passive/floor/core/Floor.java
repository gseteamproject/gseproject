package gseproject.passive.floor.core;

import gseproject.core.Block;
import gseproject.passive.core.IGive;
import gseproject.passive.core.ITake;
import gseproject.passive.floor.IFloorLanguage;

public abstract class Floor implements IFloorLanguage, IGive, ITake{
    protected boolean hasBlock;
    protected boolean occupied;
    protected boolean hasFinishedBlock;
    protected Block block;

    public Floor() {
	this.hasBlock = false;
	this.occupied = false;
	this.hasFinishedBlock = false;
	this.block = null;
    }

    /* GETTER */
    public boolean hasBlock() {
	return this.hasBlock;
    }

    public boolean isOccupied() {
	return this.occupied;
    }

    public boolean hasFinishedBlock() {
	return this.hasFinishedBlock;
    }
    /*END GETTER*/

    /* SETTER */
   
    @Override
    public void iOccupy() throws FloorException {
	if (occupied) {
	    throw new FloorException("I am already occupied");
	}
	this.occupied = true;
    }
    
   @Override
    public void iLeave() throws FloorException {
	if (!occupied) {
	    throw new FloorException("I am not occupied");
	}
	this.occupied = false;
    }
    
   
    /*END SETTER*/

    @Override
    public String toString() {
	return "Floor [hasBlock=" + hasBlock + ", isOccupied=" + occupied + ", hasFinishedBlock=" + hasFinishedBlock
		+ ", block=" + block + "]";
    }
}
