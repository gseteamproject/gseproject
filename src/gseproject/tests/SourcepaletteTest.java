package gseproject.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import gseproject.core.Block;
import gseproject.passive.palette.Sourcepalette;

public class SourcepaletteTest {

	@Test
	public void testGiveBlock() {
		Sourcepalette source = new Sourcepalette();
		Block block = new Block();
		assertEquals("New block is not dirty!", Block.possibleBlockStatus.DIRTY, block.Status);
		assertFalse("Source should't take a new block", source.giveBlock(block));
	}

	@Test
	public void testHasBlock() {
		Sourcepalette source = new Sourcepalette();
		assertTrue("Sourcepalette should always has a block", source.hasBlock());
	}

	@Test
	public void testIsOccupied() {
		Sourcepalette source = new Sourcepalette();
		assertFalse("Source should't be occuppied at first", source.isOccupied());
		assertTrue("Palette can't be set to occupied on first call", source.iOccuppy());
		assertTrue("Source should be occupied after call iOccuppy", source.isOccupied());
	}

	@Test
	public void testIOccuppy() {
		Sourcepalette source = new Sourcepalette();
		assertTrue("Palette can't be set to occupied on first call", source.iOccuppy());
		assertFalse("Palette could be set to occupied after firt call", source.iOccuppy());
	}

	@Test
	public void testILeave() {
		Sourcepalette source = new Sourcepalette();
		assertTrue("Palette can't be set to occupied on first call", source.iOccuppy());
		source.iLeave();
		assertFalse("Palette can't be leaved", source.isOccupied());
	}

	@Test
	public void testTakeBlock() {
		Sourcepalette source = new Sourcepalette();
		assertNotNull("Source should always return a block", source.takeBlock());
	}

}
