package gseproject.robot.interaction;

import gseproject.core.Block;
import gseproject.core.grid.Position;

public class RealActuator {

    public void init() {

    }

    /* Return TRUE if Ok, and FALSE if fails */
    public boolean move(Position gridSpace) {
        return true;
    }

    /* Return BLOCK if Ok, and null if fails */
    public Block pick(Position blockPosition) {
        return null;
    }

    /* Return TRUE if Ok, and FALSE if fails */
    public boolean drop(Position dropPosition) {
        return true;
    }

    /* Return TRUE if Ok, and FALSE if fails */
    public boolean doWork(Position workPosition) {
        return true;
    }
}