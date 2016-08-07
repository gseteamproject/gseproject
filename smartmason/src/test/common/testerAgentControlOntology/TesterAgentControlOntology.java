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
package test.common.testerAgentControlOntology;

import jade.content.onto.*;
import jade.content.schema.*;

/**
 * Ontology containing concepts and predicates used to control the 
 * execution of a TesterAgent. This ontology is generally used by 
 * the JADE test suite agent.
 * @author Giovanni Caire - TILAB
 */
public class TesterAgentControlOntology extends Ontology {
	// NAME
  public static final String ONTOLOGY_NAME = "Tester-agent-control-ontology";
	
	// VOCABULARY
  public static final String EXECUTE = "EXECUTE";
  public static final String EXECUTE_DEBUG_MODE = "debug-mode";
  
  public static final String CONFIGURE = "CONFIGURE";

  public static final String SELECT_TESTS = "SELECT-TESTS";

  public static final String RESUME = "RESUME";
  public static final String RESUME_DEBUG_MODE = "debug-mode";
  
  public static final String EXIT = "EXIT";
  
  public static final String EXEC_RESULT = "EXECRESULT";
  public static final String EXEC_RESULT_PASSED = "passed";
  public static final String EXEC_RESULT_FAILED = "failed";
  public static final String EXEC_RESULT_SKIPPED = "skipped";

  public static final String NUMBER_OF_TESTS = "NUMBEROFTESTS";
  public static final String NUMBER_OF_TESTS_N = "n";
  
  public static final String TEST_RESULT = "TESTRESULT";
  public static final String TEST_RESULT_NAME = "name";
  public static final String TEST_RESULT_RESULT = "result";
  public static final String TEST_RESULT_ERROR_MSG = "error-msg";
  
  // The singleton instance of this ontology
	private static Ontology theInstance = new TesterAgentControlOntology();
	
	public static Ontology getInstance() {
		return theInstance;
	}
	
  /**
   * Constructor
   */
  private TesterAgentControlOntology() {
  	super(ONTOLOGY_NAME, BasicOntology.getInstance(), new ReflectiveIntrospector());

    try {
    	add(new AgentActionSchema(EXECUTE), Execute.class);
    	add(new AgentActionSchema(CONFIGURE), Configure.class);
    	add(new AgentActionSchema(SELECT_TESTS), SelectTests.class);
    	add(new AgentActionSchema(RESUME), Resume.class);
    	add(new AgentActionSchema(EXIT), Exit.class);
    	add(new ConceptSchema(EXEC_RESULT), ExecResult.class);
    	add(new PredicateSchema(NUMBER_OF_TESTS), NumberOfTests.class);
    	add(new PredicateSchema(TEST_RESULT), TestResult.class);
    	
    	AgentActionSchema as = (AgentActionSchema) getSchema(EXECUTE);
    	as.add(EXECUTE_DEBUG_MODE, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));

    	as = (AgentActionSchema) getSchema(RESUME);
    	as.add(RESUME_DEBUG_MODE, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));

    	ConceptSchema cs = (ConceptSchema) getSchema(EXEC_RESULT);
    	cs.add(EXEC_RESULT_PASSED, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
    	cs.add(EXEC_RESULT_FAILED, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
    	cs.add(EXEC_RESULT_SKIPPED, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
    	
    	PredicateSchema ps = (PredicateSchema) getSchema(NUMBER_OF_TESTS);
    	ps.add(NUMBER_OF_TESTS_N, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
    	
    	ps = (PredicateSchema) getSchema(TEST_RESULT);
    	ps.add(TEST_RESULT_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));
    	ps.add(TEST_RESULT_RESULT, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
    	ps.add(TEST_RESULT_ERROR_MSG, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    } 
    catch (Exception e) {
    	e.printStackTrace();
    } 
	}

}
