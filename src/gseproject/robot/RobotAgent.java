package gseproject.robot;

import gseproject.infrastructure.contracts.RobotSkillContract;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotSkillReader;
import gseproject.infrastructure.serialization.robot.RobotSkillWriter;
import gseproject.robot.skills.TransportSkill;
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

    private void robotSkillContract_Serialization_Test() {
        TransportSkill tsbo = new TransportSkill();
        System.out.println("Expected: " + tsbo.toString());
        RobotSkillContract skillContract = new RobotSkillContract();
        skillContract.cost = tsbo.getCost();
        skillContract.duration = tsbo.getDuration();
        String str = serializationController.Serialize(skillContract);
        System.out.println("serialized: " + str);
        RobotSkillContract skillContract2 = serializationController.Deserialize(RobotSkillContract.class, str);
        System.out.println("Actual: " + skillContract2.cost + " " + skillContract2.duration);
    }

    private void SerializatorsInitialization() {

        // RobotStateContract Serialization
        RobotStateWriter stateWriter = new RobotStateWriter();
        RobotStateReader stateReader = new RobotStateReader();
        serializationController.RegisterSerializator(RobotStateContract.class, stateWriter, stateReader);

        // RobotSkillContract Serialization
        RobotSkillWriter skillWriter = new RobotSkillWriter();
        RobotSkillReader skillReader = new RobotSkillReader();
        serializationController.RegisterSerializator(RobotSkillContract.class, skillWriter, skillReader);
    }

    protected void setup() {
        robotStateContract_Serialization_Test();
        robotSkillContract_Serialization_Test();
    }
}
