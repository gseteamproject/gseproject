package examples.pingPong;

import jade.Boot;

public class PingPong
{
	public static void main(String[] p_args)
	{
		System.out.println("Ping pong example started ...");		
		
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "ping:examples.pingPong.PingAgent;";
		parameters[ 1 ] += "pong:examples.pingPong.PongAgent;";

		Boot.main( parameters );
	}
}