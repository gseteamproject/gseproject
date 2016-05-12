package examples.employment;

import jade.Boot;

public class EmploymentExample
{
	public static void main(String[] p_args)
	{	
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "";
		parameters[ 1 ] += "engager:examples.employment.EngagerAgent;";
		parameters[ 1 ] += "requester:examples.employment.RequesterAgent;";

		Boot.main( parameters );
	}
}
