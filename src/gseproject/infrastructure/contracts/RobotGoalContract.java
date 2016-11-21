package gseproject.infrastructure.contracts;

import gseproject.core.grid.Position;
import jade.core.AID;

public class RobotGoalContract {

    public AID aid;

    public Position position;

    public RobotGoalContract(){}

    public RobotGoalContract(AID aid, Position position){
        this.aid = aid;
        this.position = position;
    }
}
