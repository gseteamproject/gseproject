package gseproject.infrastructure.serialization.grid;

import java.io.DataInputStream;
import java.io.IOException;

import gseproject.core.grid.SpaceType;
import gseproject.core.grid.Grid.GridBuilder;
import gseproject.infrastructure.contracts.GridContract;
import gseproject.infrastructure.serialization.IReader;

public class GridReader implements IReader<GridContract> {

	@Override
	public GridContract read(DataInputStream stream) throws IOException {
		int width  = stream.readInt();
		int height = stream.readInt();
		GridBuilder gb = new GridBuilder(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				gb.setSpaceType(x, y, SpaceType.valueOf(stream.readUTF()));
			}
		}
		return new GridContract(gb.build());
	}

}
