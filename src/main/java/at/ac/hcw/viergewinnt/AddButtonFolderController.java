package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import at.ac.hcw.viergewinnt.AppState;

import java.nio.file.Path;

public class AddButtonFolderController {
    @FXML
    private TextField addButtonTextfield;

    private String name;
    Path dir = AppState.getSelectedDirectory();

    @FXML
    private void handleSave() {
        name = addButtonTextfield.getText();

        // close the window
        Stage stage = (Stage) addButtonTextfield.getScene().getWindow();
        stage.close();
    }

    public String returnName() {
        return name;
    }
}
