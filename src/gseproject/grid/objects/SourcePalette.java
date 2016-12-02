package gseproject.grid.objects;

import gseproject.grid.Position;
import gseproject.grid.SpaceType;

public class SourcePalette extends Palette {
	
	public SourcePalette(Position position, int width, int height) {
		super(position, width, height, SpaceType.SOURCE_PALETTE);
	}
}
