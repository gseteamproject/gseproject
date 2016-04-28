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
	
	private Hashtable<String, Integer> availableBooks = new Hashtable<String, Integer>();
	
	private BookSellerGUI myGUI;
	
	private void trace(String p_message)
	{
		String agentType = "Seller-agent ";
		System.out.println(agentType + "(" + getAID().getName() + ") " + p_message);
	}
	
	protected void setup()
	{
		initializeData();
		initializeGUI();
		registerInDirectoryFacilitator();
		initializeBehaviour();
		trace("is ready");
	}
	
	protected void takeDown()
	{
		deregisterInDirectoryFacilitator();		
		finalizeGUI();
		trace("is terminated");
	}

	private void initializeData()
	{
		Random rand = new Random();
		registerBook(new String("The-Lord-of-the-rings"), new Integer(rand.nextInt(100)));
	}

	private void initializeGUI()
	{
		myGUI = new BookSellerGUI(this);
		myGUI.showGUI();
	}
	
	private void finalizeGUI()
	{
		myGUI.dispose();
	}

	private void registerInDirectoryFacilitator()
	{
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setName("JADE-book-trading");
		serviceDescription.setType("book-selling");
		agentDescription.addServices(serviceDescription);
		try
		{
			DFService.register(this, agentDescription);
		}
		catch(FIPAException exception)
		{
			exception.printStackTrace();
		}
	}
	
	private void deregisterInDirectoryFacilitator()
	{
		try
		{
			DFService.deregister(this);
		}
		catch(FIPAException exception)
		{
			exception.printStackTrace();
		}
	}
	
	private void initializeBehaviour()
	{
		addBehaviour(new OfferRequestProcessing());		
		addBehaviour(new PurchaseRequestProcessing());
	}	

	public void registerBook(final String p_title, final Integer p_price)
	{
		addBehaviour(new RegisterBookInAvailableBooks(p_title, p_price));		
	}
	
	class RegisterBookInAvailableBooks extends OneShotBehaviour
	{
		private static final long serialVersionUID = -5766869902626092150L;
		
		String title;
		Integer price;
		
		public RegisterBookInAvailableBooks(String p_title, Integer p_price)
		{
			title = p_title;
			price = p_price;
		}

		public void action()
		{
			availableBooks.put(title, price);
			System.out.println(title +" inserted into catalogue. Price = " + price);
		}

	}
		
	class OfferRequestProcessing extends CyclicBehaviour
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

	class PurchaseRequestProcessing extends CyclicBehaviour
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
