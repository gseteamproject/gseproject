package gseproject.active;

import gseproject.*;
import jade.core.AID;
import java.util.*;


public interface IRobotToRobotComm {
	
	public void broadCastPosition(IGridSpace Position);
	public void informAboutBestRobot(AID bestRobot, List<AID> allRobots);
	public void sendService();
	public IGridSpace receivePosition();
}
