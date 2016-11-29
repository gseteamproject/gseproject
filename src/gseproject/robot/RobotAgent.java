package gseproject.robot;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import gseproject.core.Block;
import gseproject.core.grid.Position;
import gseproject.core.interaction.IActuator;
import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.communicator.IRobotToStationComm;
import gseproject.robot.communicator.RobotToStationCommunicator;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import gseproject.robot.domain.RobotState;
import gseproject.robot.skills.ISkill;
import gseproject.robot.skills.SkillsSettings;
import jade.core.AID;
import jade.core.Agent;
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
		String executionPath = System.getProperty("user.dir") + "/SmartMASON_Settings/SkillsSettings.xml";
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
		_controller = new DummyController();
	}

	private void initState() {
		this._state = new RobotState();
		_state.block = new Block();
		_state.isCarryingBlock = false;
		_state.position = new Position(1, 1);
	}

	private void initCommunicators() {
		this._robotToStationCommunicator = new RobotToStationCommunicator(new AID("SourcePalette", AID.ISLOCALNAME),
				new AID("CleaningFloor", AID.ISLOCALNAME), new AID("PaintingFloor", true), new AID("GoalPalette", true),
				this);
	}

	public void setup() {
		loadSkillSettings();
		initState();
		initCommunicators();
		
		_controller.move(new Position(1,1));

	}

	private void getDirtyBlock() {
		_robotToStationCommunicator.requestDirtyBlock();
		ACLMessage reply = _robotToStationCommunicator.receiveReply();
		if (reply.getPerformative() == ACLMessage.INFORM) {
			try {
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
		_robotToStationCommunicator.giveDirtyBlock(_state.block);
		ACLMessage reply = _robotToStationCommunicator.receiveReply();
		if (reply.getPerformative() == ACLMessage.INFORM) {
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
