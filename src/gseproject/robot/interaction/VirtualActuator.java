package gseproject.robot.interaction;

import gseproject.core.Block;
import gseproject.core.grid.GridSpace;

public class VirtualActuator {

    public void init() {

    }

    /* Return TRUE if Ok, and FALSE if fails */
    public boolean move(GridSpace gridSpace) {
        return true;
    }

    /* Return Block instance if Ok, and null if fails */
    public Block pick(GridSpace blockPosition) {
        return null;
    }

    /* Return TRUE if Ok, and FALSE if fails */
    public boolean drop(GridSpace dropPosition) {

        return true;
    }

    /* Return TRUE if Ok, and FALSE if fails */
    public boolean doWork(GridSpace workPosition) {

        return true;
    }
}