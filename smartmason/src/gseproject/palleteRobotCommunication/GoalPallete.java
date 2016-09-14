package gseproject.palleteRobotCommunication;

import jade.lang.acl.ACLMessage;

public class GoalPallete extends Pallete {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected ACLMessage fillReply(ACLMessage msg) {
		ACLMessage reply = msg.createReply();
		if(this.remainingBlocks == this.capacity){
			reply.setContent(GoalPalleteReply.FULL);
		}
		else if(this.remainingBlocks == 0){
			reply.setContent(GoalPalleteReply.EMPTY_AGAIN);
		} else {
			reply.setContent(GoalPalleteReply.AMLOST_FULL);
		}
		return reply;
	}

	@Override
	protected void trace(String message) {
		System.out.println(getAID().getName() + " ( GoalPallete ): " + message);
	}

}
