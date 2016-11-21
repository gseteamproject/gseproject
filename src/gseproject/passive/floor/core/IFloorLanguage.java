package gseproject.passive.floor.core;

import gseproject.passive.floor.core.FloorException;

public interface IFloorLanguage {

    /**
     * The robot that occupies the floor can process a block. This methods
     * represents the action of processing a block.
     * 
     * @throws FloorException
     *             if there is no block on the floor or the block has the wrong
     *             state.
     */
    public void finishBlock() throws FloorException;

    /**
     * A robot can occupy a floor. This method represents the action of
     * occupying a floor.
     * 
     * @throws FloorException
     *             if the floor is already occupied.
     */
    public void iOccupy() throws FloorException;

    /**
     * A robot can leave the floor. This method represents the action of
     * leaving.
     * 
     * @throws FloorException
     *             if no robot has occupied the floor.
     */
    public void iLeave() throws FloorException;

}
