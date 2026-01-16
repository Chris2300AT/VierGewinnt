package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import javafx.scene.control.Label;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RemoveButtonController {

    private boolean removed;

    @FXML
    private Label removeLabel;

    public Boolean hasBeenRemoved() {
        return removed;
    }

    @FXML
    private void handleRemoveButton(){
        try {
            Path path = Paths.get("/home/chris/VierGewinnt/Audio/38 - Creedence Clearwater Revival - Fortunate Son.flac");
            Files.delete(path);
            System.out.println("File deleted successfully!");
        } catch (IOException e) {
            System.err.println("Could not delete file: " + e.getMessage());
        }

        // close the window
        Stage stage = (Stage) removeLabel.getScene().getWindow();
        stage.close();
    }
}