package gseproject.passive;

import gseproject.Block;
import gseproject.Block.possibleBlockStatus;
import gseproject.ServiceType;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.passive.palette.Sourcepalette;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class SourcePalleteCommunicator extends Agent implements IStationComm {
	private static final long serialVersionUID = 4965866091852183019L;
	//todo: this must be generic or distinct
	private Sourcepalette sourcepalette = new Sourcepalette();
	//todo: quick&dirty but i want my test, it was private
	public ACLMessage requestFromRobot;
	private SerializationController serializationController = SerializationController.Instance;
	//todo: quick&dirty but i want my test
	public ACLMessage aclMsgJustForUnittests = null;
	
	// ==> Start Jadesetup
	protected void setup() {
		this.addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 1;

			@Override
			public void action() {
				ACLMessage message = (ACLMessage) receiveMessage();
				if (message.getPerformative() == ACLMessage.REQUEST) {
					ServiceType robotService = serializationController.Deserialize(ServiceType.class,
							message.getContent());
					reply(robotService);
				} //todo: das hier sollte wieder rein
				/*else {
                    handleInformDone(message);
					// TODO: this is what?
					System.out.println("setup: action: else; screw you guys i'm going home");
				}*/
			}
		});
	}

	/**
	 * this method waits for a message from a robot the message can be a request
	 * or an inform from a robot
	 * 
	 * @return null if a robot sends a request, else the object that was sent by
	 *         the robot
	 */
	public Object receiveMessage() {
		return blockingReceive(requestsAndInfo());
	}

	private static MessageTemplate requestsAndInfo() {
		return MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
	}

	@Override
	public void reply(Object requestO) {
		ServiceType request = (ServiceType) requestO;
		if (request == null) {
			// info: this should not happen....
			System.out.println("reply: screw you guys i'm going home");
			return;
		}

		Block block = new Block();
		switch (request) {
		case FINISH_BLOCK:
			System.out.println("reply: switch: FINISH_BLOCK; screw you guys i'm going home");
			send(createReplyMessage(ServiceType.NOPE.name()));
			break;
		case GIVE_BLOCK_DIRTY:
			//todo: check for the right palette
			block.Status = possibleBlockStatus.DIRTY;
			send(createReplyMessage(Boolean.toString(sourcepalette.giveBlock(block))));
			break;
		case GIVE_BLOCK_CLEANED:
			//todo: check for the right palette
			block.Status = possibleBlockStatus.CLEANED;
			send(createReplyMessage(Boolean.toString(sourcepalette.giveBlock(block))));
			break;
		case GIVE_BLOCK_PAINTED:
			//todo: check for the right palette
			block.Status = possibleBlockStatus.PAINTED;
			send(createReplyMessage(Boolean.toString(sourcepalette.giveBlock(block))));
			break;
		case HAS_BLOCK:
			send(createReplyMessage(Boolean.toString(sourcepalette.hasBlock())));
			break;
		case HAS_FINISHED_BLOCK:
			System.out.println("reply: switch: HAS_FINISHED_BLOCK; screw you guys i'm going home");
			send(createReplyMessage(ServiceType.NOPE.name()));
			break;
		case I_LEAVE:
			//todo: inconsistent, function should return a boolean
			sourcepalette.iLeave();
			send(createReplyMessage("true"));
			break;
		case I_OCCUPY:
			send(createReplyMessage(Boolean.toString(sourcepalette.iOccuppy())));
			break;
		case IS_OCCUPIED:
			send(createReplyMessage(Boolean.toString(sourcepalette.isOccupied())));
			break;
		case TAKE_BLOCK:
			send(createReplyMessage(sourcepalette.takeBlock().Status.name()));
			break;
		default:
			// info: this should not happen....
			System.out.println("reply: switch: screw you guys i'm going home");
			return;
		}
	}

	private ACLMessage createReplyMessage(String content) {
		ACLMessage replyToRobot = requestFromRobot.createReply();
		replyToRobot.setContent(content);
		aclMsgJustForUnittests = replyToRobot;
		return replyToRobot;
	}
	
	// ==> End Jadesetup

	//todo: was das hier? wo kommt das her? was soll ich damit?
	@Override
	public Object receiveInform() {
		// TODO Auto-generated method stub
		return null;
	}
}
