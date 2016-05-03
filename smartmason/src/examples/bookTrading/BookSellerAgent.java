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
		addBookToStore(new Book(BookTitle.JAVA_TUTORIAL));
		addBookToStore(new Book(BookTitle.JADE_PROGRAMMING_TUTORIAL));
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
		addBehaviour(new OfferRequestHandling());		
		addBehaviour(new PurchaseRequestHandling());
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
		
	class OfferRequestHandling extends CyclicBehaviour
	{
		private static final long serialVersionUID = 1421695410309195595L;
		
		public void action()
		{
			ACLMessage receivedMsg = myAgent.receive();
			if(receivedMsg != null)
			{
				String title = receivedMsg.getContent();
				ACLMessage replyMsg = receivedMsg.createReply();
				
				Book book = bookStore.findBookByTitle(title);				
				if( book != null)
				{
					replyMsg.setPerformative(ACLMessage.PROPOSE);
					replyMsg.setContent(String.valueOf(book.price.intValue()));
				}
				else
				{
					replyMsg.setPerformative(ACLMessage.REFUSE);
					replyMsg.setContent(TradingMessage.NOT_AVAILABLE);
				}
				myAgent.send(replyMsg);
			}
			else
			{
				block();
			}
		}
	}

	class PurchaseRequestHandling extends CyclicBehaviour
	{
		private static final long serialVersionUID = 2627668785596772159L;

		public void action()
		{
			MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage receivedMsg = myAgent.receive(messageTemplate);
			if (receivedMsg != null)
			{
				String title = receivedMsg.getContent();
				ACLMessage replyMsg = receivedMsg.createReply();

				if ( bookStore.removeBook(new Book(title)) )
				{
					replyMsg.setPerformative(ACLMessage.INFORM);
					trace(title + " sold to agent " + receivedMsg.getSender().getName());
				}
				else
				{
					replyMsg.setPerformative(ACLMessage.FAILURE);
					replyMsg.setContent(TradingMessage.NOT_AVAILABLE);
				}
				myAgent.send(replyMsg);
			}
			else
			{
				block();
			}
		}	
	}
}
