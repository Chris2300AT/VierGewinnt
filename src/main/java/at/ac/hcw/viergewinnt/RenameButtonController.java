package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RenameButtonController {

    @FXML
    private TextField renameTextfield;

    private Boolean renamed;

    public Boolean hasBeenRenamed() {
        return renamed;
    }

    @FXML
    private void handleRenameButton(){
        // close the window
        Stage stage = (Stage) renameTextfield.getScene().getWindow();
        stage.close();
    }
}
