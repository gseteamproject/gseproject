package gseproject.robot.controller;

import gseproject.core.Block;
import gseproject.core.grid.GridSpace;
import gseproject.core.grid.Position;
import gseproject.robot.interaction.RealActuator;
import gseproject.robot.interaction.VirtualActuator;

public class DummyController implements IController {

    private Position _currentPosition;

    /** Actuators */
    private VirtualActuator _virtActuator;
    private RealActuator _realActuator;

    /** Sensors */

    /* Default Constructor */
    public DummyController(){
        _virtActuator = new VirtualActuator();
        _realActuator = new RealActuator();
    }
    /* Move Forward */
    public void move(Position position) {
        _virtActuator.move(position);
        _currentPosition = position;
        return;
    }

    /* Pick Block */
    public Block pick(Position position) {
        _virtActuator.pick(position);
        _currentPosition = position;
        return null;
    }

    /* Put block on Floor/Pallete */
    public void drop(Position dropPosition) {
        _virtActuator.drop(dropPosition);
        _currentPosition = dropPosition;
        return;
    }

    /* Do Clean or Paint Work */
    public void doWork(Position workPosition) {
        _virtActuator.doWork(workPosition);
        _currentPosition = workPosition;
        return;
    }

    /* Return Current Position of Robot */
    public Position getCurrentPosition(){
        return _currentPosition;
    }
}
