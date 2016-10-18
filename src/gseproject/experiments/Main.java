package gseproject.experiments;

import jade.Boot;

public class Main {
    public static void main(String[] p_args) {
	String[] parameters = new String[2];
	parameters[0] = "-gui";
	parameters[1] = "cleaningFloor:gseproject.experiments.floor.CleaningFloor;";
	parameters[1] += "testRobot1:gseproject.experiments.TestBlockRobot;";
	parameters[1] += "testBlockTransporter2:gseproject.experiments.TestWorkerRobot;";
	parameters[1] += "testBlockTransporter3:gseproject.experiments.TestTransporterRobot;";
	Boot.main(parameters);
    }
}
