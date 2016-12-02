package gseproject.robot;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

import javax.xml.parsers.ParserConfigurationException;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAException;
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
import jade.core.behaviours.CyclicBehaviour;


public class RobotAgent extends Agent {
	private static final long serialVersionUID = -8771094154231916562L;
	private IController _controller;
	private ICommunicator _communicator;
	private IRobotToStationComm _robotToStationCommunicator;
	private RobotState _state;
	private SkillsSettings _skillsSettings;
	private AID[] _broadcastAddr;
	private DatagramSocket _udpSocket;

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
		b.addSubBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				DatagramPacket pack = new DatagramPacket(new byte[1], 1);
                try {
					if(_udpSocket != null)
					{
						_udpSocket.receive(pack);
					}
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("UDP Socket failed to recieve byte array ");
                }
                if(pack.getLength() != 0)
                {
                    ByteBuffer bb = ByteBuffer.wrap(pack.getData());
                    /** TODO: CHANGE FUCKING COLOR */
                    //_state.COLOR = bb.getInt(); !!!
                }
            }
		});

		/*
		 * Start Role behaviours
		 */
		if (this._skillsSettings._robotID.equals("Transporter")) {

			/*
			 *  Init Sockets
			 */
			initSockets();

			b.addSubBehaviour(new TransporterBehaviour(_controller, _robotToStationCommunicator, _state, this));
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

	private void initSockets() {
		int port = 34567;
        InetAddress addr = null;

        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();

        }

        try {
            _udpSocket = new DatagramSocket(port, addr);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            _udpSocket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void broadCastColor(Color color) {
        byte[] array = new byte[1];
        /* TODO: THIS FUNCTION SHOULD BE CALLED FROM TRANSPORT BEHAVIOUR */
        array[0] = 0x02;
        DatagramPacket packet = new DatagramPacket(array, 1);
        try {
            _udpSocket.send(packet);
        } catch (IOException e) {
            System.out.println("Broadcasting color was failed.");
            e.printStackTrace();
        }
    }
}