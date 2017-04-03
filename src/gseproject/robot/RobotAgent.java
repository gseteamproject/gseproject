package gseproject.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import gseproject.core.ServiceType;
import jade.lang.acl.MessageTemplate;
import org.xml.sax.SAXException;

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
	public AID[] _broadcastAddr;
	int _cleanRate = 0;
	int	_paintRate = 0;
	int	_transportRate = 0;



	/* Temporal attr for parsing and algorithm */
	private List<SkillsSettings> _tempSkills;

	private void loadSkillSettings() {
		_skillsSettings = new SkillsSettings();
		String executionPath = System.getProperty("user.dir") + "/" + this.getLocalName() + "_Settings" + "/SkillsSettings.xml";
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
		String executionPath = System.getProperty("user.dir") + "/" + this.getLocalName() + "_Settings" + "/StateSettings.xml";
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
		_tempSkills = new ArrayList<SkillsSettings>();

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
		 * 1) Register Raw Service in DF
		*/
		registerDFRaw();

		/*
		 * Find Agents with "Raw" Service
		 */
		findRobots();

		/*
		 * Exchange skill settings with other Agents
		 */
		exchangeSkills();

		/*
		 * Based on exchanged information - choose Role
		 */
		chooseRole();

		/*
		 * Start Role behaviours
		 */
		startRoleBehaviours();

	}  /* End of setup() */

	private void startRoleBehaviours(){
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		if (this._skillsSettings._robotID.equals("Transporter")) {
			startTransportBehaviour(parallelBehaviour);
		} else if (this._skillsSettings._robotID.equals("Cleaner")) {
			startCleanBehaviour(parallelBehaviour);
		} else if (this._skillsSettings._robotID.equals("Painter")) {
			startPaintBehaviour(parallelBehaviour);
		} else {
			System.out.println("Something Went Wrong");
		}
		this.addBehaviour(parallelBehaviour);
	}

	private void startTransportBehaviour(ParallelBehaviour behaviour)
	{
        behaviour.addSubBehaviour(new TransporterBehaviour(_robotToStationCommunicator, _state, this, _skillsSettings._mode));
    }

    private void startCleanBehaviour(ParallelBehaviour behaviour) {
        behaviour.addSubBehaviour(
                new WorkerBehaviour(_robotToStationCommunicator, _controller, _state, "needClean", "needClean", _skillsSettings._mode));
    }

    private void startPaintBehaviour(ParallelBehaviour behaviour) {
        behaviour.addSubBehaviour(
                new WorkerBehaviour(_robotToStationCommunicator, _controller, _state, "needPaint", "needPaint", _skillsSettings._mode));
    }


	private boolean findRobots() {
		boolean found = false;
		DFAgentDescription agentDescriptionTemplate = new DFAgentDescription();
		ServiceDescription serviceTransport = new ServiceDescription();
		serviceTransport.setType("Raw");

		SearchConstraints searchConstraints = new SearchConstraints();
		searchConstraints.setSearchId("Raw");

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
	private void registerDFRaw() {

		String agentServiceGroup = new String("Raw");
		String agentServiceName = new String("Raw");

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

	private void exchangeSkills() {

		String strMessage = _skillsSettings.CodeString();
		for(int i = 0; i != _broadcastAddr.length; ++i)
		{
			sendSkills(_broadcastAddr[i], strMessage);

			ACLMessage message = this.blockingReceive(MessageTemplate.MatchProtocol("Skills"));
			SkillsSettings skills = new SkillsSettings();
			skills.DecodeString(message.getContent());
			_tempSkills.add(skills);
			System.out.print("Robot received Skills message !\n");
		}
	}

	void sendSkills(AID receiver, String strContent )
	{
		ACLMessage messageSkill = new ACLMessage(ACLMessage.INFORM);
		messageSkill.addReceiver(receiver);
		messageSkill.setProtocol("Skills");
		messageSkill.setContent(strContent);
		this.send(messageSkill);
	}

	void chooseRole()
	{
		for(int i = 0; i < _tempSkills.size(); ++i)
		{
			if(_tempSkills.get(i).getDClean() > _skillsSettings.getDClean())
			{
				++_cleanRate;
			}
			if(_tempSkills.get(i).getDPaint() > _skillsSettings.getDPaint())
			{
				++_paintRate;
			}
			if(_tempSkills.get(i).getDTransport() > _skillsSettings.getDTransport())
			{
				++_transportRate;
			}
		}
		if((_cleanRate > _paintRate)
				&& (_cleanRate > _transportRate))
		{
			_skillsSettings._robotID = "Cleaner";
		}

		if((_paintRate > _cleanRate)
				&& (_paintRate > _transportRate))
		{
			_skillsSettings._robotID = "Painter";
		}

		if((_transportRate > _cleanRate)
				&& (_transportRate > _paintRate))
		{
			_skillsSettings._robotID = "Transporter";
		}

		return;
	}

}
