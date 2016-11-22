package gseproject.core.grid.objects;

import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;

public class GoalPalette extends Palette {
	
	public GoalPalette(Position position, int width, int height) {
		super(position, width, height, SpaceType.GOAL_PALETTE);
	}
}
