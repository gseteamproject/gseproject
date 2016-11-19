package gseproject.tests.unitTests;

import static org.junit.Assert.*;
import org.junit.Test;
import gseproject.core.Block;
import gseproject.passive.core.StationException;
import gseproject.passive.pallete.core.SourcePalette;

public class SourcepaletteTest {
    @Test
    public void testTakeBlock() {
	SourcePalette source = new SourcePalette(5, 5);
	try {
	    Block b = source.takeBlock();
	    assertNotNull("Source should always return a block", b);
	    assertEquals(Block.possibleBlockStatus.DIRTY, b.Status);
	} catch (StationException e) {
	    e.printStackTrace();
	}
    }

}
