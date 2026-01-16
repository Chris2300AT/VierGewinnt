package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class MoveButtonController {

    private boolean moved;

    private String srcPath, destPath;

    @FXML
    private Label moveLabel;

    public Boolean hasBeenMoved() {
        return moved;
    }
   @FXML
   private TextField chooseFile;

   @FXML
   private TextField chooseNewLocation;

   @FXML
    private void initialize() {
        chooseFile.setOnMouseClicked(event -> srcPath = openFileExplorerUtil());
        chooseNewLocation.setOnMouseClicked(event -> destPath = openDirectoryExplorerUtil());
    }

    @FXML
    private void handleMoveButton(){
        moveFile();
        // close the window
        Stage stage = (Stage) moveLabel.getScene().getWindow();
        stage.close();
    }

    private void moveFile(){
        if (srcPath == null || destPath == null || srcPath.isEmpty() || destPath.isEmpty()) {
            System.out.println("Error: One of the paths is empty.");
            return;
        }

        try {
            Path sourcePath = Paths.get(srcPath);
            Path destinationFolder = Paths.get(destPath);

            Path finalDestination = destinationFolder.resolve(sourcePath.getFileName());

            Files.move(sourcePath, finalDestination, StandardCopyOption.REPLACE_EXISTING);

            moved = true;
            System.out.println("Successfully moved to: " + finalDestination);

        } catch (IOException e) {
            System.err.println("Failed to move file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String openFileExplorerUtil() {
        return FileExplorerUtil.pickFilePath();
    }
    private String openDirectoryExplorerUtil() {
        return FileExplorerUtil.pickDirectoryPath();
    }
}
