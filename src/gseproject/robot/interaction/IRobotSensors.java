package gseproject.robot.interaction;

import com.sun.org.apache.xpath.internal.operations.Bool;
import gseproject.IGridSpace;

/**
 * Created by Metelev Gregory on 02/11/2016.
 */
public interface IRobotSensors {

    public void init();

    /** ToDo: This method should return IBlock */
    public Bool isInGrid(IGridSpace Grid);

    public Bool didPickBlock(IGridSpace Grid);
}
