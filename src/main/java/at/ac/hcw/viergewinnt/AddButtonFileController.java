package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;

public class AddButtonFileController {
    @FXML
    private TextField addButtonTextfield;

    private String path;

    @FXML
    private TextField filePathField;

    @FXML
    private void initialize() {
        filePathField.setOnMouseClicked(event -> openFileExplorerUtil());
    }

    private void openFileExplorerUtil() {

        path = FileExplorerUtil.pickFilePath();
        System.out.println("Path: " + path);
    }

    @FXML
    private void handleSave() {
        // close the window
        Stage stage = (Stage) addButtonTextfield.getScene().getWindow();
        stage.close();
    }

    public String returnPath() {
        return path;
    }
}
