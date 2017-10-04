package gseproject.grid.objects;

import gseproject.grid.Position;
import gseproject.grid.SpaceType;

public class GoalPalette extends Palette {
	
	private static final long serialVersionUID = -224099770850162435L;

	public GoalPalette(Position position, int width, int height) {
		super(position, width, height, SpaceType.GOAL_PALETTE);
	}
}
