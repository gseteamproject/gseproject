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
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;


public class RobotAgent extends Agent {
	private static final long serialVersionUID = -8771094154231916562L;
	private IController _controller;
	private IRobotToStationComm _robotToStationCommunicator;
	private RobotState _state;
	private SkillsSettings _skillsSettings;
	private AID[] _broadcastAddr;

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

	public void initCommunicators() {
		this._robotToStationCommunicator = new RobotToStationCommunicator(
				new AID("SourcePalette", AID.ISLOCALNAME),
				new AID("CleaningFloor", AID.ISLOCALNAME),
				new AID("PaintingFloor", AID.ISLOCALNAME),
				new AID("GoalPalette", AID.ISLOCALNAME), this);
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


		/*
		 * Register Service in DF
		 */
		registerDF();

		this._skillsSettings.getAID(this.getAID().getLocalName());

		/*
		 * Ticker behaviour for broadcasting state of robot
		 */

		ParallelBehaviour b = new ParallelBehaviour();

		/*
		 * Start Role behaviours
		 */
		if (this._skillsSettings._robotID.equals("Transporter")) {
			b.addSubBehaviour(new TransporterBehaviour(_robotToStationCommunicator, _state, this));

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

	private boolean findRobots() {
		boolean found = false;
		DFAgentDescription agentDescriptionTemplate = new DFAgentDescription();
		ServiceDescription serviceTransport = new ServiceDescription();
		serviceTransport.setType("Transporter");

		SearchConstraints searchConstraints = new SearchConstraints();
		searchConstraints.setSearchId("GUI");

		ServiceDescription serviceGUI = new ServiceDescription();
		serviceGUI.setType("GUI");

		agentDescriptionTemplate.addServices(serviceGUI);
		agentDescriptionTemplate.addServices(serviceTransport);
		try {
			DFAgentDescription[] foundRobots = DFService.search(this, agentDescriptionTemplate);
			_broadcastAddr = new AID[foundRobots.length];
			for (int i = 0; i < foundRobots.length; i++) {
				found = true;
				_broadcastAddr[i] = foundRobots[i].getName();
			}
		} catch (FIPAException exception) {
			exception.printStackTrace();
		}
		return found;
	}

	private void registerDF() {

		String agentServiceGroup = this._skillsSettings._robotID;
		String agentServiceName = new String("Transporter");

		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setName(agentServiceGroup);
		serviceDescription.setType(agentServiceName);
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		agentDescription.addServices(serviceDescription);
		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException exception) {
			exception.printStackTrace();
		}

	}
}