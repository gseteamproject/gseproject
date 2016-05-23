package examples.protocols;

import jade.Boot;

public class FIPARequestExample
{
	public static void main(String[] p_args)
	{
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "r1:examples.protocols.FIPARequestResponderAgent;";
		parameters[ 1 ] +="r2:examples.protocols.FIPARequestResponderAgent;";
		parameters[ 1 ] +="i:examples.protocols.FIPARequestInitiatorAgent(r1,r2);";

		Boot.main( parameters );
	}
}
