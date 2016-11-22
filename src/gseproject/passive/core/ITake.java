package gseproject.passive.core;

import gseproject.core.Block;

public interface ITake {
    /**
     * A robot can take a block from a station. This method represents the
     * action of taking the block from the floor.
     * 
     * @return the block that is taken.
     * @throws FloorException
     *             if the block is not finished.
     */
    public Block takeBlock() throws StationException;
}
