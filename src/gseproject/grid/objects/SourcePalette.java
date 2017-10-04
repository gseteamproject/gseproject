package gseproject.grid.objects;

import gseproject.grid.Position;
import gseproject.grid.SpaceType;

public class SourcePalette extends Palette {
	
	private static final long serialVersionUID = -5521076511363386196L;

	public SourcePalette(Position position, int width, int height) {
		super(position, width, height, SpaceType.SOURCE_PALETTE);
	}
}
