package examples.timeServer;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

public class TimeOntology extends BeanOntology {
	private static final long serialVersionUID = 4614131212286958597L;

	public static final String NAME = "Time-Ontology";
	
	// The singleton instance of the Time-Ontology
	private static TimeOntology theInstance = new TimeOntology();
	
	public static Ontology getInstance() {
		return theInstance;
	}
	
	private TimeOntology() {
		super(NAME);
		
		try {
			// Add all Concepts, Predicates and AgentActions in the local package
			add(getClass().getPackage().getName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
