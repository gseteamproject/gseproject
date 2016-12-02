package gseproject.grid;

import gseproject.core.Color;
import gseproject.grid.communicator.ITrackCommunicator;
import gseproject.grid.communicator.TrackCommunicator;
import gseproject.passive.core.Floor;
import gseproject.passive.core.Palette;
import gseproject.robot.domain.RobotState;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;

public class TrackAgent extends Agent {
	private static final long serialVersionUID = -9064311405865477921L;
	private Track track;
	private ITrackCommunicator trackCommunicator;
	
	private void initTrack() {
		Color[] fields = new Color[16];
		fields[0] = Color.WHITE;
		fields[1] = Color.BROWN;
		fields[2] = Color.WHITE;
		fields[3] = Color.WHITE;
		fields[4] = Color.WHITE;
		fields[5] = Color.GREEN;
		fields[6] = Color.WHITE;
		fields[7] = Color.WHITE;
		fields[8] = Color.RED;
		fields[9] = Color.WHITE;
		fields[10] = Color.WHITE;
		fields[11] = Color.WHITE;
		fields[12] = Color.YELLOW;
		fields[13] = Color.WHITE;
		fields[14] = Color.WHITE;
		fields[15] = Color.WHITE;
		this.track = new Track(fields);
	}
	
	private void initCommunicator(){
		this.trackCommunicator = new TrackCommunicator(this, track);
	}

	
	private void initBehaviour(){
		ParallelBehaviour parallel = new ParallelBehaviour();
		parallel.addSubBehaviour(new CyclicBehaviour(this){
			private static final long serialVersionUID = 1228859825351422623L;

			@Override
			public void action() {
				RobotState robotState = trackCommunicator.receiveRobotState();
				track.addState(robotState);
			}
			
		});
		parallel.addSubBehaviour(new CyclicBehaviour(this){
			private static final long serialVersionUID = 306428081895341759L;

			@Override
			public void action() {
				Floor floor = trackCommunicator.receiveFloor();
				track.addFloorState(floor);
			}
			
		});
		parallel.addSubBehaviour(new CyclicBehaviour(this){
			private static final long serialVersionUID = 306428081895341759L;

			@Override
			public void action() {
				Palette palette = trackCommunicator.receivePalette();
				track.addPaletteState(palette);
			}
			
		});
		this.addBehaviour(parallel);
	}

	protected void setup() {
		System.out.println("Grid Agent started");
		System.out.println("initializing Track");
		this.initTrack();
		System.out.println("initializing communicator");
		this.initCommunicator();
		System.out.println("initializing behaviours");
		this.initBehaviour();
		System.out.println("waiting for states now");
	}

	protected void takeDown() {
		
	}

}
