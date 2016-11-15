package gseproject.core.grid.communicator;

import gseproject.core.grid.Grid;
import gseproject.core.grid.GridAgent;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import jade.core.ScalabilityTest;
import jade.lang.acl.ACLMessage;

public class GridCommunicator implements IGridCommunicator {
    private SerializationController _serializationController = SerializationController.Instance;
    private GridAgent gridAgent;

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
    public void notifyGUI(ACLMessage messageFromRobot) {
	RobotStateContract state = _serializationController.Deserialize(RobotStateContract.class,
		messageFromRobot.getContent());
	System.out.println(state);
    }

    @Override
    public ACLMessage agreeRobot(ACLMessage messageFromRobot) {
	ACLMessage reply = messageFromRobot.createReply();
	reply.setPerformative(ACLMessage.AGREE);
	return reply;
    }

}
