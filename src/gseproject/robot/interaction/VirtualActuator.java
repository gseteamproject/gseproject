package gseproject.robot.interaction;

import gseproject.Block;
import gseproject.grid.GridSpace;

public class VirtualActuator implements IRobotActuator {

    @Override
    public void init() {

    }

    @Override
    /* Return TRUE if Ok, and FALSE if fails */
    public boolean move(GridSpace gridSpace) {
        return true;
    }

    @Override
    /* Return Block instance if Ok, and null if fails */
    public Block pick(GridSpace blockPosition) {
        return null;
    }

    @Override
    /* Return TRUE if Ok, and FALSE if fails */
    public boolean drop(GridSpace dropPosition) {

        return true;
    }

    @Override
    /* Return TRUE if Ok, and FALSE if fails */
    public boolean doWork(GridSpace workPosition) {

        return true;
    }
}