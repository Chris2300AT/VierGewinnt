package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import javafx.scene.control.Label;

public class MoveButtonController {

    private boolean moved;

    @FXML
    private Label moveLabel;

    public Boolean hasBeenMoved() {
        return moved;
    }

    @FXML
    private void handleMoveButton(){
        // close the window
        Stage stage = (Stage) moveLabel.getScene().getWindow();
        stage.close();
    }
}
