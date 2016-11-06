package gseproject.infrastructure.serialization.robot;

import gseproject.infrastructure.contracts.RobotSkillContract;
import gseproject.infrastructure.serialization.IReader;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class RobotSkillReader implements IReader<RobotSkillContract> {

    public RobotSkillContract read(DataInputStream stream) throws IOException {
        RobotSkillContract skillContract = new RobotSkillContract();
        skillContract.cost = stream.readInt();
        skillContract.duration = stream.readInt();
        skillContract.id = UUID.fromString(stream.readUTF());
        return skillContract;
    }

}
