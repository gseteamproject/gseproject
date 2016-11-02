package gseproject.robot.interaction;


import gseproject.IGridSpace;
import gseproject.robot.domain.RobotStateDto;

public class VirtualSensor extends AbstractSensor {

    @Override
    public RobotStateDto updateStatement() {
        //getting info from cache (only for virtual sensors ofc)
        //or any other ideas how to get information about robots state?
        return null;
    }

    @Override
    public boolean isInGrid(IGridSpace Grid) {

        return false;
    }

    @Override
    public boolean didPickBlock(IGridSpace Grid) {

        return false;
    }
}
