package tests;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

public class AgentClass extends Agent {
	private static final long serialVersionUID = 5662452448689849111L;
	
	
					//Variables\\
	
	//Agents
	AMSAgentDescription [] agentsList = null;

	
					//Main code\\
	
	//onCreate
	public void setup() {
		System.out.println("Agent : \"" + getAID().getName()+"\" started working");
		
		//Response manager
		addBehaviour(new CyclicBehaviour(this) 
        {
			private static final long serialVersionUID = 2553105569435917057L;

			public void action() 
             {
                ACLMessage msg= receive();
                if (msg!=null)
                    System.out.println( "<" +myAgent.getLocalName() + "> MSG received: " +msg.getContent() );
                block();
             }
        });
		
		getAgentsList();
		sendMessage("Hello, palette",findAgentAID("PaletteClass"));
	}
	
	
	//Send custom message to single agent
	public void sendMessage(String data,AID destAID){
		ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
	    msg.setContent(data);
	    msg.addReceiver(destAID);
	    send(msg);
	}
	
	
	//Get list of active agents
	public void getAgentsList(){
		try {
	        SearchConstraints c = new SearchConstraints();
	        c.setMaxResults ( new Long(-1) );
	        agentsList = AMSService.search( this, new AMSAgentDescription (), c );
	    }
	    catch (Exception e) {e.printStackTrace();}
		
		for (int i=0; i<agentsList.length;i++){
		     AID agentID = agentsList[i].getName();
		     System.out.println(agentID.getLocalName());
		}
	}
	
	public AID findAgentAID(String name){
		
		AID result = null;
	
		for (int i=0; i<agentsList.length;i++){
			//System.out.println("Scanning:"+agentsList[i].getName().getLocalName());
			
			if (agentsList[i].getName().getLocalName().equals(name)){
				result = agentsList[i].getName();
				
				System.out.println("Agent found: {"+name+"}");
			}
		}
		
		if (result == null){
			System.out.println("Can't find agent with name: {"+name+"}");
		}
		
		return result;
	}

}
