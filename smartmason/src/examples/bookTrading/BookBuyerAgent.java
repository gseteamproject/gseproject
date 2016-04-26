package examples.bookTrading;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookBuyerAgent extends Agent
{
	private static final long serialVersionUID = 7689797116251341355L;
	
	private String targetBookTitle;
	
	private AID[] sellerAgents;
	
	protected void setup()
	{
		System.out.println("Hello! Buyer-agent " + getAID().getName() + " is ready.");
		
		Object[] args = getArguments();
		if(args!=null && args.length>0)
		{
			targetBookTitle = args[0].toString();
			System.out.println("Trying to buy " + targetBookTitle);
			
			addBehaviour( new PeriodicBuyRequest(this, 5000) );
		}
		else
		{
			System.out.println("No book title specified");
			doDelete();
		}
	}
	
	protected void takeDown()
	{
		System.out.println("Buyer-agent " + getAID().getName() + " terminating.");
	}
	
	class PeriodicBuyRequest extends TickerBehaviour
	{
		private static final long serialVersionUID = -6675419962533691697L;
		
		public PeriodicBuyRequest(Agent a, long period)
		{
			super(a, period);
		}
		
		protected void onTick()
		{
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("book-selling");
			template.addServices(sd);
			try
			{
				DFAgentDescription[] result = DFService.search(myAgent, template);
				sellerAgents = new AID[result.length];
				for(int i=0; i<result.length; i++)
				{
					sellerAgents[i] = result[i].getName();
				}
			}
			catch(FIPAException fe)
			{
				fe.printStackTrace();
			}
			
			myAgent.addBehaviour(new BuyRequest());
		}
	}
	
	class BuyRequest extends Behaviour
	{
		private static final long serialVersionUID = -6454308733359936523L;
		
		private AID bestSeller;
		private int bestPrice;
		private int repliesCnt = 0;
		private MessageTemplate msgTemplate;
		private int buyRequestStep = 0;
		
		public void action()
		{
			switch(buyRequestStep)
			{
			case 0:
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for(int i = 0; i < sellerAgents.length; i++)
				{
					cfp.addReceiver(sellerAgents[i]);
				}
				cfp.setContent(targetBookTitle);
				cfp.setConversationId("book-trade");
				cfp.setReplyWith("cfp" + System.currentTimeMillis());
				myAgent.send(cfp);
				msgTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
												  MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				buyRequestStep = 1;
				break;
			case 1:
				ACLMessage reply = myAgent.receive(msgTemplate);
				if(reply != null)
				{
					if(reply.getPerformative() == ACLMessage.PROPOSE)
					{
						int price = Integer.parseInt(reply.getContent());
						if(bestSeller == null || price < bestPrice)
						{
							bestPrice = price;
							bestSeller = reply.getSender();
						}
					}
					repliesCnt++;
					if(repliesCnt >= sellerAgents.length)
					{
						buyRequestStep = 2;
					}
				}
				else
				{
					block();
				}
				break;
			case 2:
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(bestSeller);
				order.setContent(targetBookTitle);
				order.setConversationId("book-trade");
				order.setReplyWith("order" + System.currentTimeMillis());
				myAgent.send(order);
				msgTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
						  						  MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				buyRequestStep = 3;
				break;
			case 3:
				reply = myAgent.receive(msgTemplate);
				if(reply != null)
				{
					if(reply.getPerformative() == ACLMessage.INFORM)
					{
						System.out.println(targetBookTitle + " succesfully purchased.");
						System.out.println("Price = " + bestPrice);
						myAgent.doDelete();
					}
					buyRequestStep = 4;
				}
				else
				{
					block();
				}
				break;
			}
		}
		
		public boolean done()
		{
			return ((buyRequestStep == 2 && bestSeller == null) ||
					(buyRequestStep == 4));
		}
	}
}
