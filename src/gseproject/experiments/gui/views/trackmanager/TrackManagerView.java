package gseproject.experiments.gui.views.trackmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class TrackManagerView {

	private final Parent view;

	public TrackManagerView() throws Exception {
		view = FXMLLoader.load(getClass().getResource("TrackManager.fxml"));
		view.getStylesheets().add(getClass().getResource("TrackManager.css").toExternalForm());
	}

	public Parent getView() {
		return view;
	}
}
