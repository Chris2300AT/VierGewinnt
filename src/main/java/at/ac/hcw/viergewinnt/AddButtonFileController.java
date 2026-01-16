package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import at.ac.hcw.viergewinnt.AppState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AddButtonFileController {
    @FXML
    private TextField addButtonTextfield;

    private String path;

    @FXML
    private TextField filePathField;

    private Boolean hasBeenCreated;

    @FXML
    private void initialize() {
        filePathField.setOnMouseClicked(event -> openFileExplorerUtil());
    }

    Path dir = AppState.getSelectedDirectory();

    private void openFileExplorerUtil() {

        path = FileExplorerUtil.pickFilePath();
        System.out.println("Path: " + path);
    }

    private void copyFile() {
        if (path == null || path.isEmpty()) {
            System.out.println("No source file selected!");
            return;
        }

        String newName = addButtonTextfield.getText();
        Path selection = AppState.getSelectedDirectory();

        Path sourcePath = Paths.get(path);

        if (!newName.contains(".")) {
            String originalName = sourcePath.getFileName().toString();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            newName += extension;
        }

        Path destinationPath = selection.resolve(newName);

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied to: " + destinationPath);
            hasBeenCreated = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        copyFile();
        Stage stage = (Stage) addButtonTextfield.getScene().getWindow();
        stage.close();
    }

    public Boolean getHasBeenCreated() {
        return hasBeenCreated;
    }
}