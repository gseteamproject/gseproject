package examples.ontology.timeServer;

import java.util.Date;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class TimeClientAgent extends Agent {
	private static final long serialVersionUID = -7458855700677257054L;
	private AID timeServerAgent; 
	
	protected void setup() {
		// Read the time server agent name as startup argument (use default if not specified)
		String timeServerAgentName = "server"; // Default name
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			timeServerAgentName = (String) args[0];
		}
		timeServerAgent = new AID(timeServerAgentName, AID.ISLOCALNAME);
		
		// Register language and ontology 
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(TimeOntology.getInstance());
		
		// After 10 seconds request the current time
		addBehaviour(new WakerBehaviour(this, 10000) {
			private static final long serialVersionUID = -3260614914851919611L;

			public void onWake() {
				requestGetTime();
			}
		});
		
		// After 20 seconds shift the time managed by the TimeServerAgent by 1 hour
		addBehaviour(new WakerBehaviour(this, 20000) {
			private static final long serialVersionUID = 4841882805265088264L;

			public void onWake() {
				Date newTime = new Date(System.currentTimeMillis() + 3600000); // Now + 1 hour 
				requestSetTime(newTime);
			}
		});
		
		// After 30 seconds request the current time again
		addBehaviour(new WakerBehaviour(this, 30000) {
			private static final long serialVersionUID = 8330733328661212886L;

			public void onWake() {
				requestGetTime();
			}
		});
	}
		

	private void requestGetTime() {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(timeServerAgent);
		request.setOntology(TimeOntology.getInstance().getName());
		request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
		try {
			GetTime gt = new GetTime();
			Action actExpr = new Action(timeServerAgent, gt);
			getContentManager().fillContent(request, actExpr);
			addBehaviour(new AchieveREInitiator(this, request) {
				private static final long serialVersionUID = -1032516199243751198L;

				public void handleInform(ACLMessage inform) {
					System.out.println("Agent "+myAgent.getLocalName()+" - Current time is "+inform.getContent());
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void requestSetTime(Date time) {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.addReceiver(timeServerAgent);
		request.setOntology(TimeOntology.getInstance().getName());
		request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
		try {
			SetTime st = new SetTime();
			st.setTime(time);
			Action actExpr = new Action(timeServerAgent, st);
			getContentManager().fillContent(request, actExpr);
			addBehaviour(new AchieveREInitiator(this, request) {
				private static final long serialVersionUID = -6119609841648017660L;

				public void handleInform(ACLMessage inform) {
					System.out.println("Agent "+myAgent.getLocalName()+" - New time successfully set");
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
