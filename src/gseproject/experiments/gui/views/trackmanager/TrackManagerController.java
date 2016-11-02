package gseproject.experiments.gui.views.trackmanager;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import jade.core.Agent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TrackManagerController implements Initializable {

	@FXML
	private BorderPane main;

	@FXML
	private BorderPane track;

	@FXML
	private ImageView robotImage;

	@FXML
	private ImageView boxImage;
	
	@FXML
	private BorderPane sourcePalletOne;
	
	@FXML
	private BorderPane sourcePalletTwo;
	
	@FXML
	private BorderPane sourcePalletThree;
	
	@FXML
	private BorderPane goalPalletOne;
	
	@FXML
	private BorderPane goalPalletTwo;
	
	@FXML
	private BorderPane goalPalletThree;
	
	
	/* dummy values */
	private int[] sourcePallets = new int[3];
	
	/* private fields */
	private ObservableList<Agent> activeAgents;
	
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		
		activeAgents = FXCollections.observableArrayList();
		
		robotImage.setOnDragDetected(event -> System.out.println(event));
		boxImage.setOnDragDetected(event -> handleDragOnBlockDetected(event));
		//imageWrapper.setOnDragOver(event -> handleDragOver(event));
		//imageWrapper.setOnDragDropped(event -> handleImageDrop(event));
		
		/* setting up event handlers for sourcePallets */
		sourcePalletOne.setOnDragOver(event -> handleOnDragOver(event));
		sourcePalletOne.setOnDragEntered(event -> sourcePalletOne.setStyle("-fx-background-color: rgba(255, 150, 2, 0.6);"));
		sourcePalletOne.setOnDragExited(event -> sourcePalletOne.setStyle("-fx-background-color: rgba(255, 203, 2, 0.6);"));
		sourcePalletOne.setOnDragDropped(event -> dropOnSourcePallet(event, 0));
		sourcePalletTwo.setOnDragOver(event -> handleOnDragOver(event));
		sourcePalletTwo.setOnDragEntered(event -> sourcePalletTwo.setStyle("-fx-background-color: rgba(255, 150, 2, 0.6);"));
		sourcePalletTwo.setOnDragExited(event -> sourcePalletTwo.setStyle("-fx-background-color: rgba(255, 203, 2, 0.6);"));
		sourcePalletTwo.setOnDragDropped(event -> dropOnSourcePallet(event, 1));
		sourcePalletThree.setOnDragOver(event -> handleOnDragOver(event));
		sourcePalletThree.setOnDragEntered(event -> sourcePalletThree.setStyle("-fx-background-color: rgba(255, 150, 2, 0.6);"));
		sourcePalletThree.setOnDragExited(event -> sourcePalletThree.setStyle("-fx-background-color: rgba(255, 203, 2, 0.6);"));
		sourcePalletThree.setOnDragDropped(event -> dropOnSourcePallet(event, 2));
	
		/* setting up event handlers for goalPallets - A WORK IN PROGRESS */
		goalPalletOne.setOnDragEntered(event -> goalPalletOne.setStyle("-fx-background-color: rgba(255, 100, 0, 0.6);"));
		goalPalletOne.setOnDragExited(event -> goalPalletOne.setStyle("-fx-background-color: rgba(255, 130, 0, 0.6);"));
		goalPalletTwo.setOnDragEntered(event -> goalPalletTwo.setStyle("-fx-background-color: rgba(255, 100, 0, 0.6);"));
		goalPalletTwo.setOnDragExited(event -> goalPalletTwo.setStyle("-fx-background-color: rgba(255, 130, 0, 0.6);"));
		goalPalletThree.setOnDragEntered(event -> goalPalletThree.setStyle("-fx-background-color: rgba(255, 100, 0, 0.6);"));
		goalPalletThree.setOnDragExited(event -> goalPalletThree.setStyle("-fx-background-color: rgba(255, 130, 0, 0.6);"));
	}

	private void handleDragOnBlockDetected(MouseEvent event) {
		ImageView imageView = (ImageView) event.getSource();
		Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();
		content.putString("addrobot");
		db.setContent(content);
		db.setDragView(imageView.getImage());
		event.consume();
	}
	
	private void handleOnDragOver(DragEvent event) {
		if (event.getDragboard().getString().equals("addrobot")) {
            event.acceptTransferModes(TransferMode.ANY);
        }
		event.consume();
	}
	
	
	private void dropRobotOnTrack() {
		/*System.out.println("got here");
		Stage stage = new Stage();
		try {
			AddRobotView addRobotView = new AddRobotView();
			stage.setTitle("Add Robot");
			stage.setScene(new Scene(addRobotView.getView(), 450, 450));
		} catch (Exception e) {
			e.printStackTrace();
		}
		event.setDropCompleted(true);
		event.consume();
		stage.show();
		System.out.println("got here");*/
		
		
	}
	
	private void dropOnSourcePallet(DragEvent event, int sourcePalletId) {
		event.setDropCompleted(true);
		TextInputDialog addBlocksDialog = new TextInputDialog();
		addBlocksDialog.setTitle("Add Blocks");
		addBlocksDialog.setHeaderText("Enter the amount of blocks you want to add");
		Optional<String> result = addBlocksDialog.showAndWait();
		result.ifPresent(count -> sourcePallets[sourcePalletId] += Integer.parseInt(count));
		
		String stateImagePath;
		if (sourcePallets[sourcePalletId] <= 5) {
			stateImagePath = "images/blocks-almost-empty.png";
		} else if (sourcePallets[sourcePalletId] > 5 && sourcePallets[sourcePalletId] <= 15) {
			stateImagePath = "images/blocks-normal.png";
		} else if (sourcePallets[sourcePalletId] > 15) {
			stateImagePath = "images/blocks-full.png";
		} else {
			stateImagePath = "";
		}
		Image stateImage = new Image(getClass().getResource(stateImagePath).toString());
		((BorderPane) event.getSource()).setCenter(new ImageView(stateImage));
		
		System.out.println(Arrays.toString(sourcePallets));
		event.consume();
	}
}
