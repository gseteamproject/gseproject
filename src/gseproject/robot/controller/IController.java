package gseproject.robot.controller;


import java.awt.Color;

import gseproject.core.Block;
import gseproject.core.grid.Position;

public interface IController {

    /**
     * The Robot is moving to the color on the real grid.
     * @param color the color on the real grid.
     */
    public void move(Color color);

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
