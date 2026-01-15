package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private MenuButton addMenu;

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
    @FXML
    private void folderAddButton(javafx.event.ActionEvent event) {
        addButtonFolder();
    }

    @FXML
    private void fileAddButton(javafx.event.ActionEvent event) {
        addButtonFolder();
    }



    public void addButtonFolder (){
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/hcw/viergewinnt/Buttons/addButtonFolder.fxml")
        );
        Parent root = null;

        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addMenu.getScene().getWindow());
            stage.setTitle("Add Folder");

            stage.setScene(new Scene(root));
            stage.showAndWait();


            // If you need data back:
            AddButtonFolderController controller = loader.getController();
            String returnPath = controller.returnPath();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
