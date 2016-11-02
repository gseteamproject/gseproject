package gseproject.robot.interaction;

import gseproject.IGridSpace;
import gseproject.robot.domain.RobotStateDto;

public class RealSensor extends AbstractSensor {

    @Override
    public RobotStateDto updateStatement() {
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