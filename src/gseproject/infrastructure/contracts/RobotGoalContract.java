package gseproject.infrastructure.contracts;

import gseproject.core.grid.Position;
import jade.core.AID;

import java.io.Serializable;

public class RobotGoalContract implements Serializable{

    public AID aid;

    public Position goal;

    public Position currentPosition;

    public RobotGoalContract(){}

    public RobotGoalContract(AID aid, Position currentPosition, Position goal){
        this.aid = aid;
        this.goal = goal;
        this.currentPosition = currentPosition;
    }
}
