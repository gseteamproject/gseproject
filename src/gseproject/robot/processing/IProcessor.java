package gseproject.robot.processing;

import gseproject.core.grid.SpaceType;
import gseproject.core.grid.GridSpace;
import jade.core.AID;

public interface IProcessor {

    GridSpace[] buildWay(SpaceType spaceType, AID aid);

    GridSpace getCurrentPosition();

}
