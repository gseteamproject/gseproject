package gseproject.robot.controller;


import gseproject.core.Block;
import gseproject.core.grid.GridSpace;

public interface IController {

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