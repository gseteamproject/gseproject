package gseproject.robot;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import gseproject.robot.communicator.ICommunicator;
import gseproject.robot.communicator.IRobotToStationComm;
import gseproject.robot.communicator.RobotToStationCommunicator;
import gseproject.robot.communicator.TransporterBehaviour;
import gseproject.robot.communicator.WorkerBehaviour;
import gseproject.robot.controller.DummyController;
import gseproject.robot.controller.IController;
import gseproject.robot.domain.RobotState;
import gseproject.robot.skills.SkillsSettings;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class RobotAgent extends Agent {
	private static final long serialVersionUID = -8771094154231916562L;
	private IController _controller;
	private ICommunicator _communicator;
	private IRobotToStationComm _robotToStationCommunicator;
	private RobotState _state;
	private SkillsSettings _skillsSettings;

	private void loadSkillSettings() {
		_skillsSettings = new SkillsSettings();
		String executionPath = System.getProperty("user.dir") + "/SmartMASON_Settings/SkillsSettings.xml";
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
		_controller = new DummyController(_skillsSettings);
	}

	private void loadStateSettings() {
		_state = new RobotState();
		String executionPath = System.getProperty("user.dir") + "/SmartMASON_Settings/StateSettings.xml";
		try {
			_state.xmlDocumentDecode(executionPath);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	private void initCommunicators() {
		this._robotToStationCommunicator = new RobotToStationCommunicator(new AID("SourcePalette@192.168.111.1", AID.ISLOCALNAME),
				new AID("CleaningFloor@192.168.111.1", AID.ISLOCALNAME), new AID("PaintingFloor@192.168.111.1", AID.ISLOCALNAME),
				new AID("GoalPalette@192.168.111.1", AID.ISLOCALNAME), this);
	}

	public void setup() {
		System.out.println(this.getAID() + "started");
		/*
		 * Load settings
		 */
		loadSkillSettings();
		loadStateSettings();

		/*
		 * Initiate Controller, Communicator and Behaviours
		 */
		initController();
		initCommunicators();

		this._skillsSettings.getAID(this.getAID().getLocalName());
		ParallelBehaviour b = new ParallelBehaviour();
		b.addSubBehaviour(new TickerBehaviour(this, 10000) {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6128539874456834232L;

			@Override
			protected void onTick() {
				System.out.println(this.myAgent.getAID().getLocalName() + " Current State: " + _state + "\n Later this will be sent to GUI \n");
			}

		});
		if (this._skillsSettings._robotID.equals("Transporter")) {
			b.addSubBehaviour(new TransporterBehaviour(_controller, _robotToStationCommunicator, _state));
		} else if (this._skillsSettings._robotID.equals("Cleaner")) {
			_robotToStationCommunicator.requestOccupyCleaningFloor();
			ACLMessage reply = _robotToStationCommunicator.receiveReply();
			if (reply.getPerformative() == ACLMessage.INFORM) {
				System.out.println("successfully occupied cleaning floor");
			} else {
				System.out.println("failed occupy cleaning floor");
			}
			b.addSubBehaviour(
					new WorkerBehaviour(_robotToStationCommunicator, _controller, _state, "needClean", "needClean"));
		} else if (this._skillsSettings._robotID.equals("Painter")) {
			_robotToStationCommunicator.requestOccupyPaintingFloor();
			ACLMessage reply = this._robotToStationCommunicator.receiveReply();
			if (reply.getPerformative() == ACLMessage.INFORM) {
				System.out.println("successfully occupied painting floor");
			} else {
				System.out.println("failed occupy painting floor");
			}
			b.addSubBehaviour(
					new WorkerBehaviour(_robotToStationCommunicator, _controller, _state, "needPaint", "needPaint"));
		} else {
			System.out.println("something went wrong");
		}
		this.addBehaviour(b);
	}

	public void takeDown(String errorCode) {
		System.out.println("Robot Agent shut down with errorcode=" + errorCode);
	}

}