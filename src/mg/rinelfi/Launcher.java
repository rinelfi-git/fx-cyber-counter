package mg.rinelfi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mg.rinelfi.controller.MainCtrl;

import java.io.InputStream;

public class Launcher extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Launcher.class.getResource("/mg/rinelfi/view/MainView.fxml"));
		Parent root = (Parent) loader.load();
		MainCtrl controller = (MainCtrl) loader.getController();
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		controller.setPrimaryStage(primaryStage);
		primaryStage.setX(10);
		primaryStage.setY(10);
		primaryStage.setTitle("Cyber counter");
		InputStream inputStream = Launcher.class.getResourceAsStream("/mg/rinelfi/ressources/favicon/android-chrome-192x192.png");
		System.out.println(inputStream);
		primaryStage.getIcons().add(new Image(inputStream));
		primaryStage.show();
	}
}
