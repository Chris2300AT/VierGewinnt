package at.ac.hcw.viergewinnt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApplication extends Application {

    // Base directory for whole Vier Gewinnt Projekt, is created if there is not already  C:\User\Username\VierGewinnt -VH
    public static final Path BASE_DIR = Paths.get(System.getProperty("user.home"), "VierGewinnt");

    @Override
    public void start(Stage stage) throws IOException {
        checkValidBaseStructure();
        // add Icon 4G, von mir in paint designt -VH
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("icons/4G.png")));
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMaximized(true);
        stage.setTitle("4Gewinnt!");
        stage.setScene(scene);
        stage.show();
    }
//check if Subfolders Audio, Video and Text are in folder "VierGewinnt" -VH
    private static void checkValidBaseStructure() {
        try {
            Files.createDirectories(BASE_DIR);
            Files.createDirectories(BASE_DIR.resolve("Audio"));
            Files.createDirectories(BASE_DIR.resolve("Video"));
            Files.createDirectories(BASE_DIR.resolve("Text"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
