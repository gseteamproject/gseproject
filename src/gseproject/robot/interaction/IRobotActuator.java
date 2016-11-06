package gseproject.robot.interaction;

import gseproject.core.Block;
import gseproject.core.grid.GridSpace;
import gseproject.core.interaction.IActuator;

public interface IRobotActuator extends IActuator {

    boolean move(GridSpace gridSpace);

    Block pick(GridSpace blockPosition);

    boolean drop(GridSpace dropPosition);

    boolean doWork(GridSpace workPosition);

}
