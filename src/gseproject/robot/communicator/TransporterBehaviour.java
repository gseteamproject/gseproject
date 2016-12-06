package gseproject.robot.communicator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.stream.Collectors;

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

	private IRobotToStationComm _robotToStationCommunicator;
	private RobotState _state;
	private RobotAgent _agent;
	private DatagramSocket _udpSocket;
	private InetAddress myAddress;
	private InetAddress broadcast;

	public TransporterBehaviour(IRobotToStationComm robotToStationComm, RobotState robotState, RobotAgent agent) {
		this._robotToStationCommunicator = robotToStationComm;
		this._state = robotState;
		this._agent = agent;
		initUDP();
	}
	
	private void initUDP(){
		try {
			myAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
			String[] splitAddress = myAddress.getHostAddress().split("\\.");
			splitAddress[3] = "255";
			broadcast = InetAddress.getByName(Arrays.asList(splitAddress).stream().collect(Collectors.joining(".")));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		initSocket();
	}

	private void initSocket() {
		/** Init Socket for listening and sending */
		int port = 34567;

		try {
			this._udpSocket = new DatagramSocket(port, myAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		try {
			this._udpSocket.setBroadcast(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		System.out.println("init udp with: " + this.myAddress + ", " + this.broadcast);

	}

	private void moveAndgetBlockFromSourcePalette() {
		// _controller.move(sourcePaletteColor);
		// _agent.broadCastColor(sourcePaletteColor);
		//this.sendPosition((byte) 2);
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

	private void moveAndDropBlockOnCleaningFloor() {
		// _controller.move(cleaningFloorColor); // 2. move to cleaning floor
		// _agent.broadCastColor(cleaningFloorColor);
		//this.sendPosition((byte) 6);
		_robotToStationCommunicator.giveDirtyBlock(_state.block);
		ACLMessage reply = _robotToStationCommunicator.receiveReply();
		if (reply.getPerformative() == ACLMessage.INFORM) {
			_state.block.Status = Block.possibleBlockStatus.NULL;
			_state.isCarryingBlock = false;
		} else {
			System.out.println("dropping failed due to communication");
		}
	}

	private void moveAndDropBlockOnPaintingFloor() {
		// _controller.move(paintingFloorColor); // 2. move to cleaning floor
		// _agent.broadCastColor(paintingFloorColor);
		//this.sendPosition((byte) 9);
		_robotToStationCommunicator.giveCleanedBlock(_state.block);
		ACLMessage reply = _robotToStationCommunicator.receiveReply();
		if (reply.getPerformative() == ACLMessage.INFORM) {
			_state.block.Status = Block.possibleBlockStatus.NULL;
			_state.isCarryingBlock = false;
		} else {
			// He sent failure -> exception handling
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

	private void moveAndDropBlockOnGoalPalette() {
		// _controller.move(goalPaletteColor); // 2. move to cleaning floor
		// _agent.broadCastColor(goalPaletteColor);
		//this.sendPosition((byte) 13);
		_robotToStationCommunicator.givePaintedBlock(_state.block);
		ACLMessage reply = _robotToStationCommunicator.receiveReply();
		if (reply.getPerformative() == ACLMessage.INFORM) {
			_state.block.Status = Block.possibleBlockStatus.NULL;
			_state.isCarryingBlock = false;
		} else {
			System.out.println("dropping failed");
		}

	}

	private byte receivePosition() {
		DatagramPacket pack = new DatagramPacket(new byte[1], 1);

		byte position = (byte) 255;
		try {
			if (this._udpSocket != null) {
				System.out.println("receiving");
				this._udpSocket.receive(pack);
				if (pack.getAddress().equals(myAddress)) {
					return receivePosition();
				}
				position = pack.getData()[0];
				System.out.println(" Package Received");
				return position;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("UDP Socket failed to recieve byte array ");
		}
		return position;
	}

	private void sendPosition(byte goalPosition) {
		byte[] array = new byte[1];
		array[0] = goalPosition;
		DatagramPacket packet = new DatagramPacket(array, 1);
		InetAddress addr = null;
		addr = this.broadcast;
		try {
			packet.setPort(34567);
			packet.setAddress(addr);
			System.out.println("try sending");
			_udpSocket.send(packet);
			System.out.println("sent");
		} catch (IOException e) {
			System.out.println("Broadcasting color was failed.");
			e.printStackTrace();
		}
	}

	@Override
	public void action() {
		/*
		System.out.println("Workflow start:");
		System.out.println("Getting a Block from SourcePalette.");
		this.moveAndgetBlockFromSourcePalette();
		this._agent.doWait(2000);
		System.out.println("done.");
		System.out.println(this._agent.getAID().getLocalName() + " State: " + this._state + "\n");
		
		System.out.println("Dropping block on cleaning floor.");
		this.moveAndDropBlockOnCleaningFloor();
		this._agent.doWait(2000);
		System.out.println("done.");
		System.out.println(this._agent.getAID().getLocalName() + " State: " + this._state + "\n");
		
		System.out.println("Waiting until block is cleaned.");
		this.waitAndGetCleanedBlock();
		System.out.println("Got cleaned block.");
		System.out.println(this._agent.getAID().getLocalName() + " State: " + this._state + "\n");

		System.out.println("Dropping block on painting floor.");
		this.moveAndDropBlockOnPaintingFloor();
		this._agent.doWait(2000);
		System.out.println("done.");
		System.out.println(this._agent.getAID().getLocalName() + " State: " + this._state + "\n");
		
		System.out.println("Waiting until block is painted.");
		this.waitAndGetPaintedBlock();
		System.out.println("got painted block.");
		System.out.println(this._agent.getAID().getLocalName() + " State: " + this._state + "\n");
		
		System.out.println("Dropping block on goal palette.");
		this.moveAndDropBlockOnGoalPalette();
		this._agent.doWait(2000);
		System.out.println(this._agent.getAID().getLocalName() + " State: " + this._state + "\n");
		*/
		
		byte physicalrobotPosition = receivePosition();
		System.out.println("received position from robot:" + physicalrobotPosition);
		switch (physicalrobotPosition) {
		case 0:
			this.moveAndgetBlockFromSourcePalette();
			this._state._position = physicalrobotPosition;
			System.out.println(this._state);
			break;
		case 2:
			this.moveAndDropBlockOnCleaningFloor();
			this.waitAndGetCleanedBlock();
			this._state._position = physicalrobotPosition;
			System.out.println(this._state);
			break;
		case 6:
			this.moveAndDropBlockOnPaintingFloor();
			this.waitAndGetPaintedBlock();
			this._state._position = physicalrobotPosition;
			System.out.println(this._state);
			break;
		case 9:
			this.moveAndDropBlockOnGoalPalette();
			this._state._position = physicalrobotPosition;
			System.out.println(this._state);
			break;
		case 13:
			this.sendPosition((byte) 1);
			this._state._position = physicalrobotPosition;
			System.out.println(this._state);
			break;
		default:
			if (physicalrobotPosition < 17) {
				this._state._position = physicalrobotPosition;
			}
			System.out.println(this._state);
		}
		
	}
}
