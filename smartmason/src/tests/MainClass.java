package tests;

import jade.Boot;

public class MainClass {
	
	public static void main(String[] args){
		System.out.println("GSE-Project running");
		
		String[] param = new String[ 2 ];
		param[ 0 ] = "-gui";
		param[ 1 ] = "AgentClass:tests.AgentClass;PaletteClass:tests.PaletteClass";

		Boot.main( param );
	}

}
