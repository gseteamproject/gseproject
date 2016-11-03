package gseproject.robot.processing;

import gseproject.SpaceType;
import gseproject.grid.Grid;
import gseproject.grid.GridSpace;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import jade.core.AID;

public class RobotDummyProcessor implements IRobotProcessor {

    /** Grid */
    private Grid _grid;

    /** Controllers related objects */
    private IController _controller;

    /* Communicator related objects */
    private ICommunicator _communicator;

    /* Constructors */
    private void RobotDummyProcessor(){}
    public void RobotDummyProcessor(Grid grid){
        _grid = grid;
    }
    /* Connection Methods */
    public void connect(IController Controller){
        _controller = Controller;
    }
    public void connect(ICommunicator Communicator){
        _communicator = Communicator;
    }

    /* Set Default Grid */
    public void setDefaultGrid(Grid grid) { _grid = grid; }
    public GridSpace getCurrentPosition(){
        return _controller.getCurrentPosition();
    }



    public GridSpace[] buildWay(SpaceType spaceType, AID aid){
        return new GridSpace[0];
    }


}
