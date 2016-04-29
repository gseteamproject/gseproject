package examples.bookTrading;

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

	private BookStore bookStore = new BookStore();
	
	private BookSellerGUI myGUI;
	
	private void trace(String p_message)
	{
		String agentTypeName = "Seller-agent";
		System.out.println(agentTypeName + " (" + getAID().getName() + ") " + p_message);
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
		registerBook(new Book(BookTitles.LORD_OF_THE_RINGS));
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

	public void registerBook(final Book p_book)
	{
		addBehaviour(new RegisterBookInAvailableBooks(p_book));		
	}
	
	class RegisterBookInAvailableBooks extends OneShotBehaviour
	{
		private static final long serialVersionUID = -5766869902626092150L;
		
		Book book;
		
		public RegisterBookInAvailableBooks(Book p_book)
		{			
			book = p_book;
		}

		public void action()
		{
			bookStore.addBook(book);			
			System.out.println(book.title +" inserted into catalogue. Price = " + book.price);
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
				
				Book book = bookStore.findBookByTitle(bookTitle);				
				if( book != null)
				{
					msgReply.setPerformative(ACLMessage.PROPOSE);
					msgReply.setContent(String.valueOf(book.price.intValue()));
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

				if ( bookStore.removeBook(new Book(title)) )
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
