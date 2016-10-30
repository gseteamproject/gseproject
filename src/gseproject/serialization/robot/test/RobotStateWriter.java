package gseproject.serialization.robot.test;

import gseproject.infrastructure.serialization.IWriter;
import gseproject.serialization.robot.test.RobotStateContract;

import java.io.DataOutputStream;
import java.io.IOException;

public class RobotStateWriter implements IWriter<RobotStateContract>{

    @Override
    public void write(RobotStateContract data, DataOutputStream stream) throws IOException {
        stream.writeBoolean(data.isCarryingBlock);
        stream.writeFloat(data.position);
        stream.writeByte(data.by);
        stream.writeShort(data.sh);
        stream.writeInt(data.intt);
        stream.writeLong(data.lng);
        stream.writeDouble(data.dbl);
        stream.writeChar(data.ch);
        stream.writeUTF(data.str1);
        
    }
}
