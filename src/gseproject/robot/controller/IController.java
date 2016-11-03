package gseproject.robot.controller;


import gseproject.Block;
import gseproject.grid.GridSpace;
import gseproject.robot.processing.IRobotProcessor;

public interface IController {

    /* Connect *this with Processor */
    public void connect(IRobotProcessor Processor);

    /* Move Forward */
    public void move(GridSpace position);

    /* Pick Block */
    public Block pick(GridSpace position);

    /* Put block on Floor/Pallete */
    public void drop(GridSpace dropPosition);

    /* Do Clean or Paint Work */
    public void doWork(GridSpace workPosition);

    /* Return current Position */
    public GridSpace getCurrentPosition();
}
