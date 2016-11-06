package gseproject.robot.interaction;

import gseproject.grid.Block;
import gseproject.grid.GridSpace;
import gseproject.infrastructure.interaction.IActuator;

public interface IRobotActuator extends IActuator {

    boolean move(GridSpace gridSpace);

    Block pick(GridSpace blockPosition);

    boolean drop(GridSpace dropPosition);

    boolean doWork(GridSpace workPosition);

}
