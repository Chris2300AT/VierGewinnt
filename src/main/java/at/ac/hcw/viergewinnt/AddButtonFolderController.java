package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddButtonFolderController {
    @FXML
    private TextField textField;

    private String path;

    @FXML
    private void handleSave() {
        path = textField.getText();

        // close the window
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.close();
    }

    public String returnPath() {
        return path;
    }
}
