package gseproject.experiments.grid.example;

import gseproject.experiments.grid.Position;
import gseproject.experiments.grid.Grid;
import gseproject.experiments.grid.Grid.GridBuilder;
import gseproject.experiments.grid.objects.GoalPalette;
import gseproject.experiments.grid.objects.SourcePalette;

public class GridExample {

	public static void main(String[] args) {
		Grid gridThree = new GridBuilder(20, 11)
				.addGridObject("sourcePaletteOne", new SourcePalette(new Position(0, 0), 2, 3))
				.addGridObject("sourcePaletteTwo", new SourcePalette(new Position(0, 4), 2, 3))
				.addGridObject("sourcePaletteThree", new SourcePalette(new Position(0, 8), 2, 3))
				.addGridObject("goalPaletteOne", new GoalPalette(new Position(18, 0), 2, 3))
				.addGridObject("goalPaletteTwo", new GoalPalette(new Position(18, 4), 2, 3))
				.addGridObject("goalPaletteThree", new GoalPalette(new Position(18, 8), 2, 3))
				.build();
		System.out.println(gridThree.getGridObjects().keySet());
		System.out.println(gridThree.toString());
	}
}
