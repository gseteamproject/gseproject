package gseproject.robot.communicator;

import java.awt.Color;

import gseproject.core.Block;
import gseproject.robot.controller.IController;
import gseproject.robot.domain.RobotState;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class TransporterBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = -2309082371882678325L;

	private IController _controller;
	private IRobotToStationComm _robotToStationCommunicator;
	private RobotState _state;

	public TransporterBehaviour(IController controller, IRobotToStationComm robotToStationComm, RobotState robotState) {
		this._controller = controller;
		this._robotToStationCommunicator = robotToStationComm;
		this._state = robotState;
	}

	private void moveAndgetBlockFromSourcePalette(Color sourcePaletteColor) {
		_controller.move(sourcePaletteColor);
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

	private void moveAndDropBlockOnCleaningFloor(Color cleaningFloorColor) {
		_controller.move(cleaningFloorColor); // 2. move to cleaning floor
		if (_controller.drop()) {
			_robotToStationCommunicator.giveDirtyBlock(_state.block);
			ACLMessage reply = _robotToStationCommunicator.receiveReply();
			if (reply.getPerformative() == ACLMessage.INFORM) {
				_state.block.Status = Block.possibleBlockStatus.NULL;
				_state.isCarryingBlock = false;
			} else {
				// He sent failure -> exception handling
			}
		}
	}

	private void moveAndDropBlockOnPaintingFloor(Color paintingFloorColor) {
		_controller.move(paintingFloorColor); // 2. move to cleaning floor
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

	private void moveAndDropBlockOnGoalPalette(Color goalPaletteColor) {
		_controller.move(goalPaletteColor); // 2. move to cleaning floor
		if (_controller.drop()) {
			_robotToStationCommunicator.givePaintedBlock(_state.block);
			ACLMessage reply = _robotToStationCommunicator.receiveReply();
			if (reply.getPerformative() == ACLMessage.INFORM) {
				_state.block.Status = Block.possibleBlockStatus.NULL;
				_state.isCarryingBlock = false;
			} else {
				// He sent failure -> exception handling
			}
		}
	}

	@Override
	public void action() {
		moveAndgetBlockFromSourcePalette(Color.black);
		moveAndDropBlockOnCleaningFloor(Color.black);
		waitAndGetCleanedBlock();
		moveAndDropBlockOnPaintingFloor(Color.black);
		waitAndGetPaintedBlock();
		moveAndDropBlockOnGoalPalette(Color.black);
	}
}
