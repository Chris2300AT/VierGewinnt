package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CenterController {

    @FXML
    private Label infoLabel;

    private String selectedCategory;

    // Called from MainController whenever a new category is selected
    public void initData(String selectedCategory) {
        this.selectedCategory = selectedCategory;
        infoLabel.setText("Currently selected: " + selectedCategory);
    }
}
