package tests;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class PaletteClass extends Agent {
	private static final long serialVersionUID = 78843894130072704L;
	
	public void setup() {
		System.out.println("Palette : \"" + getAID().getName()+"\" started working");
		
		//Response manager
		addBehaviour(new CyclicBehaviour(this) 
        {
			private static final long serialVersionUID = -2090834330353446196L;

			public void action() 
             {
                ACLMessage msg= receive();
                if (msg!=null)
                    System.out.println( "<" +myAgent.getLocalName() + "> MSG received from {"+msg.getSender().getLocalName()+"}:"+msg.getContent());
                block();
             }
        });
		
	}
	
	
}
