package gseproject.core.grid.communicator;

import gseproject.core.grid.GridAgent;
import gseproject.infrastructure.contracts.GridContract;
import gseproject.infrastructure.contracts.RobotStateContract;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.infrastructure.serialization.grid.GridReader;
import gseproject.infrastructure.serialization.grid.GridWriter;
import gseproject.infrastructure.serialization.robot.RobotStateReader;
import gseproject.infrastructure.serialization.robot.RobotStateWriter;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class GridCommunicator implements IGridCommunicator {
    private SerializationController sc = SerializationController.Instance;
    private GridAgent gridAgent;
    private AID GUI_AID = new AID("GUI", AID.ISLOCALNAME);

    public GridCommunicator(GridAgent gridAgent) {
	this.gridAgent = gridAgent;
	initiateSerialization();
    }

    private void initiateSerialization() {
	RobotStateWriter writer = new RobotStateWriter();
	RobotStateReader reader = new RobotStateReader();

	sc.RegisterSerializator(RobotStateContract.class, writer, reader);

	GridWriter gridWriter = new GridWriter();
	GridReader gridReader = new GridReader();
	sc.RegisterSerializator(GridContract.class, gridWriter, gridReader);
    }

    @Override
    public ACLMessage agreeRobot(ACLMessage messageFromRobot) {
	ACLMessage reply = messageFromRobot.createReply();
	reply.setPerformative(ACLMessage.AGREE);
	return reply;
    }

    @Override
    public ACLMessage sendInitialGrid(ACLMessage messageFromGUI) {
	ACLMessage reply = messageFromGUI.createReply();
	reply.setPerformative(ACLMessage.INFORM);
	if (gridAgent.getGrid() == null) {
	    System.out.println("init failed");
	}
	reply.setContent(sc.Serialize(new GridContract(gridAgent.getGrid())));
	return reply;
    }

    public GridAgent getGridAgent() {
	return this.gridAgent;
    }

    @Override
    public AID GUI_AID() {
	return this.GUI_AID;
    }

    @Override
    public RobotStateContract parseRobotStateFromMessage(ACLMessage messageFromRobot) {
	return sc.Deserialize(RobotStateContract.class, messageFromRobot.getContent());
    }

    @Override
    public ACLMessage sendRobotStateToGUI(RobotStateContract robotState) {
	ACLMessage message = new ACLMessage(ACLMessage.INFORM);
	message.addReceiver(GUI_AID);
	message.setContent(sc.Serialize(robotState));
	return message;
    }

}
