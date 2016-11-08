package gseproject.experiments.gui.testagents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class TestAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static MessageTemplate getGUIMessageTemplate() {
		return AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
	}

	private void registerService() {
		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setName("testagent");
		sd.setType("gui");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	protected void setup() {
		System.out.println("TestAgent started");
		registerService();
		this.addBehaviour(new AchieveREResponder(this, getGUIMessageTemplate()) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3644918222515037051L;

			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
				// TODO: send state
				ACLMessage informDone = request.createReply();
				informDone.setPerformative(ACLMessage.INFORM);
				return informDone;
			}
		});
	}

	protected void takeDown() {
		System.out.println("TestAgent down!");
	}

}
