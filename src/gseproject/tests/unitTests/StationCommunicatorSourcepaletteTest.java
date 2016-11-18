package gseproject.tests.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import gseproject.core.ServiceType;
import gseproject.passive.palette.communicator.StationCommunicator;
import gseproject.passive.pallete.core.Sourcepalette;
import jade.lang.acl.ACLMessage;

public class StationCommunicatorSourcepaletteTest {
	/*
	@Test
	public void testSetup() {
		//PalleteCommunicator pcom = new PalleteCommunicator();
		//pcom.setup();
		fail("Not yet implemented");
	}
	*/
	
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveFINISH_BLOCKMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.FINISH_BLOCK);
		assertEquals("Content should be NOPE!", "NOPE", pcom.aclMsgJustForUnittests.getContent());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveGIVE_BLOCK_CLEANEDMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.GIVE_BLOCK_CLEANED);
		assertEquals("Content should be NOPE!", "NOPE", pcom.aclMsgJustForUnittests.getContent());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveGIVE_BLOCK_DIRTYMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.GIVE_BLOCK_DIRTY);
		assertEquals("Content should be NOPE!", "NOPE", pcom.aclMsgJustForUnittests.getContent());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveGIVE_BLOCK_PAINTEDMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.GIVE_BLOCK_PAINTED);
		assertEquals("Content should be NOPE!", "NOPE", pcom.aclMsgJustForUnittests.getContent());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveHAS_BLOCKMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.HAS_BLOCK);
		assertEquals("Content should be true!", "true", pcom.aclMsgJustForUnittests.getContent());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveHAS_FINISHED_BLOCKMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.HAS_FINISHED_BLOCK);
		assertEquals("Content should be NOPE!", "NOPE", pcom.aclMsgJustForUnittests.getContent());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveI_LEAVEMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.I_LEAVE);
		assertEquals("Content should be true!", "true", pcom.aclMsgJustForUnittests.getContent());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveI_OCCUPYMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.I_OCCUPY);
		assertEquals("Content should be true!", "true", pcom.aclMsgJustForUnittests.getContent());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveIS_OCCUPIEDMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.IS_OCCUPIED);
		assertEquals("Content should be false!", "false", pcom.aclMsgJustForUnittests.getContent());
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveNOPEMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.NOPE);
		assertEquals("aclMsgJustForUnittests should be null!", null, pcom.aclMsgJustForUnittests);
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testReceiveTAKE_BLOCKMessage() {
		StationCommunicator pcom = new StationCommunicator<Sourcepalette>(new Sourcepalette());
		pcom.requestFromRobot = new ACLMessage();
		pcom.reply(ServiceType.TAKE_BLOCK);
		assertEquals("Content should be DIRTY!", "DIRTY", pcom.aclMsgJustForUnittests.getContent());
	}

	/*
	@Test
	public void testReply() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveInform() {
		fail("Not yet implemented");
	}
	*/
}
