package gseproject.experiments;

import jade.Boot;

public class Main {
    public static void main(String[] p_args) {
	String[] parameters = new String[2];
	parameters[0] = "-gui";
	parameters[1] = "GUIAgent:gseproject.experiments.gui.testagents.GUIAgent;";
	parameters[1] += "testAgent:gseproject.experiments.gui.testagents.TestAgent;";
	Boot.main(parameters);
    }
}