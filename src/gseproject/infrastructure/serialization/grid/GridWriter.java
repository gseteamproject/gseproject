package gseproject.infrastructure.serialization.grid;

import java.io.DataOutputStream;
import java.io.IOException;

import gseproject.infrastructure.contracts.GridContract;
import gseproject.infrastructure.serialization.IWriter;

public class GridWriter implements IWriter<GridContract> {

	@Override
	public void write(GridContract gridContract, DataOutputStream stream) throws IOException {
		stream.writeInt(gridContract.getGrid().getWidth());
		stream.writeInt(gridContract.getGrid().getHeight());
		for (int x = 0; x < gridContract.getGrid().getWidth(); x++) {
			for (int y = 0; y < gridContract.getGrid().getHeight(); y++) {
				stream.writeUTF(gridContract.getGrid().getSpaces()[x][y].name());
			}
		}
	}
}
