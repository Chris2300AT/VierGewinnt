package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class RenameButtonController {

    @FXML
    private TextField renameTextfield;

    private Boolean renamed;

    public Boolean hasBeenRenamed() {
        return renamed;
    }

    public void renameDirectory(Path sourcePath, String newName) {
        try {
            // resolveSibling replaces the last element of the path with the new name
            Path targetPath = sourcePath.resolveSibling(newName);

            if (Files.exists(targetPath)) {
                System.err.println("A file or folder with that name already exists!");
                return;
            }

            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Directory renamed successfully!");

            AppState.setSelectedDirectory(targetPath);
            renamed = true;

        } catch (IOException e) {
            System.err.println("Rename failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRenameButton(){
        String newName = renameTextfield.getText();
        renameDirectory(AppState.getSelectedDirectory(), newName);

        // close the window
        Stage stage = (Stage) renameTextfield.getScene().getWindow();
        stage.close();
    }
}
