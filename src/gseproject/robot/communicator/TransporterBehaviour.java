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
import gseproject.core.ServiceType;
import gseproject.robot.domain.RobotState;
import gseproject.robot.RobotAgent;
import gseproject.robot.skills.SkillsSettings;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class TransporterBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = -2309082371882678325L;

	private IRobotToStationComm _robotToStationCommunicator;
	private RobotState _state;
	private RobotAgent _agent;
	private DatagramSocket _udpSocket;
	private InetAddress myAddress;
	private InetAddress broadcast;
	private int _mode;
	private boolean _isMoving = false;

	public TransporterBehaviour(IRobotToStationComm robotToStationComm, RobotState robotState, RobotAgent agent, int mode) {
		this._robotToStationCommunicator = robotToStationComm;
		this._state = robotState;
		this._agent = agent;
		this._mode = mode;
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

	/*
	 * True if collision predicted, false if not
	 */
	private boolean predictCollision(){

		for(int i = 0; i != _agent._broadcastAddr.length; ++i) {
			ACLMessage messageToRobots = new ACLMessage(ACLMessage.REQUEST);
			messageToRobots.addReceiver(_agent._broadcastAddr[i]);
			messageToRobots.setProtocol("RobotPosition");
			messageToRobots.setContent(String.valueOf(_state._position));
			_agent.send(messageToRobots);

			ACLMessage message = _agent.blockingReceive(MessageTemplate.MatchProtocol("RobotPosition"));
			int Position = Integer.valueOf(message.getContent());
			int factor = 0;
			if(_agent.getName().equals(message.getSender().getName())){
				continue;
			}
			for(int counter = _state._position; counter != Position; ++counter)
			{
				++factor;
				if(counter == 16)
				{
					counter = 0;
				}
			}
			if(factor < 2)
			{
				System.out.println("Collision predicted");
				return true;
			}
		}

		return  false;
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

	private byte moveAgent() {
		byte position = (byte) 255;



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

		if(predictCollision())
		{
			return ;
		}
		byte physicalRobotPosition = 0;
		if(_mode == 0) {
			physicalRobotPosition = receivePosition();
		}
		else if(_mode == 1) {
			physicalRobotPosition = moveAgent();
		}
		System.out.println("Received position from Robot:" + physicalRobotPosition);

		if((_state.block.Status == Block.possibleBlockStatus.NULL)
				&& ((physicalRobotPosition == 13) || (physicalRobotPosition == 0)))
		{
			this.moveAndgetBlockFromSourcePalette();
			this._state._position = physicalRobotPosition;
			System.out.println(this._state);

			return;
		}

		if((_state.block.Status == Block.possibleBlockStatus.DIRTY)
				&& (physicalRobotPosition == 2))
		{
			this.moveAndDropBlockOnCleaningFloor();
			this.waitAndGetCleanedBlock();
			this._state._position = physicalRobotPosition;
			System.out.println(this._state);

			return;
		}

		if((_state.block.Status == Block.possibleBlockStatus.CLEANED)
				&& (physicalRobotPosition == 6))
		{
			this.moveAndDropBlockOnPaintingFloor();
			this.waitAndGetPaintedBlock();
			this._state._position = physicalRobotPosition;
			System.out.println(this._state);

			return;
		}

		if((_state.block.Status == Block.possibleBlockStatus.PAINTED)
				&& (physicalRobotPosition == 9))
		{
			this.moveAndDropBlockOnGoalPalette();
			this._state._position = physicalRobotPosition;
			System.out.println(this._state);

			return;
		}
	}
}
