package gseproject.robot;

import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.robot.interaction.AbstractActuator;
import gseproject.robot.interaction.AbstractSensor;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import jade.core.Agent;

public class RobotAgent extends Agent {

    private AbstractActuator _actuator;
    private AbstractSensor _sensor;
    private final SerializationController serializationController;

    public RobotAgent(){
        serializationController = SerializationController.Instance;
        SerializatorsInitialization();

        RobotStateContract a = new RobotStateContract();
        a.isCarryingBlock = true;
        a.position =  65f;

        //Getting string like from FIPA protocol (msg.getContent())
        String str = serializationController.Serialize(a);

        //Getting real object from string that came from getContent method
        RobotStateContract b = serializationController.Deserialize(RobotStateContract.class, str);
        System.out.println("_____________________________");
        System.out.println(b.isCarryingBlock);
        System.out.println(b.position);

    }

    private void SerializatorsInitialization(){

        //RobotStateWriter
        RobotStateWriter writer = new RobotStateWriter();
        RobotStateReader reader = new RobotStateReader();
        serializationController.RegisterSerializator(RobotStateContract.class, writer, reader);

    }

    public void setup(){

    }
}
