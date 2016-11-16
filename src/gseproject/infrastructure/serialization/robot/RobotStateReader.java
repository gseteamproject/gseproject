package gseproject.infrastructure.serialization.robot;

import java.io.DataInputStream;
import java.io.IOException;

import gseproject.core.Direction;
import gseproject.core.grid.Position;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.IReader;

public class RobotStateReader implements IReader<RobotStateContract> {

	public RobotStateContract read(DataInputStream stream) throws IOException {
		RobotStateContract stateDto = new RobotStateContract();
		stateDto.isCarryingBlock = stream.readBoolean();
		stateDto.position = new Position(stream.readInt(), stream.readInt());
		stateDto.goal = new Position(stream.readInt(), stream.readInt());
		stateDto.direction = Direction.valueOf(stream.readUTF());
		return stateDto;
	}
}
