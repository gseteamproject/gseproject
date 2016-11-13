package gseproject.robot;

import gseproject.core.interaction.IActuator;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import gseproject.robot.processing.IProcessor;
import gseproject.robot.processing.DummyProcessor;
import gseproject.robot.skills.ISkill;
import gseproject.robot.skills.TransportSkill;
import jade.core.Agent;

import java.util.List;

public class RobotAgent extends Agent {

    private IProcessor _processor;
    private IController _controller;
    private ICommunicator _communicator;
    private List<ISkill> _skills;
    private IActuator _actuator;

    public RobotAgent(){

        /*

        Scheme of Robot Agent Object hierarchy.

            RobotAgent
                |
          ------------------------
          |         |             |
  Communicator<-->Processor<--->Controller
        |                           |
 Jade Behaviours                --------------
                                |            |
                              Actuators     Sensors

         */


        _processor = new DummyProcessor();
        _controller = new DummyController();
    }

    public void setup(){
        _communicator = new DummyCommunicator(this);
        TransportSkill transportSkill = new TransportSkill(_actuator);
        transportSkill.registerService(this);
        _skills.add(transportSkill);


    }
}
