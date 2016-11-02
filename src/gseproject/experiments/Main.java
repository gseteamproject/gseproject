package gseproject.experiments;

import jade.Boot;

public class Main {
    public static void main(String[] p_args) {
	String[] parameters = new String[2];
	parameters[0] = "-gui";
	/*
	parameters[1] = "cleaningFloor:gseproject.experiments.floor.CleaningFloor;";
	*/
	Boot.main(parameters);
    }
}
