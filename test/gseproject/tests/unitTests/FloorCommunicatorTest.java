package gseproject.tests.unitTests;

public class FloorCommunicatorTest {
//	private IStationCommunicator communicator;
//	private Floor floor;

//	private static ACLMessage dummyMessage() {
//		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
//		return message;
//	}

//	private static ACLMessage takeBlockMessage() {
//		ACLMessage message = dummyMessage();
//		message.setContent(ServiceType.TAKE_BLOCK.name());
//		return message;
//	}

//	private static ACLMessage giveBlockToCleanStationMessage() {
//		ACLMessage message = dummyMessage();
//		Block block = new Block();
//		block.Status = Block.possibleBlockStatus.DIRTY;
//		try {
//			message.setContentObject(block);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return message;
//	}

//	private static ACLMessage giveBlockToPaintStationMessage() {
//		ACLMessage message = dummyMessage();
//		Block block = new Block();
//		block.Status = Block.possibleBlockStatus.CLEANED;
//		try {
//			message.setContentObject(block);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return message;
//	}

//	private static ACLMessage finishBlockMessage() {
//		ACLMessage message = dummyMessage();
//		message.setContent(ServiceType.FINISH_BLOCK.name());
//		return message;
//	}

//	private static ACLMessage occupyMessage() {
//		ACLMessage message = dummyMessage();
//		message.setContent(ServiceType.I_OCCUPY.name());
//		return message;
//	}
	/*
	@Test
	public void cleaningFloorTest() {
		floor = new CleaningFloor();
		communicator = new FloorCommunicator(floor);
		communicator.handleServiceTypeRequest(occupyMessage());
		assertTrue(floor.isOccupied());
		assertFalse(floor.hasBlock());
		assertFalse(floor.hasFinishedBlock());
		System.out.println(floor);

		ACLMessage msg = communicator.handleServiceTypeRequest(giveBlockToCleanStationMessage());
		assertTrue(msg.getPerformative() == ACLMessage.INFORM);
		assertTrue(floor.hasBlock());
		assertTrue(floor.isOccupied());
		assertFalse(floor.hasFinishedBlock());
		System.out.println(floor);

		msg = communicator.handleServiceTypeRequest(finishBlockMessage());
		assertTrue(msg.getPerformative() == ACLMessage.INFORM);
		assertFalse(floor.hasBlock());
		assertTrue(floor.isOccupied());
		assertTrue(floor.hasFinishedBlock());
		System.out.println(floor.toString());

		msg = communicator.handleServiceTypeRequest(takeBlockMessage());
		assertTrue(msg.getPerformative() == ACLMessage.INFORM);
		try {
			Block block = (Block) msg.getContentObject();
			assertTrue(block.Status == Block.possibleBlockStatus.CLEANED);
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		System.out.println(floor);
	}

	@Test
	public void paintingFloorTest() {
		floor = new PaintingFloor();
		communicator = new FloorCommunicator(floor);
		communicator.handleServiceTypeRequest(occupyMessage());
		assertTrue(floor.isOccupied());
		assertFalse(floor.hasBlock());
		assertFalse(floor.hasFinishedBlock());
		System.out.println(floor);

		ACLMessage msg = communicator.handleServiceTypeRequest(giveBlockToPaintStationMessage());
		assertTrue(msg.getPerformative() == ACLMessage.INFORM);
		assertTrue(floor.hasBlock());
		assertTrue(floor.isOccupied());
		assertFalse(floor.hasFinishedBlock());
		System.out.println(floor);

		msg = communicator.handleServiceTypeRequest(finishBlockMessage());
		assertTrue(msg.getPerformative() == ACLMessage.INFORM);
		assertFalse(floor.hasBlock());
		assertTrue(floor.isOccupied());
		assertTrue(floor.hasFinishedBlock());
		System.out.println(floor.toString());

		msg = communicator.handleServiceTypeRequest(takeBlockMessage());
		assertTrue(msg.getPerformative() == ACLMessage.INFORM);
		try {
			Block block = (Block) msg.getContentObject();
			assertTrue(block.Status == Block.possibleBlockStatus.PAINTED);
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		System.out.println(floor);
	}
	*/
}
