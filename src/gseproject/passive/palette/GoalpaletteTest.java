package gseproject.passive.palette;

import static org.junit.Assert.*;

import org.junit.Test;

import gseproject.core.Block;
import gseproject.core.Block.possibleBlockStatus;

public class GoalpaletteTest {

	@Test
	public void testTakeBlock() {
		Goalpalette goal = new Goalpalette();
		assertNull("You should't be able to take a block from the goal", goal.takeBlock());
	}

	/*
	 * info: not important coz its true every time
	@Test
	public void testHasBlock() {
		Goalpalette goal = new Goalpalette();
		assertTrue("");
	}
	*/

	@Test
	public void testIsOccupied() {
		Goalpalette goal = new Goalpalette();
		assertFalse("Goal should't be occuppied at first", goal.isOccupied());
		assertTrue("Palette can't be set to occupied on first call", goal.iOccuppy());
		assertTrue("Goal should be occupied after call iOccuppy", goal.isOccupied());
	}

	@Test
	public void testGiveBlock() {
		Goalpalette goal = new Goalpalette();
		Block block = new Block();
		assertEquals("New block is not dirty!", Block.possibleBlockStatus.DIRTY, block.Status);
		assertFalse("Goal accepts unfinished block", goal.giveBlock(block));
		block.Status = possibleBlockStatus.CLEANED;
		assertFalse("Goal accepts unfinished block", goal.giveBlock(block));
		block.Status = possibleBlockStatus.PAINTED;
		assertTrue("can't give block to goalpalette", goal.giveBlock(block));
	}

	@Test
	public void testIOccuppy() {
		Goalpalette goal = new Goalpalette();
		assertTrue("Palette can't be set to occupied on first call", goal.iOccuppy());
		assertFalse("Palette could be set to occupied after firt call", goal.iOccuppy());
	}

	@Test
	public void testILeave() {
		Goalpalette goal = new Goalpalette();
		assertTrue("Palette can't be set to occupied on first call", goal.iOccuppy());
		goal.iLeave();
		assertFalse("Palette can't be leaved", goal.isOccupied());
	}

}
