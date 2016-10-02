package test.pallete.tests;

import gseproject.palleteRobotCommunication.RobotRequest;
import gseproject.palleteRobotCommunication.SourcePalleteReply;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import test.common.Test;
import test.common.TestException;
import test.common.TestUtility;

public class PalleteEmptyTest extends Test {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AID sourcePallete;

	public Behaviour load(Agent a) throws TestException {
		setTimeout(2000);
		sourcePallete = TestUtility.createAgent(a, "test", "gseproject.palleteRobotCommunication.SourcePallete",
				new Object[] { 0 });
		CyclicBehaviour b = new CyclicBehaviour(a) {
			private static final long serialVersionUID = -3423642459063630856L;

			public void onStart() {
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(sourcePallete);
				msg.setContent(RobotRequest.WHAT_YOUR_STATE);
				System.out.println("Sending message " + msg);
				myAgent.send(msg);
			}

			public void action() {
				ACLMessage msg = myAgent.receive();
				if (msg != null && msg.getContent().equals(SourcePalleteReply.EMPTY)) {
					passed("Reply correct.");
				}
			}
		};
		return b;
	}

	public void clean(Agent a) {
		try {
			TestUtility.killAgent(a, sourcePallete);
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
