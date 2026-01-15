package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import javafx.scene.control.Label;

public class RemoveButtonController {

    private boolean removed;

    @FXML
    private Label removeLabel;

    public Boolean hasBeenRemoved() {
        return removed;
    }

    @FXML
    private void handleRemoveButton(){
        // close the window
        Stage stage = (Stage) removeLabel.getScene().getWindow();
        stage.close();
    }
}