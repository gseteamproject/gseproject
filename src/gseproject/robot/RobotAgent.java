package gseproject.robot;

import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.ContractsSerializationController;
import gseproject.robot.interaction.AbstractActuator;
import gseproject.robot.interaction.AbstractSensor;
import gseproject.robot.interaction.VirtualActuator;
import gseproject.robot.interaction.VirtualSensor;
import gseproject.robot.processing.IRobotProcessor;
import gseproject.robot.processing.RobotProcessor;
import gseproject.robot.serialization.RobotStateReader;
import gseproject.robot.serialization.RobotStateWriter;
import jade.core.Agent;

public class RobotAgent extends Agent {

    private IRobotProcessor _processor;
    private final ContractsSerializationController serializationController;

    public RobotAgent(){

        _processor = new RobotProcessor();
        serializationController = ContractsSerializationController.Instance;

        RobotStateContract a = new RobotStateContract();
        a.isCarryingBlock = true;
        a.position =  64f;

        RobotStateWriter writer = new RobotStateWriter();
        RobotStateReader reader = new RobotStateReader();

        serializationController.RegisterSerializator(a.getId(), writer, reader);

        String str = serializationController.Serialize(a);

        RobotStateContract b = serializationController.Deserialize(a.getId(), str);
        System.out.println("_____________________________");
        System.out.println(b.isCarryingBlock);
        System.out.println(b.position);

    }

    public void setup(){

    }
}
