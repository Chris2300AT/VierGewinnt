package at.ac.hcw.viergewinnt;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

public class CenterController {

    @FXML
    private Label pathLabel;

    @FXML
    private TreeView<File> fileTree;

    private String selectedCategory;

    // Called from MainController whenever a new category is selected
    public void initData(String selectedCategory) {
        this.selectedCategory = selectedCategory;
        initFileExplorer();
    }

    private void initFileExplorer() {
        if (fileTree == null) return;

        // Wenn: Audio/Video/Text ausgewählt-> BASE_DIR/<Kategorie>, sonst BASE_DIR -VH
        Path rootPath = MainApplication.BASE_DIR;
        if ("Audio".equals(selectedCategory) || "Video".equals(selectedCategory) || "Text".equals(selectedCategory)) {
            rootPath = MainApplication.BASE_DIR.resolve(selectedCategory);
        }

        // gibt es den Ordner check -VH
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File startDir = rootPath.toFile();
        pathLabel.setText(startDir.getAbsolutePath());

        TreeItem<File> rootItem = createNode(startDir);
        rootItem.setExpanded(true);

        fileTree.setShowRoot(true);
        fileTree.setRoot(rootItem);

        fileTree.setCellFactory(tv -> {
            TreeCell<File> cell = new TreeCell<>() {
                @Override
                protected void updateItem(File item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String name = item.getName();
                        setText((name == null || name.isBlank()) ? item.getPath() : name);
                    }
                }
            };

            //Doppelklick: Datei öffnen und Ordnerlogik -VH
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) {
                    File f = cell.getItem();
                    if (f == null) return;
                //Ordner öffnen/schließen -VH
                    if (f.isDirectory()) {
                        TreeItem<File> item = cell.getTreeItem();
                        if (item != null) item.setExpanded(!item.isExpanded());
                    } else {
                        //öffnen mit StandardProgramm -VH
                        openWithDefaultProgram(f);
                    }
                }
            });

            return cell;
        });

        // Pfad anzeigen mit fx:id="pathLabel" -VH
        fileTree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.getValue() != null) {
                pathLabel.setText(newVal.getValue().getAbsolutePath());
            }
        });
    }

    private void openWithDefaultProgram(File file) {
        try {
            if (!Desktop.isDesktopSupported()) {
                pathLabel.setText("Desktop.open() wird auf diesem System nicht unterstützt.");
                return;
            }
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            pathLabel.setText("Konnte Datei nicht öffnen: " + e.getMessage());
        }
    }


    private TreeItem<File> createNode(final File f) {
        return new TreeItem<>(f) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    isLeaf = f.isFile();
                }
                return isLeaf;
            }

            @Override
            public javafx.collections.ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }

            private javafx.collections.ObservableList<TreeItem<File>> buildChildren(TreeItem<File> treeItem) {
                File value = treeItem.getValue();
                if (value == null || !value.isDirectory()) {
                    return FXCollections.observableArrayList();
                }

                File[] files = value.listFiles();
                if (files == null) {
                    return FXCollections.observableArrayList();
                }

                //  Ordner zuerst, dann Dateien; alphabetisch -VH
                Arrays.sort(files, Comparator
                        .comparing(File::isFile)
                        .thenComparing(a -> a.getName().toLowerCase())
                );

                var children = FXCollections.<TreeItem<File>>observableArrayList();
                for (File child : files) {
                    //Versteckte Ordner ausblenden -VH
                    if (child.isHidden()) continue;
                    children.add(createNode(child));
                }
                return children;
            }
        };
    }
}
