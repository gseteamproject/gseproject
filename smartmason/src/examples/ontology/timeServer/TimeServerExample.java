package examples.ontology.timeServer;

import jade.Boot;

public class TimeServerExample {
	public static void main(String[] p_args)
	{	
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] += "server:examples.ontology.ontologyServer.TimeServerAgent;";
		parameters[ 1 ] += "client:examples.ontology.ontologyServer.TimeClientAgent;";

		Boot.main( parameters );
	}
}
