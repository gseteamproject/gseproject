package gseproject.experiments.gui.application;

import jade.Boot;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gseproject.experiments.gui.views.trackmanager.TrackManagerView;


public class Main extends Application {
	
	
	public static void main(String[] args) {
		launch(Main.class, args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		startTestAgents();
		initGUI(primaryStage);
		primaryStage.show();
	}
	
	
	private void startTestAgents() {
	    String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "GUIAgent:gseproject.experiments.gui.testagents.GUIAgent;";
		parameters[1] += "testAgent:gseproject.experiments.gui.testagents.TestAgent;";
		Boot.main(parameters);
	}
	
	
	private void initGUI(Stage primaryStage) throws Exception {
		TrackManagerView trackManagerView = new TrackManagerView();
		Scene scene = new Scene(trackManagerView.getView());
		primaryStage.setScene(scene);
		primaryStage.setTitle("GSE Project - Manage Robots");
		primaryStage.setMinWidth(800);
		primaryStage.setMinHeight(600);
	}
}
