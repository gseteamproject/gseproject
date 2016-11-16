package gseproject.tests.interaction.grid_gui;

import jade.Boot;

public class Grid_Gui_CommTest {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "GridAgent:gseproject.core.grid.GridAgent;";
		parameters[1] += "GUI:gseproject.experiments.gui.testagents.GUIAgent;";
		Boot.main(parameters);
	}
}
