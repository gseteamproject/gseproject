package gseproject.tests.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import gseproject.core.Block;
import gseproject.core.Block.possibleBlockStatus;
import gseproject.passive.core.StationException;
import gseproject.passive.pallete.core.GoalPallete;

public class GoalpaletteTest {
	@Test
	public void testGiveBlock() {
		GoalPallete goal = new GoalPallete(10);
		Block block = new Block();
		block.Status = Block.possibleBlockStatus.PAINTED;
		try {
		    goal.giveBlock(block);
		} catch (StationException e) {
		    e.printStackTrace();
		}
	}

	

}
