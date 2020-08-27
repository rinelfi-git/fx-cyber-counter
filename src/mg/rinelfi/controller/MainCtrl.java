package mg.rinelfi.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mg.rinelfi.beans.Timer;
import mg.rinelfi.observation.ValueChangedListener;

import java.net.URL;
import java.util.ResourceBundle;

public class MainCtrl implements Initializable {
	private boolean isRunning;
	private Stage primaryStage;
	private double xOffset;
	private double yOffset;
	private Timer timer;
	private Thread timerExecutor;
	
	@FXML
	private ProgressBar progressBar;
	
	@FXML
	private AnchorPane topPane;
	@FXML
	private Button launchBtn;
	@FXML
	private Label hourViewer;
	
	@FXML
	private Label minuteViewer;
	
	@FXML
	private Label secondViewer;
	
	@FXML
	void pauseStart(ActionEvent event) {
		isRunning = !isRunning;
		launchBtn.setText(isRunning ? "Suspendre" : "Lancer");
		if (isRunning) {
			timerExecutor = new Thread(timer);
			timerExecutor.start();
		} else {
			timer.pause();
		}
	}
	
	@FXML
	void stop(ActionEvent event) {
		timer.stop();
		isRunning = false;
		launchBtn.setText("Lancer");
	}
	
	@FXML
	void close(ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	void minimise(ActionEvent event) {
		primaryStage.setIconified(true);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isRunning = false;
		timer = new Timer();
		timer.addObserver(new ValueChangedListener() {
			@Override
			public void getNotified() {
				Platform.runLater(() -> {
					hourViewer.setText(timer.getFormatedHour());
					minuteViewer.setText(timer.getFormatedMinute());
					secondViewer.setText(timer.getFormatedSecond());
					progressBar.setProgress((double)timer.getSecond() / 100);
				});
			}
		});
		topPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				AnchorPane source = (AnchorPane) event.getSource();
				source.setCursor(Cursor.MOVE);
				xOffset = primaryStage.getX() - event.getScreenX();
				yOffset = primaryStage.getY() - event.getScreenY();
			}
		});
		topPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				AnchorPane source = (AnchorPane) event.getSource();
				source.setCursor(Cursor.DEFAULT);
				xOffset = 0;
				yOffset = 0;
			}
		});
		topPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(xOffset + event.getScreenX());
				primaryStage.setY(yOffset + event.getScreenY());
			}
		});
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}
