package gseproject.robot.controller;


import java.awt.Color;

import gseproject.core.Block;
import gseproject.grid.Position;

public interface IController {

    public void move();

    /**
     * The robot picks the block.
     */
    public boolean pick();

    /**
     * The robot drops his block.
     */
    public boolean drop();

    /**
     * The robot processes the block e.g. clean or paint.
     */
    public boolean doWork();

    /**
     * GETTER
     * @return current position
     */
    public Position getCurrentPosition();
}
