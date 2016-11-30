package gseproject.robot;

import java.awt.Color;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import gseproject.core.Block;
import gseproject.core.grid.Position;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.communicator.IRobotToStationComm;
import gseproject.robot.communicator.RobotToStationCommunicator;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import gseproject.robot.domain.RobotState;
import gseproject.robot.skills.SkillsSettings;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class RobotAgent extends Agent {
	private static final long serialVersionUID = -8771094154231916562L;
	private IController _controller;
	private ICommunicator _communicator;
	private IRobotToStationComm _robotToStationCommunicator;
	private RobotState _state;
	private SkillsSettings _skillsSettings;

	private void loadSkillSettings() {
		_skillsSettings = new SkillsSettings();
		String executionPath = System.getProperty("user.dir") + "\\SmartMASON_Settings\\SkillsSettings.xml";
		System.out.println(executionPath);
		try {
			_skillsSettings.xmlDocumentDecode(executionPath);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	private void initController() {
		loadSkillSettings();
		_controller = new DummyController(_skillsSettings);
	}

	private void initState() {
		this._state = new RobotState();
		_state.block = new Block();
		_state.isCarryingBlock = false;
		_state._position = new Position(1, 1);
	}

	private void initCommunicators() {
		this._robotToStationCommunicator = new RobotToStationCommunicator(new AID("SourcePalette", AID.ISLOCALNAME),
				new AID("CleaningFloor", AID.ISLOCALNAME), new AID("PaintingFloor", true),
				new AID("GoalPalette", AID.ISLOCALNAME), this);
	}

	public void setup() {
		initController();
		initState();
		initCommunicators();
		ParallelBehaviour b = new ParallelBehaviour();
		b.addSubBehaviour(new TickerBehaviour(this, 500) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6128539874456834232L;

			@Override
			protected void onTick() {
				// broadcast state (receiver = all robots + gui)
			}

		});

		b.addSubBehaviour(new CyclicBehaviour(this) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4919825570106173333L;

			@Override
			public void action() {
				_controller.move(Color.black); // 1. move to sourcepalette
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
						// He sent failure -> exception handling
					}
				}
				_controller.move(Color.black); // 2. move to cleaning floor
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
				_robotToStationCommunicator.requestCleanedBlock();
				ACLMessage reply = _robotToStationCommunicator.receiveReply();
				while (reply.getPerformative() == ACLMessage.FAILURE) {
					System.out.println("got failure message");
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

		});
		this.addBehaviour(b);
	}

	public void takeDown(String errorCode) {
		System.out.println("Robot Agent shut down with errorcode=" + errorCode);
	}

}