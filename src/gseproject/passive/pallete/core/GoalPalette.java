package gseproject.passive.pallete.core;

import java.util.ArrayList;

import gseproject.core.Block;
import gseproject.passive.core.IGive;
import gseproject.passive.core.StationException;

public class GoalPalette extends Pallete implements IGive {
    private int maxAmountOfBlocks;

    public GoalPalette(int maxAmountOfBlocks) {
	this.maxAmountOfBlocks = maxAmountOfBlocks;
	this.blocks = new ArrayList<>();
    }

    @Override
    public void giveBlock(Block block) throws StationException {
	if (!block.Status.equals(Block.possibleBlockStatus.PAINTED)) {
	    throw new StationException("block needs to be painted if you want to add him to goal pallete");
	}
	if (this.blocks.size() == this.maxAmountOfBlocks) {
	    this.blocks = new ArrayList<Block>();
	}
	this.blocks.add(block);
    }

    public int getMaxAmountOfBlock() {
	return this.getMaxAmountOfBlock();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + maxAmountOfBlocks;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	GoalPalette other = (GoalPalette) obj;
	if (maxAmountOfBlocks != other.maxAmountOfBlocks)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "GoalPallete [maxAmountOfBlocks=" + maxAmountOfBlocks + "]";
    }

}
