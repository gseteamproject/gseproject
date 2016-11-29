package gseproject.robot.controller;

import gseproject.core.Block;
import gseproject.core.grid.GridSpace;
import gseproject.robot.interaction.RealActuator;
import gseproject.robot.interaction.VirtualActuator;
import gseproject.robot.processing.IProcessor;

public class DummyController implements IController {

    /** Processor */
    private IProcessor _processor;
    private GridSpace CurrentPosition;

    /** Actuators */
    private VirtualActuator _virtActuator;
    private RealActuator _realActuator;

    /** Sensors */

    /* Default Constructor */
    public DummyController(){
        _virtActuator = new VirtualActuator();
        _processor = null;
    }
    /* Move Forward */
    public void move(GridSpace position) {
        _virtActuator.move(position);
        CurrentPosition = position;
        return;
    }

    /* Pick Block */
    public Block pick(GridSpace position) {
        _virtActuator.pick(position);
        CurrentPosition = position;
        return null;
    }

    /* Put block on Floor/Pallete */
    public void drop(GridSpace dropPosition) {
        _virtActuator.drop(dropPosition);
        CurrentPosition = dropPosition;
        return;
    }

    /* Do Clean or Paint Work */
    public void doWork(GridSpace workPosition) {
        _virtActuator.doWork(workPosition);
        CurrentPosition = workPosition;
        return;
    }

    /* Return Current Position of Robot */
    public GridSpace getCurrentPosition(){
        return CurrentPosition;
    }
}
