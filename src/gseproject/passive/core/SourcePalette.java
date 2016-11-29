package gseproject.passive.core;

import java.util.ArrayList;

import gseproject.core.Block;

public class SourcePalette extends Palette implements ITake {
	private int amountOfBlocks;
	private int maxAmountOfBlocks;

	private void refill(int amount) {
		for (int i = 0; i < amount; i++) {
			Block b = new Block();
			b.Status = Block.possibleBlockStatus.DIRTY;
			blocks.add(b);
		}
		this.amountOfBlocks = this.maxAmountOfBlocks;
	}

	public SourcePalette(int amountOfBlocks, int maxAmountOfBlocks) {
		this.amountOfBlocks = amountOfBlocks;
		this.maxAmountOfBlocks = maxAmountOfBlocks;
		this.blocks = new ArrayList<>();
		refill(amountOfBlocks);
	}

	@Override
	public synchronized Block takeBlock() {
		if (amountOfBlocks == 0) {
			refill(maxAmountOfBlocks+1);
			amountOfBlocks=maxAmountOfBlocks;
		} else {
			amountOfBlocks--;
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
