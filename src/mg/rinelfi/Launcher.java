package mg.rinelfi;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mg.rinelfi.controller.InstanceErrorCtrl;
import mg.rinelfi.controller.MainCtrl;

import java.io.File;
import java.io.IOException;

public class Launcher extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			if (!lockedInstance()) {
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
				primaryStage.getIcons().add(new Image(Launcher.class.getResourceAsStream("/mg/rinelfi/ressources/favicon/android-chrome-192x192.png")));
				primaryStage.show();
			} else {
				// Get first screen size
				Rectangle2D rectangle2D = Screen.getPrimary().getBounds();
				
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Launcher.class.getResource("/mg/rinelfi/view/InstanceErrorView.fxml"));
				Parent root = (Parent) loader.load();
				InstanceErrorCtrl controller = (InstanceErrorCtrl) loader.getController();
				Scene scene = new Scene(root);
				scene.setFill(Color.TRANSPARENT);
				primaryStage.setScene(scene);
				primaryStage.initStyle(StageStyle.TRANSPARENT);
				controller.setPrimaryStage(primaryStage);
				Platform.runLater(() -> {
					double xLocation = rectangle2D.getMaxX()/2 - scene.getWidth()/2, yLocation = rectangle2D.getMaxY()/2 - scene.getHeight()/2;
					primaryStage.setX(xLocation);
					primaryStage.setY(yLocation);
				});
				primaryStage.setTitle("Instance error");
				primaryStage.getIcons().add(new Image(Launcher.class.getResourceAsStream("/mg/rinelfi/ressources/favicon/android-chrome-192x192.png")));
				primaryStage.show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean lockedInstance() throws IOException {
		final File file = new File("counter.lock");
		if (file.createNewFile()) {
			file.deleteOnExit();
			return false;
		}
		return true;
	}
}
