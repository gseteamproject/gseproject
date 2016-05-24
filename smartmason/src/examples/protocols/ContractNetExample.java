package examples.protocols;

import jade.Boot;

public class ContractNetExample
{
	public static void main(String[] p_args)
	{
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "r1:examples.protocols.ContractNetResponderAgent;";
		parameters[ 1 ] +="r2:examples.protocols.ContractNetResponderAgent;";
		parameters[ 1 ] +="i:examples.protocols.ContractNetInitiatorAgent(r1,r2);";

		Boot.main( parameters );
	}
}
