package gseproject.tests.unitTests;

import static org.junit.Assert.*;
import org.junit.Test;
import gseproject.core.Block;
import gseproject.passive.core.SourcePalette;
import gseproject.passive.core.StationException;

public class SourcepaletteTest {
    @Test
    public void testTakeBlock() {
	SourcePalette source = new SourcePalette(5, 5);
	Block b = source.takeBlock();
	assertNotNull("Source should always return a block", b);
	assertEquals(Block.possibleBlockStatus.DIRTY, b.Status);
    }

}
