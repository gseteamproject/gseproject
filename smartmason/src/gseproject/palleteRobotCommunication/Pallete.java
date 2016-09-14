package gseproject.palleteRobotCommunication;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public abstract class Pallete extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int capacity = 5;
	protected int remainingBlocks;

	protected abstract ACLMessage fillReply(ACLMessage msg);

	protected abstract void trace(String message);

	protected void setup() {
		initializeData();
		initializeBehaviour();
		trace("I am a Pallete and I have " + remainingBlocks + " Blocks");
	}

	/**
	 * Sets capacity of the pallete to the given argument. If no argument is
	 * passed, capacity = 5
	 */
	private void initializeData() {
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			this.remainingBlocks = Integer.parseInt(args[0].toString());
		} else {
			this.remainingBlocks = 5;
		}
	}

	private void initializeBehaviour() {
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 7774831398907094833L;

			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					if (msg.getContent().equals(RobotRequest.WHAT_YOUR_STATE)) {
						trace("got Message from Robot: " + msg.getContent());
						trace("answering...");
						ACLMessage reply = fillReply(msg);
						myAgent.send(reply);
					}
				}
				block();
			}
		});
	}

	protected void takeDown() {
		trace("terminated");
	}
}
