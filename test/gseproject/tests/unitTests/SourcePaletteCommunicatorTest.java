package gseproject.tests.unitTests;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import gseproject.core.Block;
import gseproject.core.ServiceType;
import gseproject.passive.communicator.IStationCommunicator;
import gseproject.passive.communicator.SourcePaletteCommunicator;
import gseproject.passive.core.SourcePalette;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class SourcePaletteCommunicatorTest {
	private IStationCommunicator communicator;
	private SourcePalette sourcePalette;

	private static ACLMessage takeBlockMessage() {
		ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
		message.setContent(ServiceType.TAKE_BLOCK.name());
		return message;
	}

	@Ignore
	@Test	
	public void takeBlockTest() {
		sourcePalette = new SourcePalette(0, 10);
		communicator = new SourcePaletteCommunicator(sourcePalette);
		ACLMessage reply = communicator.handleServiceTypeRequest(takeBlockMessage());
		assertTrue(reply.getPerformative() == ACLMessage.INFORM);
		assertTrue(this.sourcePalette.getBlocks().size() == 10);
		Block block = null;
		try {
			block = (Block) reply.getContentObject();
		} catch (UnreadableException e) {
			fail();
			e.printStackTrace();
		}
		assertTrue(block.Status.equals(Block.possibleBlockStatus.DIRTY));
	}
}
