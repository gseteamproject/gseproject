package gseproject.palleteRobotCommunication;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 
 * @author Tobias
 *
 */
public class SimpleRobot extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void trace(String message) {
		System.out.println(getAID().getName() + " ( SimpleRobot ): " + message);
	}

	protected void setup() {
		initializeData();
		initializeBehaviour();
	}

	private void initializeBehaviour() {
		ACLMessage request = new ACLMessage(ACLMessage.INFORM);
		request.addReceiver(new AID("sourcePallete", AID.ISLOCALNAME));
		request.setContent(RobotRequest.WHAT_YOUR_STATE);
		trace("Requesting Source Pallete about his state: " + request.getContent());
		send(request);
		addBehaviour(new SimpleBehaviour(this) {
			private static final long serialVersionUID = 7774831398907094833L;

			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					trace("Got Reply from Pallete: " + msg.getContent());
				}
				block();
			}

			@Override
			public boolean done() {
				return false;
			}
		});

	}

	private void initializeData() {
		// TODO Auto-generated method stub
	}
}
