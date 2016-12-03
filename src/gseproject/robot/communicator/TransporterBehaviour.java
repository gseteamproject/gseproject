package gseproject.robot.communicator;

import gseproject.core.Block;
import gseproject.robot.controller.IController;
import gseproject.robot.domain.RobotState;
import gseproject.robot.RobotAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import gseproject.core.Color;

public class TransporterBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = -2309082371882678325L;

	private IController _controller;
	private IRobotToStationComm _robotToStationCommunicator;
	private RobotState _state;
	private RobotAgent _agent;

	public TransporterBehaviour(IController controller, IRobotToStationComm robotToStationComm, RobotState robotState, RobotAgent agent) {
		this._controller = controller;
		this._robotToStationCommunicator = robotToStationComm;
		this._state = robotState;
		this._agent = agent;
	}

	private void moveAndgetBlockFromSourcePalette(gseproject.core.Color sourcePaletteColor) {
		//_controller.move(sourcePaletteColor);
		System.out.println("TransporterBehaviour::moveAndgetBlockFromSourcePalette()");
		_agent.broadCastColor(sourcePaletteColor);
		if (_controller.pick()) {
			_robotToStationCommunicator.requestDirtyBlock();
			ACLMessage reply = _robotToStationCommunicator.receiveReply();
			if (reply.getPerformative() == ACLMessage.INFORM) {
				try {

					_state.block = (Block) reply.getContentObject();
					_state.isCarryingBlock = true;
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
			} else {
				// Exception handlung
			}
		}
	}

	private void moveAndDropBlockOnCleaningFloor(gseproject.core.Color cleaningFloorColor) {
		//_controller.move(cleaningFloorColor); // 2. move to cleaning floor
		_agent.broadCastColor(cleaningFloorColor);
		if (_controller.drop()) {
			_robotToStationCommunicator.giveDirtyBlock(_state.block);
			ACLMessage reply = _robotToStationCommunicator.receiveReply();
			if (reply.getPerformative() == ACLMessage.INFORM) {
				_state.block.Status = Block.possibleBlockStatus.NULL;
				_state.isCarryingBlock = false;
			} else {
				System.out.println("dropping failed due to communication");
			}
		} else {
			System.out.println("dropping failed due to real actuator");
		}
	}

	private void moveAndDropBlockOnPaintingFloor(gseproject.core.Color paintingFloorColor) {
		//_controller.move(paintingFloorColor); // 2. move to cleaning floor
		_agent.broadCastColor(paintingFloorColor);
		if (_controller.drop()) {
			_robotToStationCommunicator.giveCleanedBlock(_state.block);
			ACLMessage reply = _robotToStationCommunicator.receiveReply();
			if (reply.getPerformative() == ACLMessage.INFORM) {
				_state.block.Status = Block.possibleBlockStatus.NULL;
				_state.isCarryingBlock = false;
			} else {
				// He sent failure -> exception handling
			}
		}
	}

	private void waitAndGetCleanedBlock() {
		_robotToStationCommunicator.requestCleanedBlock();
		ACLMessage reply = _robotToStationCommunicator.receiveReply();
		while (reply.getPerformative() == ACLMessage.FAILURE) {
			myAgent.doWait(1000);
			_robotToStationCommunicator.requestCleanedBlock();
			reply = _robotToStationCommunicator.receiveReply();
		}
		try {
			_state.block = (Block) reply.getContentObject();
			_state.isCarryingBlock = true;
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}

	private void waitAndGetPaintedBlock() {
		_robotToStationCommunicator.requestPaintedBlock();
		ACLMessage reply = _robotToStationCommunicator.receiveReply();
		while (reply.getPerformative() == ACLMessage.FAILURE) {
			myAgent.doWait(1000);
			_robotToStationCommunicator.requestCleanedBlock();
			reply = _robotToStationCommunicator.receiveReply();
		}
		try {
			_state.block = (Block) reply.getContentObject();
			_state.isCarryingBlock = true;
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}

	private void moveAndDropBlockOnGoalPalette(gseproject.core.Color goalPaletteColor) {
		//_controller.move(goalPaletteColor); // 2. move to cleaning floor
		_agent.broadCastColor(goalPaletteColor);
		if (_controller.drop()) {
			_robotToStationCommunicator.givePaintedBlock(_state.block);
			ACLMessage reply = _robotToStationCommunicator.receiveReply();
			if (reply.getPerformative() == ACLMessage.INFORM) {
				_state.block.Status = Block.possibleBlockStatus.NULL;
				_state.isCarryingBlock = false;
			} else {
				System.out.println("dropping failed");
			}
		}
	}

	@Override
	public void action() {

		moveAndgetBlockFromSourcePalette(gseproject.core.Color.BLACK);
		System.out.println("got dirty block");
		this.myAgent.doWait(2000);
		moveAndDropBlockOnCleaningFloor(Color.BLACK);
		System.out.println("dropped block on cleaning floor");
		this.myAgent.doWait(2000);
		waitAndGetCleanedBlock();
		System.out.println("got cleaned block");
		this.myAgent.doWait(2000);
		moveAndDropBlockOnPaintingFloor(Color.BLACK);
		System.out.println("dropped block on painting floor");
		this.myAgent.doWait(2000);
		waitAndGetPaintedBlock();
		System.out.println("got painted block");
		this.myAgent.doWait(2000);
		moveAndDropBlockOnGoalPalette(Color.BLACK);
		this.myAgent.doWait(2000);
		System.out.println("dropped block on goal palette");
	}
}
