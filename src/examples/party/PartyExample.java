package examples.party;

import jade.Boot;

public class PartyExample
{
	public static void main(String[] p_args)
	{
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "host:examples.party.HostAgent;";		

		Boot.main( parameters );
	}
}
