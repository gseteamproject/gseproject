package gseproject.core.grid.objects;

import gseproject.core.grid.Position;
import gseproject.core.grid.SpaceType;

public class SourcePalette extends Palette {
	
	public SourcePalette(Position position, int width, int height) {
		super(position, width, height, SpaceType.SOURCE_PALETTE);
	}
}
