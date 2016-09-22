package gseproject.palleteRobotCommunication;

import jade.Boot;

/**
 * 
 * @author Tobias
 * instatiates a source pallete with a capacity of 5 (default) and 3 remaining blocks
 * instantiates a simpleRobot which asks the source pallete about its state
 */
public class palleteRobotCommunicationExample {
	public static void main(String[] p_args)
	{
		String[] parameters = new String[ 2 ];
		parameters[ 0 ] = "-gui";
		parameters[ 1 ] = "sourcePallete:gseproject.palleteRobotCommunication.SourcePallete(3);";
		parameters[ 1 ] += "simpleRobot:gseproject.palleteRobotCommunication.SimpleRobot;";
		Boot.main( parameters );
	}
}
