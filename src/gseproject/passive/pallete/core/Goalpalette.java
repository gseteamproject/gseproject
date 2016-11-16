package gseproject.passive.pallete.core;

import gseproject.core.Block;

public class Goalpalette extends Palette {
	@Override
	/*
	 * @return return null coz you can't take a box from a goal palette
	 */
	public Block takeBlock() {
		return null;
	}

	@Override
	public Boolean giveBlock(Block block) {
		if (block.Status == Block.possibleBlockStatus.PAINTED) {
			return true;
		}
		return false;
	}
}
