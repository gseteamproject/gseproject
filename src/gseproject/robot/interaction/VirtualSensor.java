package gseproject.robot.interaction;


import gseproject.robot.domain.RobotStateDto;

public class VirtualSensor extends AbstractSensor {

    @Override
    public RobotStateDto updateStatement() {
        //getting info from cache (only for virtual sensors ofc)
        //or any other ideas how to get information about robots state?
        return null;
    }

}
