package gseproject.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import gseproject.core.Block;
import gseproject.passive.floor.FloorException;
import gseproject.passive.floor.PaintingFloor;

public class PaintingFloorTest extends FloorTest {
    @Before
    public void initialize() {
	this.f = new PaintingFloor();
    }

    private static Block getCleanedBlock() {
	Block b = new Block();
	b.Status = Block.possibleBlockStatus.CLEANED;
	return b;
    }

    @Test
    public void giveBlockTest() {
	try {
	    f.giveBlock(getCleanedBlock());
	} catch (FloorException e) {
	    e.printStackTrace();
	}
	assertTrue(f.hasBlock());
	assertFalse(f.hasFinishedBlock());
    }

    @Test
    public void takeBlockTest() {
	Block b = null;
	try {
	    f.giveBlock(getCleanedBlock());
	    assertTrue(f.hasBlock());
	    assertFalse(f.hasFinishedBlock());
	    f.finishBlock();
	    assertTrue(f.hasFinishedBlock());
	    assertFalse(f.hasBlock());
	    b = f.takeBlock();
	    assertFalse(f.hasBlock());
	    assertFalse(f.hasFinishedBlock());
	} catch (FloorException e) {
	    e.printStackTrace();
	}
	assertEquals(b.Status, Block.possibleBlockStatus.PAINTED);
    }
}
