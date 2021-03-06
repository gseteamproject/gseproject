package gseproject.infrastructure.serialization.robot;

import gseproject.grid.Position;
import gseproject.infrastructure.contracts.RobotGoalContract;
import gseproject.infrastructure.serialization.IReader;
import jade.core.AID;

import java.io.DataInputStream;
import java.io.IOException;


public class RobotGoalReader implements IReader<RobotGoalContract> {

    public RobotGoalContract read(DataInputStream stream) throws IOException {
        RobotGoalContract contract = new RobotGoalContract();
        contract.aid = new AID(stream.readUTF(), AID.ISGUID);
        contract.currentPosition = new Position(stream.readInt(), stream.readInt());
        contract.goal = new Position(stream.readInt(), stream.readInt());
        return contract;
    }
}
