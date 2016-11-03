package gseproject.robot;

import gseproject.robot.communicator.DummyCommunicator;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import gseproject.robot.interaction.IRobotActuator;
import gseproject.robot.processing.IRobotProcessor;
import gseproject.robot.processing.RobotDummyProcessor;
import jade.core.Agent;

public class RobotAgent {

    IRobotActuator actuator;

    /* Processor related objects */
    private IRobotProcessor _processor;

    /* Controller related objects */
    private IController _controller;

    /* Communicator related objects */
    private ICommunicator _communicator;

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


        _processor = new RobotDummyProcessor();
        _controller = new DummyController();
        _communicator = new DummyCommunicator();

        /* Connect Objects according to the diagram above */
        _processor.connect(_controller);
        _controller.connect(_processor);
        _communicator.connect(_processor);
        _processor.connect(_communicator);

    }

    public void setup(){

    }
}
