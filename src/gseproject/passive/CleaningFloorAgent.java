package gseproject.passive;

import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.passive.communicator.FloorCommunicator;
import gseproject.passive.communicator.ServiceTypeResponder;
import gseproject.passive.core.CleaningFloor;
import jade.proto.AchieveREResponder;

public class CleaningFloorAgent extends FloorAgent {
	private static final long serialVersionUID = -7283341637605998716L;

	@Override
	protected void setup() {
		this.robotServiceRequestTemplate = AchieveREResponder
				.createMessageTemplate(ProtocolTemplates.ServiceTypeProtocolTemplate.ROBOT_CLEANING_FLOOR_PROTOCOL);
		this.floor = new CleaningFloor();
		this.stationCommunicator = new FloorCommunicator(this.floor, this);
		this.addBehaviour(new ServiceTypeResponder(robotServiceRequestTemplate, stationCommunicator));
	}

	@Override
	protected void takeDown() {

	}

}
