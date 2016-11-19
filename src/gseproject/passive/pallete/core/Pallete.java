package gseproject.passive.pallete.core;

import java.util.List;

import gseproject.core.Block;

public abstract class Pallete {
    protected List<Block> blocks;

    public List<Block> getBlocks() {
	return this.blocks;
    }

    @Override
    public String toString() {
	return "Pallete [blocks=" + blocks + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((blocks == null) ? 0 : blocks.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Pallete other = (Pallete) obj;
	if (blocks == null) {
	    if (other.blocks != null)
		return false;
	} else if (!blocks.equals(other.blocks))
	    return false;
	return true;
    }

   

}
