package gseproject.robot.interaction;

import gseproject.IGridSpace;

/**
 * Created by Metelev Gregory on 02/11/2016.
 */
public interface IRobotActions {

    public void init();

    public void move (IGridSpace nextGrid);

    /** ToDo: This method should return IBlock */
    public void pick (IGridSpace pickPos);

    public void drop (IGridSpace dropPos);
    public void doWork (IGridSpace doWorkPos);

}
