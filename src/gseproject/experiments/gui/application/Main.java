package gseproject.experiments.gui.application;


import gseproject.robot.domain.RobotState;
import gseproject.robot.skills.SkillsSettings;
import jade.Boot;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gseproject.experiments.gui.views.trackmanager.TrackManagerView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class Main extends Application {

	static SystemSettings _settings;
	public static void main(String[] args) {
		String executionPath = System.getProperty("user.dir");

		_settings = new SystemSettings();

		executionPath += "/SmartMASON_Settings/System.xml";
		try {
			_settings.xmlDocumentDecode(executionPath);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}


		launch(Main.class, _settings.args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		startTestAgents();
		initGUI(primaryStage);
		primaryStage.show();
	}
	
	private void startTestAgents() {

		Boot.main(_settings.args);
	}
	
	private void initGUI(Stage primaryStage) throws Exception {
		if(_settings.trackManager) {
			TrackManagerView trackManagerView = new TrackManagerView();
			Scene scene = new Scene(trackManagerView.getView());
			primaryStage.setScene(scene);
			primaryStage.setTitle(_settings.strTitle);
			primaryStage.setMinHeight(_settings.minHeight);
			primaryStage.setMinWidth(_settings.minWidth);
			primaryStage.setMinWidth(_settings.minWidth);
		}

	}
}
