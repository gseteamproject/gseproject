package gseproject.passive.core;

import gseproject.core.Block;
import gseproject.passive.floor.core.FloorException;

public interface IGive {
    /**
     * A robot can drop a block to a station. This method represents the action
     * of dropping a block to the floor.
     * 
     * @param block
     *            : the block that is dropped.
     * @throws FloorException
     *             if floor already has a block or the block has wrong state.
     */
    public void giveBlock(Block block) throws StationException;

}
