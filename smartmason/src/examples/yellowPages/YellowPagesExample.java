package examples.yellowPages;

import jade.Boot;

public class YellowPagesExample
{
	public static void main(String[] p_args)
	{
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "provider-1:examples.yellowPages.DFRegisterAgent(my-forecast);";
		parameters[ 1 ] += "searcher:examples.yellowPages.DFSearchAgent;";
		parameters[ 1 ] += "subscriber:examples.yellowPages.DFSubscribeAgent;";
		parameters[ 1 ] += "provider-2:examples.yellowPages.DFRegisterAgent(forecast-1);";

		Boot.main( parameters );
	}
}
