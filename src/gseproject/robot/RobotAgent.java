package gseproject.robot;

import gseproject.core.grid.Position;
import gseproject.core.interaction.IActuator;
import gseproject.robot.communicator.DummyCommunicator;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import gseproject.robot.domain.RobotState;
import gseproject.robot.skills.ISkill;
import gseproject.robot.skills.TransportSkill;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

import java.util.List;

public class RobotAgent extends Agent {

    private IController _controller;
    private ICommunicator _communicator;
    private List<ISkill> _skills;
    private IActuator _actuator;
    private RobotState _state;

    public RobotAgent(){
        _communicator = new DummyCommunicator(this);
        _controller = new DummyController();

        _state = new RobotState();
        _state.isCarryingBlock = true;
        _state.position = new Position(5, 1);
    }

    public void setup(){
        TransportSkill transportSkill = new TransportSkill(_actuator);
        transportSkill.registerService(this);
        _skills.add(transportSkill);

        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                _communicator.notifyGridAgent(_state);
            }
        });
    }
}
