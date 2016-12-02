package gseproject.core.grid.communicator;

import gseproject.core.grid.Track;
import gseproject.infrastructure.contracts.ProtocolTemplates;
import gseproject.passive.core.Floor;
import gseproject.passive.core.Palette;
import gseproject.robot.domain.RobotState;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class TrackCommunicator implements ITrackCommunicator {
	private Agent trackAgent;

	public TrackCommunicator(Agent trackAgent, Track track) {
		this.trackAgent = trackAgent;
	}

	@Override
	public RobotState receiveRobotState() {
		ACLMessage robotStateMessage = this.trackAgent.blockingReceive(
				MessageTemplate.MatchProtocol(ProtocolTemplates.TrackProtocolTemplate.TRACK_ROBOT_STATE_PROTOCOL));
		RobotState state = null;
		try {
			state = (RobotState) robotStateMessage.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		return state;
	}

	@Override
	public Floor receiveFloor() {
		ACLMessage floorMessage = this.trackAgent.blockingReceive(
				MessageTemplate.MatchProtocol(ProtocolTemplates.TrackProtocolTemplate.TRACK_FLOOR_PROTOCOL));
		Floor floorState = null;
		try {
			floorState = (Floor) floorMessage.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		return floorState;
	}

	@Override
	public Palette receivePalette() {
		ACLMessage paletteMessage = this.trackAgent.blockingReceive(
				MessageTemplate.MatchProtocol(ProtocolTemplates.TrackProtocolTemplate.TRACK_PALETTE_PROTOCOL));
		Palette paletteState = null;
		try{
			paletteState = (Palette) paletteMessage.getContentObject();
		} catch (UnreadableException e){
			e.printStackTrace();
		}
		return paletteState;
	}
}
