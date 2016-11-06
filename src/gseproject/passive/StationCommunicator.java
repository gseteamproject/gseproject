package gseproject.passive;

import gseproject.grid.Block;
import gseproject.grid.Block.possibleBlockStatus;
import gseproject.grid.ServiceType;
import gseproject.infrastructure.serialization.SerializationController;
import gseproject.passive.palette.Goalpalette;
import gseproject.passive.palette.Sourcepalette;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class StationCommunicator<T extends IStationLanguage> extends Agent implements IStationComm {
	private static final long serialVersionUID = 4965866091852183019L;
	// todo: this must be generic or distinct
	private Sourcepalette sourcepalette = new Sourcepalette();
	// todo: quick&dirty but i want my test, it was private
	public ACLMessage requestFromRobot;
	private SerializationController serializationController = SerializationController.Instance;
	// todo: quick&dirty but i want my test
	public ACLMessage aclMsgJustForUnittests = null;

	// ==> Start Jadesetup

	private T t;

	public StationCommunicator(T t) {
		this.t = t;
	}

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
				} // todo: das hier sollte wieder rein
				/*
				 * else { handleInformDone(message); // TODO: this is what?
				 * System.out.
				 * println("setup: action: else; screw you guys i'm going home"
				 * ); }
				 */
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

		// todo: check the instance an then react to it in switch case
		Boolean isSourcepalette = (t instanceof Sourcepalette);
		Boolean isGoalpalette = (t instanceof Goalpalette);
		Boolean isFloor = (t instanceof Floor);

		Block block = new Block();
		switch (request) {
		case FINISH_BLOCK:
			if (!isFloor) {
				System.out.println("reply: switch: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			} else {
				System.out.println("reply: switch: else: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			}
			break;
		case GIVE_BLOCK_DIRTY:
			if (!isFloor) {
				System.out.println("reply: switch: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			} else {
				System.out.println("reply: switch: else: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			}
			break;
		case GIVE_BLOCK_CLEANED:
			if (!isFloor) {
				System.out.println("reply: switch: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			} else {
				System.out.println("reply: switch: else: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			}
			break;
		case GIVE_BLOCK_PAINTED:
			if (!isGoalpalette) {
				System.out.println("reply: switch: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			} else {
				block.Status = possibleBlockStatus.PAINTED;
				send(createReplyMessage(Boolean.toString(((Goalpalette)t).giveBlock(block))));
			}
			break;
		case HAS_BLOCK:
			if (isGoalpalette) {
				System.out.println("reply: switch: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			} else {
				send(createReplyMessage(Boolean.toString(t.hasBlock())));
			}
			break;
		case HAS_FINISHED_BLOCK:
			if (!isFloor) {
				System.out.println("reply: switch: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			} else {
				System.out.println("reply: switch: else: FINISH_BLOCK; screw you guys i'm going home");
				send(createReplyMessage(ServiceType.NOPE.name()));
			}
			break;
		case I_LEAVE:
			// todo: inconsistent, function should return a boolean
			sourcepalette.iLeave();
			send(createReplyMessage("true"));
			break;
		case I_OCCUPY:
			send(createReplyMessage(Boolean.toString(t.iOccuppy())));
			break;
		case IS_OCCUPIED:
			send(createReplyMessage(Boolean.toString(t.isOccupied())));
			break;
		case TAKE_BLOCK:
			send(createReplyMessage(t.takeBlock().Status.name()));
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

	// todo: was das hier? wo kommt das her? was soll ich damit?
	@Override
	public Object receiveInform() {
		// TODO Auto-generated method stub
		return null;
	}
}
