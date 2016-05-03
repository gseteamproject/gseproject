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
			if( findSellers() )
			{				
				trace("trying to buy " + requiredBookTitle);
				myAgent.addBehaviour(new BuyRequest());
			}
			else
			{
				trace("no active sellers");	
			}
		}
		
		private boolean findSellers()
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
			return (sellerAgents.length > 0);
		}
	}
	
	class BuyRequest extends Behaviour
	{
		private static final long serialVersionUID = -6454308733359936523L;
		
		private static final int PREPARING_CALL_FOR_PROPOSAL = 0;
		private static final int HANDLING_CALL_FOR_PROPOSAL_REPLY = 1;
		private static final int PREPARING_ACCEPT_PROPOSAL = 2;
		private static final int HANDLING_ACCEPT_PROPOSAL_REPLY = 3;
		private static final int PURCHASE_COMPLETED = 4;
		
		private AID bestSeller = null;
		private int bestPrice;
		private int repliesCount = 0;
		private MessageTemplate replyMsgTemplate;
		private int buyRequestState = PREPARING_CALL_FOR_PROPOSAL;
		
		public void action()
		{
			ACLMessage msg = null;
			ACLMessage replyMsg = null;
			switch(buyRequestState)
			{
			case PREPARING_CALL_FOR_PROPOSAL:
				msg = prepareCallForProposalMessage();
				replyMsgTemplate = prepareReplyMessageTemplate(msg);
				myAgent.send(msg);
				buyRequestState = HANDLING_CALL_FOR_PROPOSAL_REPLY;
				break;
			
			case HANDLING_CALL_FOR_PROPOSAL_REPLY:
				replyMsg = myAgent.receive(replyMsgTemplate);
				if( (replyMsg != null) && 
					(replyMsg.getPerformative() == ACLMessage.PROPOSE) )
				{
					handleCallForProposalReplyMessage(replyMsg);					
					repliesCount++;
					if(repliesCount >= sellerAgents.length)
					{
						buyRequestState = PREPARING_ACCEPT_PROPOSAL;
					}
				}
				else
				{
					block();
				}
				break;
			
			case PREPARING_ACCEPT_PROPOSAL:
				msg = prepareAcceptProposalMessage();
				replyMsgTemplate = prepareReplyMessageTemplate(msg);
				myAgent.send(msg); 
				buyRequestState = HANDLING_ACCEPT_PROPOSAL_REPLY;
				break;
			
			case HANDLING_ACCEPT_PROPOSAL_REPLY:
				replyMsg = myAgent.receive(replyMsgTemplate);
				if( (replyMsg != null) &&				
					(replyMsg.getPerformative() == ACLMessage.INFORM) )
				{
					handleAcceptProposalReplyMessage();
					buyRequestState = PURCHASE_COMPLETED;
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
			return ((buyRequestState == PREPARING_ACCEPT_PROPOSAL && bestSeller == null) ||
					(buyRequestState == PURCHASE_COMPLETED));
		}
		
		private ACLMessage prepareCallForProposalMessage()
		{
			ACLMessage msg = new ACLMessage(ACLMessage.CFP);
			for(int i = 0; i < sellerAgents.length; i++)
			{
				msg.addReceiver(sellerAgents[i]);
			}
			msg.setConversationId(BookTrading.CONVERSATION_ID);
			msg.setContent(requiredBookTitle);				
			msg.setReplyWith("cfp" + System.currentTimeMillis());
			return msg;
		}
		
		private void handleCallForProposalReplyMessage(ACLMessage p_message)
		{
			int price = Integer.parseInt(p_message.getContent());
			if(bestSeller == null || price < bestPrice)
			{
				bestPrice = price;
				bestSeller = p_message.getSender();
			}
		}
		
		private ACLMessage prepareAcceptProposalMessage()
		{
			ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			msg.addReceiver(bestSeller);
			msg.setConversationId(BookTrading.CONVERSATION_ID);			
			msg.setContent(requiredBookTitle);			
			msg.setReplyWith("order" + System.currentTimeMillis());
			return msg;
		}
		
		private void handleAcceptProposalReplyMessage()
		{
			trace(requiredBookTitle + " succesfully purchased. Price = " + bestPrice);
			myAgent.doDelete();
		}
		
		private MessageTemplate prepareReplyMessageTemplate(ACLMessage p_message)
		{
			return MessageTemplate.and(MessageTemplate.MatchConversationId(BookTrading.CONVERSATION_ID),
									   MessageTemplate.MatchInReplyTo(p_message.getReplyWith()));
		}
	}
}
