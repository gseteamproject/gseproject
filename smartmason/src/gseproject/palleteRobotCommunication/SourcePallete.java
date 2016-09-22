package gseproject.palleteRobotCommunication;

import jade.lang.acl.ACLMessage;

/**
 * 
 * @author Tobias
 * This class can fill a message depending on its 3 states:
 * 	1. I am empty
 * 	2. I am almost empty
 * 	3. I am not empty anymore
 * 
 */
public class SourcePallete extends Pallete {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected ACLMessage fillReply(ACLMessage msg) {
		ACLMessage reply = msg.createReply();
		if(this.remainingBlocks == this.capacity){
			reply.setContent(SourcePalleteReply.NOT_EMPTY_ANYMORE);
		}
		else if(this.remainingBlocks == 0){
			reply.setContent(SourcePalleteReply.EMPTY);
		} else {
			reply.setContent(SourcePalleteReply.ALMOST_EMPTY);
		}
		return reply;
	}

	@Override
	protected void trace(String message) {
		System.out.println(getAID().getName() + " ( SourcePallete ): " + message);
	}
	
}
