package gseproject.robot.processing;

import gseproject.SpaceType;
import gseproject.grid.Grid;
import gseproject.grid.GridSpace;
import jade.core.AID;

public class RobotDummyProcessor implements IRobotProcessor {

    private Grid _grid;

    private void RobotDummyProcessor(){}

    public void RobotDummyProcessor(Grid grid){
        _grid = grid;
    }

    public GridSpace[] buildWay(SpaceType spaceType, AID aid){
        return new GridSpace[0];
    }
}
