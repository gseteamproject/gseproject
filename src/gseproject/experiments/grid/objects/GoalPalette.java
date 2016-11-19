package gseproject.experiments.grid.objects;

import gseproject.experiments.grid.Position;
import gseproject.experiments.grid.Tile;

public class GoalPalette extends Palette {
	
	public GoalPalette(Position position, int width, int height) {
		super(position, width, height, Tile.GOAL_PALLETE);
	}
}
