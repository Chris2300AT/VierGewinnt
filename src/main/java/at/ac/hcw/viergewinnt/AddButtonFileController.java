package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddButtonFileController {
    @FXML
    private TextField addButtonTextfield;

    private String path;

    @FXML
    private void handleSave() {
        path = addButtonTextfield.getText();

        // close the window
        Stage stage = (Stage) addButtonTextfield.getScene().getWindow();
        stage.close();
    }

    public String returnPath() {
        return path;
    }
}
