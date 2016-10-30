package gseproject.robot.serialization;

import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.IWriter;

import java.io.DataOutputStream;
import java.io.IOException;

public class RobotStateWriter implements IWriter<RobotStateContract>{

    @Override
    public void write(RobotStateContract data, DataOutputStream stream) throws IOException {
        stream.writeBoolean(data.isCarryingBlock);
        stream.writeFloat(data.position);
    }
}
