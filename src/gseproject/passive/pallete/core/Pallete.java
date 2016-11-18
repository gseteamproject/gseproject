package gseproject.passive.pallete.core;

import gseproject.core.Block;

public abstract class Pallete {
    private boolean occupied;

	public Pallete() {
	this.occupied = false;
    }

    /* GETTER */
    public boolean hasBlock() {
	return true;
    }

    public boolean isOccupied() {
	return this.occupied;
    }
    /*END GETTER*/

    /* SETTER */
    /**
     * A pallete can only be occupied by one robot. This method represents the action of occupying.
     * @throws PalleteException if pallete is already occupied.
     */
    public void iOccupy() throws PalleteException {
	if (occupied) {
	    throw new PalleteException("I am already occupied");
	}
	this.occupied = true;
    }
    
    /**
     * A robot can leave the pallete. This method represents the action of leaving.
     * @throws PalleteException if no robot has occupied the pallete.
     */
    public void iLeave() throws PalleteException {
	if (!occupied) {
	    throw new PalleteException("I am not occupied");
	}
	this.occupied = false;
    }
    
    /**
     * A robot can drop a block to the pallete. This method represents the action of dropping a block to the pallete.
     * @param block : the block that is dropped.
     * @throws PalleteException if pallete already has a block or the block has wrong state.
     */
    public abstract void giveBlock(Block block) throws PalleteException;
    
    /**
     * A robot can take a block from the pallete. This method represents the action of taking the block from the pallete.
     * @return the block that is taken.
     * @throws PalleteException if the block is not finished.
     */
    public abstract Block takeBlock() throws PalleteException;
    /*END SETTER*/

    @Override
    public String toString() {
	return "Pallete [isOccupied=" + occupied + "]";
    }
}
