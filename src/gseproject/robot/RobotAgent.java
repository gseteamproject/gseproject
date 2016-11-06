package gseproject.robot;

import gseproject.robot.communicator.DummyCommunicator;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import gseproject.robot.processing.IProcessor;
import gseproject.robot.processing.DummyProcessor;

public class RobotAgent {

    private IProcessor _processor;

    private IController _controller;

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


        _processor = new DummyProcessor();
        _controller = new DummyController();
        _communicator = new DummyCommunicator();
    }

    public void setup(){

    }
}
