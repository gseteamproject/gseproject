package gseproject.grid.example;

import gseproject.core.Direction;
import gseproject.grid.Grid;
import gseproject.grid.Position;
import gseproject.grid.Grid.GridBuilder;
import gseproject.grid.objects.GoalPalette;
import gseproject.grid.objects.SourcePalette;
import jade.core.AID;

public class GridExample {

	public static void main(String[] args) {
		Grid grid = new GridBuilder(20, 11)
				.addGridObject(new AID("sourcePaletteOne", true), new SourcePalette(new Position(0, 0), 2, 3))
				.addGridObject(new AID("sourcePaletteTwo", true), new SourcePalette(new Position(0, 4), 2, 3))
				.addGridObject(new AID("sourcePaletteThree", true), new SourcePalette(new Position(0, 8), 2, 3))
				.addGridObject(new AID("goalPaletteOne", true), new GoalPalette(new Position(18, 0), 2, 3))
				.addGridObject(new AID("goalPaletteTwo", true), new GoalPalette(new Position(18, 4), 2, 3))
				.addGridObject(new AID("goalPaletteThree", true), new GoalPalette(new Position(18, 8), 2, 3))
				.addTrack(new Position(3, 0), Direction.EAST, 12)
				.addTrack(new Position(16, 0), Direction.SOUTH, 11)
				.addTrack(new Position(16, 10), Direction.WEST, 12)
				.addTrack(new Position(3, 10), Direction.NORTH, 11)
				.build();
		System.out.println(grid.getGridObjects().keySet());
		System.out.println(grid.toString());
	}
}
