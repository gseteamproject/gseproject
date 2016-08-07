/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package test.common.agentConfigurationOntology;

import jade.content.onto.*;
import jade.content.schema.*;

/**
 * Ontology containing agent actions indicating that an agent
 * should add/remove a given behaviour
 * @author Giovanni Caire - TILAB
 */
public class AgentConfigurationOntology extends Ontology {
	// NAME
  public static final String ONTOLOGY_NAME = "Agent-configuration-ontology";
	
	// VOCABULARY
  public static final String ADD_BEHAVIOUR = "ADD_BEHAVIOUR";
  public static final String ADD_BEHAVIOUR_NAME = "name";
  public static final String ADD_BEHAVIOUR_CLASS_NAME = "classname";

  public static final String REMOVE_BEHAVIOUR = "REMOVE_BEHAVIOUR";
  public static final String REMOVE_BEHAVIOUR_NAME = "name";
  
  public static final String LOAD_LANGUAGE = "LOAD_LANGUAGE";
  public static final String LOAD_LANGUAGE_NAME = "name";
  public static final String LOAD_LANGUAGE_CLASS_NAME = "classname";

  public static final String LOAD_ONTOLOGY = "LOAD_ONTOLOGY";
  public static final String LOAD_ONTOLOGY_NAME = "name";
  public static final String LOAD_ONTOLOGY_CLASS_NAME = "classname";

  public static final String QUIT = "QUIT";

  // The singleton instance of this ontology
	private static Ontology theInstance = new AgentConfigurationOntology();
	
	public static Ontology getInstance() {
		return theInstance;
	}
	
  /**
   * Constructor
   */
  private AgentConfigurationOntology() {
  	super(ONTOLOGY_NAME, BasicOntology.getInstance(), new ReflectiveIntrospector());

    try {
    	add(new AgentActionSchema(ADD_BEHAVIOUR), AddBehaviour.class);
    	add(new AgentActionSchema(REMOVE_BEHAVIOUR), RemoveBehaviour.class);
    	add(new AgentActionSchema(LOAD_LANGUAGE), LoadLanguage.class);
    	add(new AgentActionSchema(LOAD_ONTOLOGY), LoadOntology.class);
    	add(new AgentActionSchema(QUIT), Quit.class);
    	
    	AgentActionSchema as = (AgentActionSchema) getSchema(ADD_BEHAVIOUR);
    	as.add(ADD_BEHAVIOUR_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    	as.add(ADD_BEHAVIOUR_CLASS_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

    	as = (AgentActionSchema) getSchema(REMOVE_BEHAVIOUR);
    	as.add(REMOVE_BEHAVIOUR_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

    	as = (AgentActionSchema) getSchema(LOAD_LANGUAGE);
    	as.add(LOAD_LANGUAGE_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    	as.add(LOAD_LANGUAGE_CLASS_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

    	as = (AgentActionSchema) getSchema(LOAD_ONTOLOGY);
    	as.add(LOAD_ONTOLOGY_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    	as.add(LOAD_ONTOLOGY_CLASS_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

    } 
    catch (OntologyException oe) {
    	oe.printStackTrace();
    } 
	}

}
