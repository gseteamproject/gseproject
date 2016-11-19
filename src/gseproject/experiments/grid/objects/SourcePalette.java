package gseproject.experiments.grid.objects;

import gseproject.experiments.grid.Position;
import gseproject.experiments.grid.Tile;

public class SourcePalette extends Palette {
	
	public SourcePalette(Position position, int width, int height) {
		super(position, width, height, Tile.SOURCE_PALLETE);
	}
}
