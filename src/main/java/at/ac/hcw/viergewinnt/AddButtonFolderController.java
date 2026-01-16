package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AddButtonFolderController {
    @FXML
    private TextField addButtonTextfield;

    private String name;

    private boolean created;

    @FXML
    private void handleSave() {
        name = addButtonTextfield.getText();
        createNewFolder(name);

        // close the window
        Stage stage = (Stage) addButtonTextfield.getScene().getWindow();
        stage.close();
    }

    public boolean hasBeenCreated() {
        return created;
    }

    public void createNewFolder(String folderName) {
        Path selection = AppState.getSelectedDirectory();

        if (selection == null || folderName.isEmpty()) return;

        Path parentPath;

        // If user has a file selected, we want to create the folder
        // NEXT to the file (in the same parent directory).
        if (Files.isDirectory(selection)) {
            parentPath = selection;
        } else {
            parentPath = selection.getParent();
        }

        // Now resolve the new folder name against the ACTUAL folder path
        Path newFolderPath = parentPath.resolve(folderName);

        try {
            Files.createDirectory(newFolderPath);
            created = true;
            System.out.println("Folder created at: " + newFolderPath);

            // Refresh your UI here
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public String returnName() {
        return name;
    }
}
