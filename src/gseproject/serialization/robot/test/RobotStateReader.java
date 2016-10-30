package gseproject.serialization.robot.test;

import gseproject.serialization.robot.test.RobotStateContract;
import gseproject.infrastructure.serialization.IReader;

import java.io.DataInputStream;
import java.io.IOException;

public class RobotStateReader implements IReader<RobotStateContract> {

    @Override
    public RobotStateContract read(DataInputStream stream) throws IOException{
        RobotStateContract stateDto = new RobotStateContract();
        stateDto.isCarryingBlock = stream.readBoolean();
        stateDto.position =  stream.readFloat();
        stateDto.by = stream.readByte();
        stateDto.sh = stream.readShort();
        stateDto.intt = stream.readInt();
        stateDto.lng = stream.readLong();
        stateDto.dbl = stream.readDouble();
        stateDto.ch = stream.readChar();
        stateDto.str1 = stream.readUTF();
        
        return stateDto;
    }
}
