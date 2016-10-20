package gseproject.robot;

import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.robot.domain.TransportSkillBusinessObject;
import gseproject.robot.interaction.AbstractActuator;
import gseproject.robot.interaction.AbstractSensor;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import jade.core.Agent;

public class RobotAgent extends Agent {

    private AbstractActuator _actuator;
    private AbstractSensor _sensor;
    private final SerializationController serializationController;

    public RobotAgent() {
	serializationController = SerializationController.Instance;
	SerializatorsInitialization();
	robotStateContract_Serialization_Test();
	transportSkillBO_Serialization_Test();
    }

    private void robotStateContract_Serialization_Test() {
	RobotStateContract rtcA = new RobotStateContract();
	rtcA.isCarryingBlock = true;
	rtcA.position = 65f;
	String str = serializationController.Serialize(rtcA);
	RobotStateContract rtcB = serializationController.Deserialize(RobotStateContract.class, str);
	System.out.println("_____________________________");
	System.out.println(rtcB.isCarryingBlock);
	System.out.println(rtcB.position);
    }

    private void transportSkillBO_Serialization_Test() {
	TransportSkillBusinessObject tsbo = new TransportSkillBusinessObject();
	System.out.println("Expected: " + tsbo.toString());
	String str = serializationController.Serialize(tsbo);
	System.out.println("serialized: " + str);
	TransportSkillBusinessObject tsbo2 =  serializationController.Deserialize(TransportSkillBusinessObject.class, str);
	System.out.println("Actual: " + tsbo2.toString());
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
