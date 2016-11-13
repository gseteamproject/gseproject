package gseproject.infrastructure.serialization.robot;

import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.IWriter;

import java.io.DataOutputStream;
import java.io.IOException;

public class RobotStateWriter implements IWriter<RobotStateContract>{

    public void write(RobotStateContract data, DataOutputStream stream) throws IOException {
        stream.writeBoolean(data.isCarryingBlock);
        stream.writeInt(data.position.x);
        stream.writeInt(data.position.y);
    }
}
