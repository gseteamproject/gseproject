package examples.protocols;

import jade.Boot;

public class BrokerExample
{
	public static void main(String[] p_args)
	{
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "r:examples.protocols.FIPARequestResponderAgent;";
		parameters[ 1 ] +="b:examples.protocols.BrokerAgent(r);";
		parameters[ 1 ] +="i:examples.protocols.FIPARequestInitiatorAgent(b)";

		Boot.main( parameters );
	}
}
