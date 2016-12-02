package gseproject.tests.serialization;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import gseproject.grid.Position;
import gseproject.infrastructure.contracts.RobotSkillContract;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotSkillReader;
import gseproject.infrastructure.serialization.robot.RobotSkillWriter;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;

public class SerializationControllerTest {

	private static SerializationController sc;

	@BeforeClass
	public static void setUp() {
		sc = SerializationController.Instance;
		RobotStateWriter robotStateWriter = new RobotStateWriter();
		RobotStateReader robotStateReader = new RobotStateReader();
		sc.RegisterSerializator(RobotStateContract.class, robotStateWriter, robotStateReader);

		RobotSkillWriter robotSkillWriter = new RobotSkillWriter();
		RobotSkillReader robotSkillReader = new RobotSkillReader();
		sc.RegisterSerializator(RobotSkillContract.class, robotSkillWriter, robotSkillReader);

	}

	private void isObjectSameAfterSerialization(Object expected) {
		String serialized = sc.Serialize(expected);
		assertEquals(sc.Deserialize(expected.getClass(), serialized), expected);
	}

	@Test
	public void robotStateContractSerializationTest() {
		RobotStateContract robotStateContract = new RobotStateContract();
		robotStateContract.isCarryingBlock = true;
		robotStateContract.position = new Position(1, 5);
		isObjectSameAfterSerialization(robotStateContract);
	}

	@Test
	public void robotSkillContractTest() {
		RobotSkillContract robotSkillContract = new RobotSkillContract();
		robotSkillContract.cost = 5;
		robotSkillContract.duration = 7;
		robotSkillContract.id = new UUID(0, 0);
		isObjectSameAfterSerialization(robotSkillContract);
	}

}
