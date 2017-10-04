package gseproject.experiments.gui.testagents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class GUIAgent extends Agent {
	private static final long serialVersionUID = 1L;
//	private Track track;

	protected void setup() {
		System.out.println("GUIAgent started");
		this.addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 6730991913139954692L;

			@Override
			public void action() {
//				ACLMessage trackMessage = myAgent.blockingReceive(
//						MessageTemplate.MatchProtocol(ProtocolTemplates.GUIProtocolTemplate.GUI_TRACK_PROTOCOL));
//				Track newTrack = null;
//				try {
//					newTrack = (Track) trackMessage.getContentObject();
//				} catch (UnreadableException e) {
//					e.printStackTrace();
//				}
//				track = newTrack;
			}

		});
	}

	protected void takeDown() {
		System.out.println("GUIAgent down!");
	}
}
