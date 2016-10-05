package gseproject.robot.interaction;


import gseproject.robot.domain.StateDto;

public class VirtualSensor extends AbstractSensor {

    @Override
    public StateDto updateStatement() {
        //getting info from repository (only for virtual sensors ofc)
        //or any other ideas how to get information about robots state?
        return null;
    }

}
