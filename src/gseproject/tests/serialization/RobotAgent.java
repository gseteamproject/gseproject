package gseproject.tests.serialization;

import gseproject.grid.Position;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import jade.core.Agent;

public class RobotAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4765529907681140004L;
	private final SerializationController serializationController;

	public RobotAgent() {
		serializationController = SerializationController.Instance;
		SerializatorsInitialization();
		robotStateContract_Serialization_Test();
	}

	private void robotStateContract_Serialization_Test() {
		RobotStateContract rtcA = new RobotStateContract();
		rtcA.isCarryingBlock = true;
		rtcA.position = new Position(1, 5);
		String str = serializationController.Serialize(rtcA);
		RobotStateContract rtcB = serializationController.Deserialize(RobotStateContract.class, str);
		System.out.println("_____________________________");
		System.out.println(rtcB.isCarryingBlock);
		System.out.println(rtcB.position);
	}

	private void SerializatorsInitialization() {

		// RobotStateWriter
		RobotStateWriter writer = new RobotStateWriter();
		RobotStateReader reader = new RobotStateReader();
		serializationController.RegisterSerializator(RobotStateContract.class, writer, reader);

	}

	public void setup() {

	}
}
