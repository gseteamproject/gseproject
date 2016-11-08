package gseproject.experiments.gui.views.gridspace;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class GridSpaceView {

	private final Parent view;

	public GridSpaceView() throws Exception {
			view = FXMLLoader.load(getClass().getResource("GridSpace.fxml"));
			view.getStylesheets().add(getClass().getResource("GridSpace.css").toExternalForm());
		}

	public Parent getView() {
		return view;
	}
}
