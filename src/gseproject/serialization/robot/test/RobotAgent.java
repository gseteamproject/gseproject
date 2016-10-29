package gseproject.serialization.robot.test;

import gseproject.serialization.robot.test.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.robot.domain.TransportSkillBusinessObject;
import gseproject.robot.interaction.AbstractActuator;
import gseproject.robot.interaction.AbstractSensor;
import gseproject.serialization.robot.test.RobotStateReader;
import gseproject.serialization.robot.test.RobotStateWriter;
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
	rtcA.by = 127;
	rtcA.sh = 1000;
	rtcA.intt = 13999;
	rtcA.lng = 789456;
	rtcA.dbl = 10.001;
	rtcA.ch = 'G';
	String str = serializationController.Serialize(rtcA);
	RobotStateContract rtcB = serializationController.Deserialize(RobotStateContract.class, str);
	System.out.println("_____________________________");
	System.out.println(rtcB.isCarryingBlock);
	System.out.println(rtcB.position);
	System.out.println(rtcB.by);
	System.out.println(rtcB.sh);
	System.out.println(rtcB.intt);
	System.out.println(rtcB.lng);
	System.out.println(rtcB.dbl);
	System.out.println(rtcB.ch);
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
