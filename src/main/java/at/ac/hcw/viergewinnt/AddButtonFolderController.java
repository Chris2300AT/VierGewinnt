package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddButtonFolderController {
    @FXML
    private TextField addButtonTextfield;

    private String name;

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
