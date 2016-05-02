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
	
	private String requiredBookTitle;
	
	private AID[] sellerAgents;
	
	protected String agentType = BookTrading.BOOK_BUYER_AGENT;	
	
	private void trace(String p_message)
	{
		System.out.println(getAID().getName() + " (" + agentType + "): " + p_message);
	}
	
	protected void setup()
	{
		initializeData();
		initializeBehaviour();
		trace("ready");
	}
	
	protected void takeDown()
	{
		trace("terminated");
	}
	
	private void initializeData()
	{
		Object[] args = getArguments();
		if(args!=null && args.length>0)
		{
			requiredBookTitle = args[0].toString();
		}
		else
		{
			requiredBookTitle = null;
		}
	}
	
	private void initializeBehaviour()
	{
		addBehaviour( new PeriodicBuyRequest(this, 5000) );
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
			if( requiredBookTitle == null)
			{
				trace("no book title specified");
				doDelete();
				return;
			}			
			findSellers();
			if( sellerAgents.length > 0)
			{				
				trace("trying to buy " + requiredBookTitle);
				myAgent.addBehaviour(new BuyRequest());
			}
			else
			{
				trace("no active sellers");	
			}
		}
		
		private void findSellers()
		{
			DFAgentDescription agentDescriptionTemplate = new DFAgentDescription();
			ServiceDescription requiredService = new ServiceDescription();
			requiredService.setType(BookTrading.BOOK_SELLING_SERVICE);
			agentDescriptionTemplate.addServices(requiredService);
			try
			{
				DFAgentDescription[] foundAgents = DFService.search(myAgent, agentDescriptionTemplate);
				sellerAgents = new AID[foundAgents.length];
				for(int i=0; i<foundAgents.length; i++)
				{
					sellerAgents[i] = foundAgents[i].getName();
				}
			}
			catch(FIPAException exception)
			{
				exception.printStackTrace();
			}
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
				cfp.setContent(requiredBookTitle);
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
				order.setContent(requiredBookTitle);
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
						trace(requiredBookTitle + " succesfully purchased. Price = " + bestPrice);
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
