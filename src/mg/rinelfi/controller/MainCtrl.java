package mg.rinelfi.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import mg.rinelfi.observation.IntObserver;
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
	private TextField pourcentagePlafondText;
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
			counter.setPlafond(Integer.valueOf(plafond.getText()));
			counter.setPourcentagePlafond(Integer.valueOf((int)(pourcentagePlafond.getValue() * 100)));
			counterExecutor = new Thread(counter);
			counterExecutor.start();
			disableAll();
		} else {
			counter.pause();
			timer.pause();
			enableAll();
		}
	}
	
	@FXML
	void stop(ActionEvent event) {
		timer.stop();
		counter.stop();
		enableAll();
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
	
	@FXML
	void switchPlafondEnabled(MouseEvent event) {
		if(definirPlafond.isSelected()) enablePlafond();
		else disablePlafond();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isRunning = false;
		tarif.setText("0");
		minimal.setText("0");
		plafond.setText("0");
		timer = new Timer();
		counter = new Counter();
		disablePlafond();
		pourcentagePlafond.setBlockIncrement(1);
		pourcentagePlafond.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				pourcentagePlafondChanged(observable, oldValue, newValue);
			}
		});
		timer.addObserver(new ValueChangedListener() {
			@Override
			public void getNotified() {
				timerSetNotified();
			}
		});
		counter.addObserver(new ValueChangedListener() {
			@Override
			public void getNotified() {
				counterSetNotified();
			}
		});
		counter.addObserver(new IntObserver() {
			@Override
			public void update(int value) {
				counterUpdateInteger(value);
			}
		});
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
	
	private void timerSetNotified() {
		Platform.runLater(() -> {
			hourViewer.setText(timer.getFormatedHour());
			minuteViewer.setText(timer.getFormatedMinute());
			secondViewer.setText(timer.getFormatedSecond());
			counter.setSeconds(timer.getHour() * 3600 + timer.getMinute() * 60 + timer.getSecond());
		});
	}
	
	private void topPaneMouseDragged(MouseEvent event) {
		getPrimaryStage().setX(xOffset + event.getScreenX());
		getPrimaryStage().setY(yOffset + event.getScreenY());
	}
	
	private void counterUpdateInteger(int value) {
		double progress = ((double) value) / 100D;
		progressBar.setProgress(progress);
	}
	
	private void counterSetNotified() {
		Platform.runLater(() -> {
			if(counter.getMontant() < counter.getMinimal()) montant.setText(counter.getMinimal() + " Ar");
			else montant.setText(counter.getMontant() + " Ar");
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
	
	private void pourcentagePlafondChanged(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		int pourcentage = Float.valueOf(newValue+"").intValue();
		pourcentagePlafondText.setText(pourcentage + "%");
		counter.setPourcentagePlafond(pourcentage);
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	private void enablePlafond() {
		boolean enabled = true;
		plafond.setDisable(!enabled);
		pourcentagePlafond.setDisable(!enabled);
		counter.setPlafondDefinis(enabled);
	}
	
	private void disablePlafond() {
		boolean enabled = false;
		plafond.setDisable(!enabled);
		pourcentagePlafond.setDisable(!enabled);
		counter.setPlafondDefinis(enabled);
	}
	
	private void disableAll() {
		boolean enabled = false;
		tarif.setDisable(!enabled);
		minimal.setDisable(!enabled);
		definirPlafond.setDisable(!enabled);
		plafond.setDisable(!enabled);
		pourcentagePlafond.setDisable(!enabled);
	}
	
	private void enableAll() {
		boolean enabled = true;
		tarif.setDisable(!enabled);
		minimal.setDisable(!enabled);
		definirPlafond.setDisable(!enabled);
		if(counter.isPlafondDefinis()) {
			plafond.setDisable(!enabled);
			pourcentagePlafond.setDisable(!enabled);
		}
	}
}
