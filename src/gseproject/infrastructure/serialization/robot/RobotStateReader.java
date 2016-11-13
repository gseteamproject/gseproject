package gseproject.infrastructure.serialization.robot;

import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.IReader;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

public class RobotStateReader implements IReader<RobotStateContract> {

    @Override
    public RobotStateContract read(DataInputStream stream) throws IOException{
        RobotStateContract stateDto = new RobotStateContract();
        stateDto.isCarryingBlock = stream.readBoolean();
        stateDto.position.x = stream.readInt();
        stateDto.position.y = stream.readInt();
        return stateDto;
    }
}
