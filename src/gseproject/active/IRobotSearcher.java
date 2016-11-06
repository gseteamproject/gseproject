package gseproject.active;

import java.util.List;

import gseproject.core.ServiceType;
import jade.core.AID;



public interface IRobotSearcher {
	
	public List<AID> getAvailableRobots(ServiceType servicetype);
	public List<ServiceType> getServices(List<AID> robots);
	public AID getBestRobot(List<AID> robotSkills);
	
}
