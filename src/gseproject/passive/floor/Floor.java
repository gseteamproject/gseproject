package gseproject.passive.floor;

import gseproject.core.Block;

public abstract class Floor {
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
    /**
     * A floor can only be occupied by one robot. This method represents the action of occupying.
     * @throws FloorException if floor is already occupied.
     */
    public void iOccupy() throws FloorException {
	if (occupied) {
	    throw new FloorException("I am already occupied");
	}
	this.occupied = true;
    }
    
    /**
     * A robot can leave the floor. This method represents the action of leaving.
     * @throws FloorException if no robot has occupied the floor.
     */
    public void iLeave() throws FloorException {
	if (!occupied) {
	    throw new FloorException("I am not occupied");
	}
	this.occupied = false;
    }
    
    /**
     * A robot can drop a block to the floor. This method represents the action of dropping a block to the floor.
     * @param block : the block that is dropped.
     * @throws FloorException if floor already has a block or the block has wrong state.
     */
    public abstract void giveBlock(Block block) throws FloorException;
    
    /**
     * A robot can take a block from the floor. This method represents the action of taking the block from the floor.
     * @return the block that is taken.
     * @throws FloorException if the block is not finished.
     */
    public abstract Block takeBlock() throws FloorException;
    
    /**
     * The robot that occupies the floor can process a block. This methods represents the action of processing a block.
     * @throws FloorException if there is no block on the floor or the block has the wrong state. 
     */
    public abstract void finishBlock() throws FloorException;
    /*END SETTER*/

    @Override
    public String toString() {
	return "Floor [hasBlock=" + hasBlock + ", isOccupied=" + occupied + ", hasFinishedBlock=" + hasFinishedBlock
		+ ", block=" + block + "]";
    }
}
