package gseproject.passive.pallete.core;

import java.util.ArrayList;

import gseproject.core.Block;
import gseproject.passive.core.ITake;
import gseproject.passive.core.StationException;

public class SourcePalette extends Palette implements ITake {
	private int amountOfBlocks;
	private int maxAmountOfBlocks;

	private void refill() {
		for (int i = 0; i < this.maxAmountOfBlocks; i++) {
			Block b = new Block();
			b.Status = Block.possibleBlockStatus.DIRTY;
			blocks.add(b);
		}
	}

	public SourcePalette(int amountOfBlocks, int maxAmountOfBlocks) {
		this.amountOfBlocks = amountOfBlocks;
		this.maxAmountOfBlocks = maxAmountOfBlocks;
		this.blocks = new ArrayList<>();
		refill();
	}

	@Override
	public Block takeBlock() {
		if (amountOfBlocks == 0) {
			refill();
		}
		return this.blocks.remove(0);
	}

	public int getAmountOfBlocks() {
		return this.getAmountOfBlocks();
	}

	public int getMaxAmountOfBlocks() {
		return this.getMaxAmountOfBlocks();
	}

	@Override
	public String toString() {
		return "SourcePalette [amountOfBlocks=" + amountOfBlocks + ", maxAmountOfBlocks=" + maxAmountOfBlocks + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + amountOfBlocks;
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
		SourcePalette other = (SourcePalette) obj;
		if (amountOfBlocks != other.amountOfBlocks)
			return false;
		if (maxAmountOfBlocks != other.maxAmountOfBlocks)
			return false;
		return true;
	}

}
