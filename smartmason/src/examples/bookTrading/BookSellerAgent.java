package examples.bookTrading;

import java.util.Hashtable;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookSellerAgent extends Agent
{
	private static final long serialVersionUID = -8742249310687723764L;
	
	private Hashtable<String, Integer> availableBooks;
	
	protected void setup()
	{
		System.out.println("Hello! Seller-agent " + getAID().getName() + " is ready.");
		
		availableBooks = new Hashtable<String, Integer>();
		
		Random rand = new Random();
		availableBooks.put(new String("The-Lord-of-the-rings"), new Integer(rand.nextInt(100)));
		
		// TODO add interaction with GUI
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("book-selling");
		sd.setName("JADE-book-trading");
		dfd.addServices(sd);
		try
		{
			DFService.register(this, dfd);
		}
		catch(FIPAException fe)
		{
			fe.printStackTrace();
		}
		
		addBehaviour(new OfferRequest());
		
		addBehaviour(new Purchase());
	}
	
	protected void takeDown()
	{
		try
		{
			DFService.deregister(this);
		}
		catch(FIPAException fe)
		{
			fe.printStackTrace();
		}
		// TODO add interaction with GUI
		
		System.out.println("Seller-agent " + getAID().getName() + " terminating.");
	}
		
	class OfferRequest extends CyclicBehaviour
	{
		private static final long serialVersionUID = 1421695410309195595L;
		
		public void action()
		{
			ACLMessage msg = myAgent.receive();
			if(msg != null)
			{
				String bookTitle = msg.getContent();
				ACLMessage msgReply = msg.createReply();
				
				Integer bookPrice = (Integer) availableBooks.get(bookTitle);
				if( bookPrice != null)
				{
					msgReply.setPerformative(ACLMessage.PROPOSE);
					msgReply.setContent(String.valueOf(bookPrice.intValue()));
				}
				else
				{
					msgReply.setPerformative(ACLMessage.REFUSE);
					msgReply.setContent("not-available");
				}
				myAgent.send(msgReply);
			}
			else
			{
				block();
			}
		}
	}

	class Purchase extends CyclicBehaviour
	{
		private static final long serialVersionUID = 2627668785596772159L;

		public void action()
		{
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null)
			{
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();

				Integer price = (Integer) availableBooks.remove(title);
				if (price != null)
				{
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println(title+" sold to agent " + msg.getSender().getName());
				}
				else
				{
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("not-available");
				}
				myAgent.send(reply);
			}
			else
			{
				block();
			}
		}	
	}
}
