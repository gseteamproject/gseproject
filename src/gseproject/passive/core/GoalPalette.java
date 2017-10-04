package gseproject.passive.core;

import java.util.ArrayList;

import gseproject.core.Block;

public class GoalPalette extends Palette implements IGive {
	private static final long serialVersionUID = -2923778793617231711L;
	private int maxAmountOfBlocks;

	public GoalPalette(int maxAmountOfBlocks) {
		this.maxAmountOfBlocks = maxAmountOfBlocks;
		this.blocks = new ArrayList<>();
	}

	@Override
	public synchronized void giveBlock(Block block) throws StationException {
		if (!block.Status.equals(Block.possibleBlockStatus.PAINTED)) {
			throw new StationException("block needs to be painted if you want to add him to goal pallete");
		}
		this.blocks.add(block);
		if (this.blocks.size() == this.maxAmountOfBlocks) {
			this.blocks = new ArrayList<Block>();
		}
	}

	public int getMaxAmountOfBlock() {
		return this.getMaxAmountOfBlock();
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
		return "GoalPalette [maxAmountOfBlocks=" + maxAmountOfBlocks + ", blocks=" + blocks + "]";
	}

}
