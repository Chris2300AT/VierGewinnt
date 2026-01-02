package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

//Controller of the center part of the BorderPane
public class CenterController {

    @FXML
    private Label infoLabel;

    //Selected Category can be used for Filesystem
    private String selectedCategory;

    // Called from MainController whenever a new category is selected
    public void initData(String selectedCategory) {
        this.selectedCategory = selectedCategory;

        //example output of the Category
        infoLabel.setText("Currently selected: " + selectedCategory);
    }
}
