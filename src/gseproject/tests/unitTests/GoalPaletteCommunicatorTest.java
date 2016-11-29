package gseproject.tests.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.passive.communicator.GoalPaletteCommunicator;
import gseproject.passive.communicator.IStationCommunicator;
import gseproject.passive.core.GoalPalette;
import jade.lang.acl.ACLMessage;

public class GoalPaletteCommunicatorTest {

	private IStationCommunicator communicator;
	private GoalPalette goalPalette;

	private static ACLMessage takeBlockMessage() {
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.setContent(ServiceType.GIVE_BLOCK.name());
		Block block = new Block();
		block.Status = Block.possibleBlockStatus.PAINTED;
		try {
			message.setContentObject(block);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	@Test
	public void goalPaletteTest() {
		goalPalette = new GoalPalette(5);
		communicator = new GoalPaletteCommunicator(goalPalette);
		for (int i = 0; i < 1000; i++) {
			ACLMessage msg = communicator.handleServiceTypeRequest(takeBlockMessage());
			assertTrue(msg.getPerformative() == ACLMessage.INFORM);
			assertTrue(goalPalette.getBlocks().size() > 0 || goalPalette.getBlocks().size() < 6);
		}

	}
}
