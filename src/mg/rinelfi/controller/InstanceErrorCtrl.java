package mg.rinelfi.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InstanceErrorCtrl implements Initializable {
	private Stage primaryStage;
	private double xOffset, yOffset;
	
	@FXML
	private AnchorPane topPane;
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	@FXML
	void close(ActionEvent event) {
		System.exit(0);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		topPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				topPaneMousePressed(event);
			}
		});
		topPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				topPaneMouseReleased(event);
			}
		});
		topPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				topPaneMouseDragged(event);
			}
		});
	}
	
	private void topPaneMouseReleased(MouseEvent event) {
		AnchorPane source = (AnchorPane) event.getSource();
		source.setCursor(Cursor.DEFAULT);
		xOffset = 0;
		yOffset = 0;
	}
	
	private void topPaneMousePressed(MouseEvent event) {
		AnchorPane source = (AnchorPane) event.getSource();
		source.setCursor(Cursor.MOVE);
		xOffset = getPrimaryStage().getX() - event.getScreenX();
		yOffset = getPrimaryStage().getY() - event.getScreenY();
	}
	
	private void topPaneMouseDragged(MouseEvent event) {
		getPrimaryStage().setX(xOffset + event.getScreenX());
		getPrimaryStage().setY(yOffset + event.getScreenY());
	}
}
