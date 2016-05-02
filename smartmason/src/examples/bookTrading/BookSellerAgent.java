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
	
	protected String agentType = BookTrading.BOOK_SELLER_AGENT;
	protected String agentServiceGroup = BookTrading.BOOK_TRADING_SERVICES;
	protected String agentServiceName = BookTrading.BOOK_SELLING_SERVICE;
	
	private void trace(String p_message)
	{
		System.out.println(getAID().getName() + " (" + agentType + "): " + p_message);
	}
	
	protected void setup()
	{
		initializeData();
		initializeGUI();		
		initializeBehaviour();
		registerAgentServicesInDirectoryFacilitator();
		trace("ready");
	}
	
	protected void takeDown()
	{
		deregisterAgentServicesInDirectoryFacilitator();		
		finalizeGUI();
		trace("terminated");
	}

	private void initializeData()
	{		
		addBookToStore(new Book(BookTitle.LORD_OF_THE_RINGS));
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

	private void registerAgentServicesInDirectoryFacilitator()
	{		
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setName(agentServiceGroup);
		serviceDescription.setType(agentServiceName);
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
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
	
	private void deregisterAgentServicesInDirectoryFacilitator()
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

	public void addBookToStore(final Book p_book)
	{
		addBehaviour(new AddBookToStore(p_book));		
	}
	
	class AddBookToStore extends OneShotBehaviour
	{
		private static final long serialVersionUID = -5766869902626092150L;
		
		Book book;
		
		public AddBookToStore(Book p_book)
		{			
			book = p_book;
		}

		public void action()
		{
			bookStore.addBook(book);
			trace(book.getFullDescription() + " inserted into catalogue");			
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
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();
				
				Book book = bookStore.findBookByTitle(title);				
				if( book != null)
				{
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(book.price.intValue()));
				}
				else
				{
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent(TradingMessage.NOT_AVAILABLE);
				}
				myAgent.send(reply);
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
			MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null)
			{
				String title = msg.getContent();
				ACLMessage reply = msg.createReply();

				if ( bookStore.removeBook(new Book(title)) )
				{
					reply.setPerformative(ACLMessage.INFORM);
					trace(title + " sold to agent " + msg.getSender().getName());
				}
				else
				{
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent(TradingMessage.NOT_AVAILABLE);
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
