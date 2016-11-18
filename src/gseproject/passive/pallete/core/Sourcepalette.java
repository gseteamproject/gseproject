package gseproject.passive.pallete.core;

import gseproject.core.Block;

public class Sourcepalette extends gseproject.passive.pallete.core.Pallete {
	@Override
	/*
	 * @return return false coz you can't give a box to a source palette
	 */
	public void giveBlock(Block block) throws PalleteException {
		throw new gseproject.passive.pallete.core.PalleteException("Sourcepalette can't take a block");
	}

	@Override
	public Block takeBlock() throws PalleteException {
		throw new gseproject.passive.pallete.core.PalleteException("Sourcepalette can't take a block");
	}
}
