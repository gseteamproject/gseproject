package gseproject.passive;

import gseproject.passive.communicator.IStationCommunicator;
import gseproject.passive.communicator.ServiceTypeResponder;
import gseproject.passive.communicator.SourcePaletteCommunicator;
import gseproject.passive.core.SourcePalette;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;

public class SourcepaletteAgent extends Agent {
	private static final long serialVersionUID = -7283341637605998716L;
	private SourcePalette sourcePalette;
	private IStationCommunicator stationCommunicator;
	private MessageTemplate robotServiceRequestTemplate;

	@Override
	protected void setup() {
		this.sourcePalette = new SourcePalette(5, 5);
		this.stationCommunicator = new SourcePaletteCommunicator(this.sourcePalette);
		this.addBehaviour(new ServiceTypeResponder(robotServiceRequestTemplate, stationCommunicator));
	}

	@Override
	protected void takeDown() {

	}

}
