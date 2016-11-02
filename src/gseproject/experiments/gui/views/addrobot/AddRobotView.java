package gseproject.experiments.gui.views.addrobot;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class AddRobotView {

	private final Parent view;

	public AddRobotView() throws Exception {
		view = FXMLLoader.load(getClass().getResource("AddRobot.fxml"));
		view.getStylesheets().add(getClass().getResource("AddRobot.css").toExternalForm());
	}

	public Parent getView() {
		return view;
	}
}
