package mg.rinelfi.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mg.rinelfi.beans.Counter;
import mg.rinelfi.beans.Timer;
import mg.rinelfi.observation.ValueChangedListener;

import java.net.URL;
import java.util.ResourceBundle;

public class MainCtrl implements Initializable {
	private boolean isRunning;
	private Stage primaryStage;
	private double xOffset, yOffset;
	private Timer timer;
	private Counter counter;
	private Thread timerExecutor, counterExecutor;
	@FXML
	private TextField tarif;
	@FXML
	private TextField minimal;
	@FXML
	private CheckBox definirPlafond;
	@FXML
	private Slider pourcentagePlafond;
	@FXML
	private TextField plafond;
	@FXML
	private Label montant;
	
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
			counter.setTarif(Integer.valueOf(tarif.getText()));
			counter.setMinimal(Integer.valueOf(minimal.getText()));
			counterExecutor = new Thread(counter);
			counterExecutor.start();
		} else {
			timer.pause();
		}
	}
	
	@FXML
	void stop(ActionEvent event) {
		timer.stop();
		counter.stop();
		isRunning = false;
		launchBtn.setText("Lancer");
	}
	
	@FXML
	void close(ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	void minimise(ActionEvent event) {
		getPrimaryStage().setIconified(true);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isRunning = false;
		tarif.setText("0");
		minimal.setText("0");
		timer = new Timer();
		counter = new Counter();
		timer.addObserver(new ValueChangedListener() {
			@Override
			public void getNotified() {
				Platform.runLater(() -> {
					hourViewer.setText(timer.getFormatedHour());
					minuteViewer.setText(timer.getFormatedMinute());
					secondViewer.setText(timer.getFormatedSecond());
					counter.setMinutes(timer.getMinute() * (timer.getHour() + 1));
				});
			}
		});
		counter.addObserver(new ValueChangedListener() {
			@Override
			public void getNotified() {
				Platform.runLater(() -> {
					if(counter.getMontant() < counter.getMinimal()) montant.setText(counter.getMinimal() + " Ar");
					else montant.setText(counter.getMontant() + " Ar");
				});
			}
		});
		topPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				AnchorPane source = (AnchorPane) event.getSource();
				source.setCursor(Cursor.MOVE);
				xOffset = getPrimaryStage().getX() - event.getScreenX();
				yOffset = getPrimaryStage().getY() - event.getScreenY();
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
				getPrimaryStage().setX(xOffset + event.getScreenX());
				getPrimaryStage().setY(yOffset + event.getScreenY());
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
