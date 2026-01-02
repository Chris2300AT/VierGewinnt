package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane root;

    @FXML
    private ListView<String> categoryList;

    @FXML
    private TextField searchField;

    @FXML
    private HBox topBar;

    @FXML
    private Region topSpacer;

    @FXML
    private StackPane centerPane; // placeholder for center screen

    @FXML
    public void initialize() {
        // Make search field stretch
        HBox.setHgrow(searchField, Priority.ALWAYS);
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        // Populate left menu
        categoryList.getItems().addAll("Audio", "Video", "Text", "Other");
        categoryList.getSelectionModel().selectFirst();

        // Load the initial center screen
        loadCenterScreen(categoryList.getSelectionModel().getSelectedItem());

        // Update center when selection changes
        categoryList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Removed welcomeText reference
                loadCenterScreen(newVal); // reload center with new category
            }
        });

        System.out.println("MainController initialized");
    }

    private void loadCenterScreen(String selectedCategory) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/at/ac/hcw/viergewinnt/centerScreens/center.fxml")
            );

            Node centerRoot = loader.load();

            // Pass the selected category to the center controller
            CenterController controller = loader.getController();
            controller.initData(selectedCategory);

            // Replace the center content
            centerPane.getChildren().setAll(centerRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
