package gseproject.passive.pallete.core;

import gseproject.core.Block;

public class Goalpalette extends gseproject.passive.pallete.core.Pallete {
	@Override
	/*
	 * @return return null coz you can't take a box from a goal palette
	 */
	public Block takeBlock() {
		return null;
	}

	
    @Override
    public void giveBlock(Block block) throws gseproject.passive.pallete.core.PalleteException {
	if (block.Status != block.Status.PAINTED) {
	    throw new PalleteException("Only finished blocks please");
	}
    }
}
