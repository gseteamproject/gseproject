package gseproject.robot.processing;

import gseproject.SpaceType;
import gseproject.grid.GridSpace;
import jade.core.AID;

public interface IRobotProcessor {

    GridSpace[] buildWay(SpaceType spaceType, AID aid);

}
