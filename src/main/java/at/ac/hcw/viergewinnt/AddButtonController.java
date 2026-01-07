package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddButtonController {
    @FXML
    private TextField textField;

    private String someValue;

    @FXML
    private void handleSave() {
        someValue = textField.getText();

        // close the window
        Stage stage = (Stage) textField.getScene().getWindow();
        stage.close();
    }

    public String addButtonReturnValue() {
        return someValue;
    }
}
