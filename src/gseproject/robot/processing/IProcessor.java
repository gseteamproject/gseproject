package gseproject.robot.processing;

import gseproject.grid.SpaceType;
import gseproject.grid.GridSpace;
import jade.core.AID;

public interface IProcessor {

    GridSpace[] buildWay(SpaceType spaceType, AID aid);

    GridSpace getCurrentPosition();

}
