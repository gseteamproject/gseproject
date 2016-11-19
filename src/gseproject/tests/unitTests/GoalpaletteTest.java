
package gseproject.tests.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import gseproject.core.Block;
import gseproject.passive.core.StationException;
import gseproject.passive.pallete.core.GoalPalette;

public class GoalpaletteTest {
	@Test
	public void testGiveBlock() {
		GoalPalette goal = new GoalPalette(10);
		Block block = new Block();
		block.Status = Block.possibleBlockStatus.PAINTED;
		try {
		    goal.giveBlock(block);
		} catch (StationException e) {
		    e.printStackTrace();
		}
		assertTrue(goal.getBlocks().size() == 1);
	}

	

}

