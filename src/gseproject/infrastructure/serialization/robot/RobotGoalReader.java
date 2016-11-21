package gseproject.infrastructure.serialization.robot;

import gseproject.core.grid.Position;
import gseproject.infrastructure.contracts.RobotGoalContract;
import gseproject.infrastructure.serialization.IReader;
import jade.core.AID;

import java.io.DataInputStream;
import java.io.IOException;


public class RobotGoalReader implements IReader<RobotGoalContract> {

    public RobotGoalContract read(DataInputStream stream) throws IOException {
        RobotGoalContract contract = new RobotGoalContract();
        contract.aid = new AID(stream.readUTF());
        contract.position = new Position(stream.readInt(), stream.readInt());
        return contract;
    }
}
