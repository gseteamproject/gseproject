package gseproject.robot.controller;


import gseproject.core.Block;
import gseproject.core.grid.Position;

public interface IController {

    /* Move Forward */
    public void move(Position position);

    /* Pick Block */
    public Block pick(Position position);

    /* Put block on Floor/Pallete */
    public void drop(Position dropPosition);

    /* Do Clean or Paint Work */
    public void doWork(Position workPosition);

    /* Return current Position */
    public Position getCurrentPosition();
}
