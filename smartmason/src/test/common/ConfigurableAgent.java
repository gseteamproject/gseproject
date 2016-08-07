/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

package test.common;

import test.common.agentConfigurationOntology.*;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.content.*;
import jade.content.lang.Codec;
import jade.content.lang.leap.LEAPCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Done;
import jade.proto.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPANames;

import java.lang.reflect.Method;
import java.util.Hashtable;

/**
   Generic agent that can load/unload behaviours, content languages 
   and ontologies on request
   @author Giovanni Caire - TILAB
 */
public class ConfigurableAgent extends Agent {
	private Hashtable behaviours = new Hashtable();
	
	private Codec codec = new LEAPCodec();
	private Ontology ontology = AgentConfigurationOntology.getInstance();
	
	private MessageTemplate mt = MessageTemplate.and(
		MessageTemplate.and(
			MessageTemplate.MatchLanguage(codec.getName()),
			MessageTemplate.MatchOntology(ontology.getName())
		),
		MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)
	);
		
	
	protected void setup() {
		System.out.println("Agent "+getName()+" started");
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
		addBehaviour(new AchieveREResponder(this, mt) {
    	protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
    		try {
    			ContentElement ce = myAgent.getContentManager().extractContent(request);
    			
    			// Store the parsed message content into the data store
    			getDataStore().put(this.toString(), ce);
    			
    			// Send back an AGREE 
    			ACLMessage agree = request.createReply();
    			agree.setPerformative(ACLMessage.AGREE);
    			return agree;
    		}
    		catch (Exception e) {
    			throw new NotUnderstoodException(e.getMessage());
    		}
    	}
    	
    	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
    		try {
    			// Retrieve the action from the data store
    			ContentElement ce = (ContentElement) getDataStore().get(this.toString());
    			
    			// Exceute the requested action
    			if (ce instanceof AddBehaviour) {
    				loadBehaviour((AddBehaviour) ce);
    			}
    			else if (ce instanceof RemoveBehaviour) {
    				unloadBehaviour((RemoveBehaviour) ce);    			
    			}
    			else if (ce instanceof LoadLanguage) {
    				loadLanguage((LoadLanguage) ce);
    			}
    			else if (ce instanceof LoadOntology) {
    				loadOntology((LoadOntology) ce);
    			}
    			else if (ce instanceof test.common.agentConfigurationOntology.Quit) {
    				registerPrepareResponse(new OneShotBehaviour() {
    					public void action() {}
    					public void reset() {
    						myAgent.doDelete();
    					}
    				} );
    			}
    			else {
    				throw new FailureException("Unknown action "+ce);
    			}
    			
    			// Send back the notification
    			Done d = new Done((AgentAction) ce);
    			ACLMessage inform = request.createReply();
    			inform.setPerformative(ACLMessage.INFORM);
    			myAgent.getContentManager().fillContent(inform, d);
    			return inform;
    		}
    		catch (Exception e) {
    			throw new FailureException(e.getMessage());
    		}
    	}
		} );
		
	}
	
	private void loadBehaviour(AddBehaviour ab) throws Exception {
		String name = ab.getName();
		String className = ab.getClassName();
		Behaviour b = (Behaviour) Class.forName(className).newInstance();
		if (name != null) {
			behaviours.put(name, b);
		}
		// DEBUG
		System.out.println("Agent "+getName()+": Loading behaviour "+b);
		addBehaviour(b);
	}
	
	private void unloadBehaviour(RemoveBehaviour rb) throws Exception {
		String name = rb.getName();
		Behaviour b = (Behaviour) behaviours.get(name);
		if (b != null) {
			// DEBUG
			System.out.println("Agent "+getName()+": Removing behaviour "+b);
			removeBehaviour(b);
		}
	}
	
	private void loadLanguage(LoadLanguage ll) throws Exception {
		String languageClassName = ll.getClassName();
		String languageName = ll.getName();
		
		Codec c = (Codec) Class.forName(languageClassName).newInstance();
		if (languageName == null) {
			languageName = c.getName();
		}
		// DEBUG
		System.out.println("Agent "+getName()+": Registering language "+languageName);
  	getContentManager().registerLanguage(c, languageName);
	}
		
	private void loadOntology(LoadOntology lo) throws Exception {
		String ontoClassName = lo.getClassName();
		String ontoName = lo.getName();
		
		Class c = Class.forName(ontoClassName);
		Method m = c.getMethod("getInstance", new Class[0]);
		Ontology onto = (Ontology) m.invoke(null, new Object[]{});
		if (ontoName == null) {
			ontoName = onto.getName();
		}
		// DEBUG
		System.out.println("Agent "+getName()+": Registering ontology "+ontoName);
  	getContentManager().registerOntology(onto, ontoName);
	}
}

