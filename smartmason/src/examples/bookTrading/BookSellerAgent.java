package examples.bookTrading;

import java.util.Hashtable;
import java.util.Random;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
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
	
	private BookSellerGUI myGUI;
	
	protected void setup()
	{
		System.out.println("Hello! Seller-agent " + getAID().getName() + " is ready.");
		
		availableBooks = new Hashtable<String, Integer>();
		
		Random rand = new Random();
		registerBook(new String("The-Lord-of-the-rings"), new Integer(rand.nextInt(100)));
		
		myGUI = new BookSellerGUI(this);
		myGUI.showGUI();
		
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
		
		myGUI.dispose();
		
		System.out.println("Seller-agent " + getAID().getName() + " terminating.");
	}
	
	public void registerBook(final String p_bookTitle, final Integer p_bookPrice)
	{
		addBehaviour(new RegisterBookInAvailableBooks(p_bookTitle, p_bookPrice));		
	}
	
	class RegisterBookInAvailableBooks extends OneShotBehaviour
	{
		private static final long serialVersionUID = -5766869902626092150L;
		
		String title;
		Integer price;
		
		public RegisterBookInAvailableBooks(String p_bookTitle, Integer p_bookPrice)
		{
			title = p_bookTitle;
			price = p_bookPrice;
		}

		public void action()
		{
			availableBooks.put(title, price);
			System.out.println(title +" inserted into catalogue. Price = " + price);
		}

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
