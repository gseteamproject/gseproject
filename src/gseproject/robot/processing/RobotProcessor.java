package gseproject.robot.processing;

import com.sun.org.apache.xpath.internal.operations.Bool;
import gseproject.IGridSpace;
import gseproject.grid.GridSpace;
import gseproject.robot.interaction.AbstractActuator;
import gseproject.robot.interaction.AbstractSensor;
import gseproject.robot.interaction.VirtualActuator;
import gseproject.robot.interaction.VirtualSensor;
import gseproject.robot.interaction.Direction;
import jade.lang.acl.ACLMessage;
import gseproject.SpaceType;

import java.util.List;

/**
 * Created by userauto on 02/11/2016.
 */
public class RobotProcessor implements IRobotProcessor {

    private AbstractActuator _actuator;
    private AbstractSensor _sensor;

    public RobotProcessor() {
        _actuator = new VirtualActuator();
        _sensor = new VirtualSensor();
    }

    @Override
    public void AddMessage(ACLMessage message) {

    }

    @Override
    public IGridSpace CalculatePath(IGridSpace position) {

        IGridSpace lGridSpace = new GridSpace(1,1, SpaceType.DEFAULT);

        return lGridSpace;
    }

    @Override
    public boolean GetTask(String task) {

        return true;
    }
    @Override
    public boolean GoToGrid(IGridSpace Grid) {

        _actuator.move(Direction.UP);

        if(_sensor.isInGrid(Grid) == false) {
            GoToGrid(Grid);
        };

        return true;
    }
}
