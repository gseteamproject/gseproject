package gseproject.serialization.robot.test;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gseproject.core.Direction;
import gseproject.core.grid.Position;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;

public class SerializationControllerTest {

    SerializationController sc;
    RobotStateWriter writer = new RobotStateWriter();
    RobotStateReader reader = new RobotStateReader();

    @Before
    public void setUp() {
	sc = SerializationController.Instance;
	sc.RegisterSerializator(RobotStateContract.class, writer, reader);
    }

    @Test
    public void serializeDeserialize() {
	RobotStateContract rtcA = new RobotStateContract();
	rtcA.isCarryingBlock = true;
	rtcA.direction = Direction.EAST;
	rtcA.position = new Position(1, 5);
	rtcA.goal = new Position(5, 5);

	String str = sc.Serialize(rtcA);

	RobotStateContract rtcB = sc.Deserialize(RobotStateContract.class, str);

	assertEquals(rtcA.isCarryingBlock, rtcB.isCarryingBlock);
	assertEquals(rtcA.direction, rtcB.direction);
	assertEquals(rtcA.position, rtcB.position);
	assertEquals(rtcA.goal, rtcB.goal);
    }
}
