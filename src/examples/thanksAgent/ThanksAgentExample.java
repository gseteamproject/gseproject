package examples.thanksAgent;

import jade.Boot;

public class ThanksAgentExample
{
	public static void main(String[] p_args)
	{
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "thanksAgent:examples.thanksAgent.ThanksAgent;";		

		Boot.main( parameters );
	}
}
