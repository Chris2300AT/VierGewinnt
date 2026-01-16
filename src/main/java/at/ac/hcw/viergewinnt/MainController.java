package at.ac.hcw.viergewinnt;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.awt.Desktop;
import java.io.File;
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

    private String currentCategory;

    private ListView<File> searchListView = new ListView<>();

    private ObservableList<File> searchResults = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        HBox.setHgrow(searchField, Priority.ALWAYS);
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        setupSearchListView();

        categoryList.getItems().addAll("Audio", "Video", "Text", "Other");
        categoryList.getSelectionModel().selectFirst();
        currentCategory = "Audio";
        loadCenterScreen(currentCategory);

        categoryList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                currentCategory = newVal;
                searchField.clear();
                loadCenterScreen(newVal);
            }
        });

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            handleSearch(newText);
        });

        System.out.println("MainController initialized");
    }

    @FXML
    private void folderAddButton(javafx.event.ActionEvent event) {
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
            String returnName = controller.returnName();
            Boolean hasBeenCreated = controller.hasBeenCreated();

            if (hasBeenCreated){
                loadCenterScreen(currentCategory);
            }

            System.out.println("Return Name: " + returnName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void fileAddButton(javafx.event.ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/hcw/viergewinnt/Buttons/addButtonFile.fxml")
        );
        Parent root = null;

        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addMenu.getScene().getWindow());
            stage.setTitle("Add File");

            stage.setScene(new Scene(root));
            stage.showAndWait();

            AddButtonFileController controller = loader.getController();
            Boolean hasBeenCreated = controller.getHasBeenCreated();

            if (hasBeenCreated){
                loadCenterScreen(currentCategory);
            }

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

            CenterController controller = loader.getController();
            controller.initData(selectedCategory);

            // Replace the center content
            centerPane.getChildren().setAll(centerRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renameButtonClicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/hcw/viergewinnt/Buttons/renameButton.fxml")
        );
        Parent root = null;

        try{
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Rename");

            stage.setScene(new Scene(root));
            stage.showAndWait();

            RenameButtonController controller = loader.getController();
            Boolean hasBeenRenamed = controller.hasBeenRenamed();

            if (hasBeenRenamed){
                loadCenterScreen(currentCategory);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeButtonClicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/hcw/viergewinnt/Buttons/removeButton.fxml")
        );
        Parent root = null;

        try{
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Remove");

            stage.setScene(new Scene(root));
            stage.showAndWait();

            RemoveButtonController controller = loader.getController();

            Boolean hasBeenRemoved = controller.hasBeenRemoved();

            if (hasBeenRemoved){
                loadCenterScreen(currentCategory);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void moveButtonClicked(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/hcw/viergewinnt/Buttons/moveButton.fxml")
        );
        Parent root = null;

        try{
            root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Move");

            stage.setScene(new Scene(root));
            stage.showAndWait();

            MoveButtonController controller = loader.getController();
            Boolean hasBeenMoved = controller.hasBeenMoved();

            if (hasBeenMoved){
                loadCenterScreen(currentCategory);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupSearchListView() {
        searchListView.setItems(searchResults);

        searchListView.setCellFactory(lv -> new ListCell<File>() {
            @Override
            protected void updateItem(File file, boolean empty) {
                super.updateItem(file, empty);
                if (empty || file == null) {
                    setText(null);
                } else {
                    // Shows: Filename [CategoryName]
                    setText(file.getName() + " [" + file.getParentFile().getName() + "]");
                }
            }
        });

        // Double-click to open file
        searchListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                File selected = searchListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openFile(selected);
                }
            }
        });
    }

    private void handleSearch(String query) {
        if (query == null || query.isEmpty()) {
            if (!centerPane.getChildren().isEmpty()) {
                centerPane.getChildren().get(0).setVisible(true);
            }
            searchListView.setVisible(false);
        } else {
            // Hide normal content, show search list
            if (!centerPane.getChildren().isEmpty()) {
                centerPane.getChildren().get(0).setVisible(false);
            }

            if (!centerPane.getChildren().contains(searchListView)) {
                centerPane.getChildren().add(searchListView);
            }
            searchListView.setVisible(true);
            performFileSearch(query);
        }
    }

    private void performFileSearch(String query) {
        searchResults.clear();
        String lowerQuery = query.toLowerCase();

        try (Stream<Path> stream = Files.walk(MainApplication.BASE_DIR)) {
            java.util.List<File> matches = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().contains(lowerQuery))
                    .limit(100)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            searchResults.addAll(matches);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFile(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            System.err.println("Could not open file: " + e.getMessage());
        }
    }
}
