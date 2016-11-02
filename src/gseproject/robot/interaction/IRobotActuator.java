package gseproject.robot.interaction;

import gseproject.Block;
import gseproject.grid.GridSpace;
import gseproject.infrastructure.ICallback;
import gseproject.infrastructure.interaction.IActuator;

public interface IRobotActuator extends IActuator {

    void move(GridSpace gridSpace);

    Block pick(GridSpace blockPosition);

    void drop(GridSpace dropPosition);

    void doWork(GridSpace workPosition);

}
