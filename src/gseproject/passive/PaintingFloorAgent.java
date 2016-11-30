package gseproject.passive;

import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.passive.communicator.FloorCommunicator;
import gseproject.passive.communicator.ServiceTypeResponder;
import gseproject.passive.core.PaintingFloor;
import jade.proto.AchieveREResponder;

public class PaintingFloorAgent extends FloorAgent {
	private static final long serialVersionUID = -1104113812018831544L;

	@Override
	protected void setup() {
		this.robotServiceRequestTemplate = AchieveREResponder
				.createMessageTemplate(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_PAINTING_FLOOR_PROTOCOL);
		this.floor = new PaintingFloor();
		this.stationCommunicator = new FloorCommunicator(floor, this);
		this.addBehaviour(new ServiceTypeResponder(robotServiceRequestTemplate, stationCommunicator));
	}

	@Override
	protected void takeDown() {

	}

}
