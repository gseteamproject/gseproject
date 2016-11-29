package gseproject.robot;

import gseproject.core.Block;
import gseproject.core.ICallbackArgumented;
import gseproject.core.ServiceType;
import gseproject.core.grid.Position;
import gseproject.core.interaction.IActuator;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.robot.communicator.DummyCommunicator;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.communicator.IRobotToStationComm;
import gseproject.robot.communicator.RobotFloorInitiator;
import gseproject.robot.communicator.RobotSourcePaletteInitiator;
import gseproject.robot.communicator.RobotToStationCommunicator;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import gseproject.robot.domain.RobotState;
import gseproject.robot.skills.ISkill;
import gseproject.robot.skills.TransportSkill;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RobotAgent extends Agent {

	private IController _controller;
	private ICommunicator _communicator;
	private IRobotToStationComm robotToStationCommunicator;
	private List<ISkill> _skills;
	private IActuator _actuator;
	private RobotState _state;

	private void initState() {
		this._state = new RobotState();
		_state.block = new Block();
		_state.isCarryingBlock = false;
		_state.position = new Position(1, 1);
	}

	private void initCommunicators() {
		this.robotToStationCommunicator = new RobotToStationCommunicator(new AID("SourcePalette", AID.ISLOCALNAME),
				new AID("CleaningFloor", AID.ISLOCALNAME), new AID("PaintingFloor", true), new AID("GoalPalette", true),
				this);
	}

	private void move(String destination) {
		System.out.println("moving to " + destination + " ...");
		this.doWait(1000);
		System.out.println("arrived");
	}

	private void pick() {
		System.out.println("picking block");
		this.doWait(1000);
	}

	private void give() {
		System.out.println("giving block");
		this.doWait(1000);
	}

	public void setup() {
		initState();
		initCommunicators();
		move("SourcePalette");
		getDirtyBlock();
		move("CleaningFloor");
		giveDirtyBlock();
	}

	private void getDirtyBlock() {
		robotToStationCommunicator.requestDirtyBlock();
		ACLMessage reply = robotToStationCommunicator.receiveReply();
		if (reply.getPerformative() == ACLMessage.INFORM) {
			try {
				pick();
				this._state.block = ((Block) reply.getContentObject());
				this._state.isCarryingBlock = true;
				System.out.println("finished picking. New state=" + _state);
			} catch (UnreadableException e) {
				e.printStackTrace();
				takeDown("1");
			}
		} else {
			takeDown("1");
		}
	}

	private void giveDirtyBlock() {
		robotToStationCommunicator.giveDirtyBlock(_state.block);
		ACLMessage reply = robotToStationCommunicator.receiveReply();
		if (reply.getPerformative() == ACLMessage.INFORM) {
			give();
			_state.block = new Block();
			_state.isCarryingBlock = false;
			System.out.println("finished giving. New state=" + _state);
		} else {
			takeDown("1");
		}
	}

	public void takeDown(String errorCode) {
		System.out.println("Robot Agent shut down with errorcode=" + errorCode);
	}

}
