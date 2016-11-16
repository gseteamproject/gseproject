package gseproject.passive.pallete.core;

import gseproject.core.Block;

public class Sourcepalette extends Palette {
	@Override
	/*
	 * @return return false coz you can't give a box to a source palette
	 */
	public Boolean giveBlock(Block block) {
		return false;
	}
}
