package gseproject.active;

import jade.core.AID;
import java.util.*;

import gseproject.grid.GridSpace;


public interface IRobotToRobotComm {
	
	public void broadCastPosition(GridSpace Position);
	public void informAboutBestRobot(AID bestRobot, List<AID> allRobots);
	public void sendService();
	public GridSpace receivePosition();
}
