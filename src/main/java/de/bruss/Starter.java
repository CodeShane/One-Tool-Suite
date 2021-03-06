package de.bruss;

import com.jcraft.jsch.JSchException;
import de.bruss.filesync.SftpService;
import de.bruss.settings.Settings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Starter extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private static Scene scene;

	private final Logger logger = LoggerFactory.getLogger(Starter.class);

	static {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	}

	@Override
	public void start(final Stage primaryStage) throws IOException, JSchException {

		System.setProperty("objectdb.home", Settings.appDataPath.toString());
		// System.setProperty("objectdb.conf", getClass().getResource("/objectdb.conf").getPath());

		if (Files.exists(Settings.appDataPath)) {
			logger.info("Config-Folder gefunden unter: " + Settings.appDataPath);
		} else {
			// altes verz. umbenennen
			if (Files.exists(Paths.get(System.getenv("APPDATA") + "\\BootDeployer"))) {
				logger.info("Importing old appData");
				Files.move(Paths.get(System.getenv("APPDATA") + "\\BootDeployer"), Settings.appDataPath);
			} else {
				logger.info("Erstelle Ordner...");
				Files.createDirectory(Settings.appDataPath);
			}

		}

		Context.setPrimaryStage(primaryStage);
		String version = Starter.class.getPackage().getImplementationVersion();
		primaryStage.setTitle("One Tool Suite - Version: " + version);

		primaryStage.getIcons().add(new Image("/images/configure-2.png"));

		if (Settings.isEmpty()) {
			Parent root = FXMLLoader.load(getClass().getResource("/scenes/SettingsScene.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
		} else {
			Parent root = FXMLLoader.load(getClass().getResource("/scenes/MainScene.fxml"));
			scene = new Scene(root);
			String css = Starter.class.getResource("/style/style.css").toExternalForm();
			scene.getStylesheets().clear();
			scene.getStylesheets().add(css);
			primaryStage.setScene(scene);

		}

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				if (SftpService.fsManager != null) {
					SftpService.fsManager.close();
				}
				Platform.exit();
				System.exit(0);
			}
		});

		primaryStage.show();
	}

	public static void reloadCss() {
		String css = Starter.class.getResource("/style/style.css").toExternalForm();
		scene.getStylesheets().clear();
		scene.getStylesheets().add(css);
	}

}
