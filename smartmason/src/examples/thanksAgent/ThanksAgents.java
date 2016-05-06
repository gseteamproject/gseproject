package examples.thanksAgent;

import jade.Boot;

public class ThanksAgents
{
	public static void main(String[] p_args)
	{
		System.out.println("Thanks agents example started ...");		
		
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "thanksAgent:examples.thanksAgent.ThanksAgent;";		

		Boot.main( parameters );
	}
}
