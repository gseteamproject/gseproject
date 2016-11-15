package task3;
b
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class CleaningFloor extends Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int packageCount;
	private int paletteMax = 20;

	protected String agentType = "PaletteStart";
	protected String agentServiceGroup = "Palette";
	protected String agentServiceName = "palette-holder";
	protected Sys sys = new Sys();
	
	public CleaningFloor(){
		
	}
	
	private void setOcupied() {
		//TODO: broadcast full
	}
	private void setFree() {
		//TODO: broadcast nearlyEmpty
	}
	private void setHasPackage() {
		//TODO: broadcast empty
	}
	private void setHasNoPackage() {
		
	}
	
	protected void setup()
	{
		initializeData();	
		registerAgentServicesInDirectoryFacilitator();
		trace("ready");
	}
	
	private void initializeData() {
		packageCount = paletteMax;
		
		
		
	}
	
	
	protected void takeDown()
	{
		deregisterAgentServicesInDirectoryFacilitator();
		trace("terminated");
	}

	
	public void addToSystem(final Palette palette)
	{
		addBehaviour(new AddToSystem(palette));		
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
	
	private void trace(String message)
	{
		System.out.println(getAID().getName() + " (" + agentType + "): " + message);
	}
	
	class AddToSystem extends OneShotBehaviour
	{
		private static final long serialVersionUID = -5766869902626092150L;
		
		Palette palette;
		
		public AddToSystem(Palette palette)
		{			
			this.palette = palette;
		}

		public void action() {
			sys.addPaletteStack();
			trace(palette + " inserted into catalogue");			
		}

	}
}
