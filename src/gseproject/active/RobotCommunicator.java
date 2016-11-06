package gseproject.active;

import java.util.List;

import gseproject.core.IAgentToGUIComm;
import gseproject.core.grid.IGridSpace;
import gseproject.core.State;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.*;

public class RobotCommunicator extends Agent implements IAgentToGUIComm, IRobotToRobotComm, IRobotToStationComm {

	private Robot robot;
	private List<Behaviour> behaviours;
	private static final long serialVersionUID = 669015027861824282L;

	@Override
	public Object receiveReply() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void informState(Object info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void broadCastPosition(IGridSpace Position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void informAboutBestRobot(AID bestRobot, List<AID> allRobots) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendService() {
		// TODO Auto-generated method stub

	}

	@Override
	public IGridSpace receivePosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void informGUIState(State State) {
		// TODO Auto-generated method stub

	}

}
