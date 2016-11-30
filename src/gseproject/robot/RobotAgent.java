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
		this._robotToStationCommunicator = new RobotToStationCommunicator(new AID("SourcePalette", AID.ISLOCALNAME),
				new AID("CleaningFloor", AID.ISLOCALNAME), new AID("PaintingFloor", true),
				new AID("GoalPalette", AID.ISLOCALNAME), this);
	}

	public void setup() {
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
			

			}

		});
		this.addBehaviour(b);
	}

	public void takeDown(String errorCode) {
		System.out.println("Robot Agent shut down with errorcode=" + errorCode);
	}

}