package gseproject.core.grid.communicator;

import gseproject.core.grid.Grid;
import gseproject.core.grid.GridAgent;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class GridCommunicator implements IGridCommunicator {
	private SerializationController _serializationController = SerializationController.Instance;
	private GridAgent gridAgent;
	private AID GUI = new AID("GUI", AID.ISLOCALNAME);
	
	public GridCommunicator(GridAgent gridAgent) {
		this.gridAgent = gridAgent;
		initiateSerialization();
	}

	private void initiateSerialization() {
		RobotStateWriter writer = new RobotStateWriter();
		RobotStateReader reader = new RobotStateReader();

		_serializationController.RegisterSerializator(RobotStateContract.class, writer, reader);
	}

	@Override
	public ACLMessage agreeRobot(ACLMessage messageFromRobot) {
		ACLMessage reply = messageFromRobot.createReply();
		reply.setPerformative(ACLMessage.AGREE);
		return reply;
	}

	@Override
	public void sendRobotStateToGUI(RobotStateContract robotState) {
		
	}

	@Override
	public void sendInitialGridToGUI(Grid grid) {
		
	}

	public GridAgent getGridAgent(){
		return this.gridAgent;
	}
	
	public AID getGUIAgent(){
		return this.GUI;
	}

}
