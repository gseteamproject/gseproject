package gseproject.infrastructure.serialization.robot;

import gseproject.infrastructure.contracts.RobotSkillContract;
import gseproject.infrastructure.serialization.IWriter;

import java.io.DataOutputStream;
import java.io.IOException;

public class RobotSkillWriter implements IWriter<RobotSkillContract> {

    public void write(RobotSkillContract data, DataOutputStream stream) throws IOException {
        stream.writeInt(data.cost);
        stream.writeInt(data.duration);
        stream.writeUTF(data.id.toString());
    }

}
