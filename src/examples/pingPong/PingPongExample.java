package examples.pingPong;

import jade.Boot;

public class PingPongExample
{
	public static void main(String[] p_args)
	{
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "ping:examples.pingPong.PingAgent;";
		parameters[ 1 ] += "pong:examples.pingPong.PongAgent;";

		Boot.main( parameters );
	}
}