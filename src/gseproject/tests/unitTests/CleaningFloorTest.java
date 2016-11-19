package gseproject.tests.unitTests;

import org.junit.Before;
import org.junit.Test;

import gseproject.core.Block;
import gseproject.passive.core.StationException;
import gseproject.passive.floor.core.CleaningFloor;
import gseproject.passive.floor.core.FloorException;

import static org.junit.Assert.*;

public class CleaningFloorTest extends FloorTest {
    private static Block getDirtyBlock() {
	Block b = new Block();
	b.Status = Block.possibleBlockStatus.DIRTY;
	return b;
    }

    @Before
    public void initialize() {
	this.f = new CleaningFloor();
    }

    @Test
    public void giveBlockTest() {
	try {
	    f.giveBlock(getDirtyBlock());
	} catch (StationException e) {
	    e.printStackTrace();
	}
	assertTrue(f.hasBlock());
	assertFalse(f.hasFinishedBlock());
    }

    @Test
    public void takeBlockTest() {
	Block b = null;
	try {
	    f.giveBlock(getDirtyBlock());
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
	} catch (StationException e) {
	    e.printStackTrace();
	}
	assertEquals(b.Status, Block.possibleBlockStatus.CLEANED);
    }
}
