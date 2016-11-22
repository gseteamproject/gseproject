package gseproject.passive.core;

import java.io.Serializable;

import gseproject.core.Block;
import gseproject.core.interaction.IState;

public abstract class Floor implements IFloorLanguage, IGive, ITake, Serializable {
	private static final long serialVersionUID = 6082271716662280434L;
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
	/* END GETTER */

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

	/* END SETTER */

	@Override
	public String toString() {
		return "Floor [hasBlock=" + hasBlock + ", isOccupied=" + occupied + ", hasFinishedBlock=" + hasFinishedBlock
				+ ", block=" + block + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Floor other = (Floor) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		if (hasBlock != other.hasBlock)
			return false;
		if (hasFinishedBlock != other.hasFinishedBlock)
			return false;
		if (occupied != other.occupied)
			return false;
		return true;
	}

}
