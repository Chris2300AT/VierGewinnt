package at.ac.hcw.viergewinnt;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CenterController {

    @FXML
    private Label pathLabel;

    @FXML
    private TreeTableView<File> fileTree;

    @FXML
    private TreeTableColumn<File, String> colName;

    @FXML
    private TreeTableColumn<File, String> colTimes;

    @FXML
    private TreeTableColumn<File, String> colDate;
    private String selectedCategory;

    private final Map<String, Integer> playCountMap = new HashMap<>();
    private Path playCountFile;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Called from MainController whenever a new category is selected
    public void initData(String selectedCategory) {
        this.selectedCategory = selectedCategory;

        //VierGewinnt\playcount.properties -VH
        this.playCountFile = MainApplication.BASE_DIR.resolve("playcount.properties");
        loadPlayCounts();

        initFileExplorer();


    }

    private void initFileExplorer() {
        if (fileTree == null) return;

        // Wenn: Audio/Video/Text ausgewählt-> BASE_DIR\<Kategorie>, sonst BASE_DIR -VH
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

        colName.setCellValueFactory(param -> {
            File f = param.getValue().getValue();
            if (f == null) return new ReadOnlyStringWrapper("");
            String name = f.getName();
            if (name == null || name.isBlank()) name = f.getPath();
            return new ReadOnlyStringWrapper(name);
        });
        colTimes.setCellValueFactory(param -> {
            File f = param.getValue().getValue();
            if (f == null) return new ReadOnlyStringWrapper("");

            // für Ordner kein Times-Played -VH
            if (f.isDirectory()) return new ReadOnlyStringWrapper("");

            int times = playCountMap.getOrDefault(f.getAbsolutePath(), 0);
            return new ReadOnlyStringWrapper(String.valueOf(times));
        });

        colDate.setCellValueFactory(param -> {
            File f = param.getValue().getValue();
            if (f == null) return new ReadOnlyStringWrapper("-");
            return new ReadOnlyStringWrapper(getChangedOrAddedDate(f));
        });

            //Doppelklick: Datei öffnen und Ordnerlogik -VH
        fileTree.setRowFactory(tv -> {
            TreeTableRow<File> row = new TreeTableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !row.isEmpty()) {
                    File f = row.getItem();
                    if (f == null) return;

                    if (f.isDirectory()) {
                        TreeItem<File> ti = row.getTreeItem();
                        if (ti != null) ti.setExpanded(!ti.isExpanded());
                    } else {
                        //öffnen mit StandardProgramm + 1 TimesPlayed + refresh  -VH
                        openWithDefaultProgram(f);
                    }
                }
            });
            return row;
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

            // Times Played +1 und speichern -VH
            String key = file.getAbsolutePath();
            playCountMap.put(key, playCountMap.getOrDefault(key, 0) + 1);
            savePlayCounts();

            // refresh
            if (fileTree != null) fileTree.refresh();

        } catch (IOException e) {
            pathLabel.setText("Konnte Datei nicht öffnen: " + e.getMessage());
        }
    }

    //soll Änderungsdatum/Erstelldatum zurückgeben -VH
    private String getChangedOrAddedDate(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

            FileTime creation = attr.creationTime();
            FileTime modified = attr.lastModifiedTime();

            // wenn es kein Änderungsdatum gbt , dann das Erstelldatum -VH
            FileTime chosen = modified;
            if (chosen == null) chosen = creation;
            if (chosen == null) return "-";

            // check ob Zeit kleiner/gleich 0 -VH
            if (chosen.toMillis() <= 0 && creation != null) chosen = creation;
            if (chosen.toMillis() <= 0 && modified != null) chosen = modified;

            LocalDateTime ldt = LocalDateTime.ofInstant(chosen.toInstant(), ZoneId.systemDefault());
            return DATE_FMT.format(ldt);

        } catch (Exception e) {
            try {
                FileTime modified = Files.getLastModifiedTime(file.toPath());
                LocalDateTime ldt = LocalDateTime.ofInstant(modified.toInstant(), ZoneId.systemDefault());
                return DATE_FMT.format(ldt);
            } catch (IOException ignored) {
                return "-";
            }
        }
    }

    private void loadPlayCounts() {
        playCountMap.clear();
        if (playCountFile == null) return;
        if (!Files.exists(playCountFile)) return;

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(playCountFile)) {
            props.load(in);
            for (String key : props.stringPropertyNames()) {
                String val = props.getProperty(key);
                if (val == null) continue;
                try {
                    playCountMap.put(key, Integer.parseInt(val.trim()));
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException ignored) {
        }
    }

    private void savePlayCounts() {
        if (playCountFile == null) return;

        Properties props = new Properties();
        playCountMap.forEach((k, v) -> props.setProperty(k, String.valueOf(v)));

        try {
            Files.createDirectories(playCountFile.getParent());
            try (OutputStream out = Files.newOutputStream(playCountFile)) {
                props.store(out, "Times Played per file");
            }
        } catch (IOException ignored) {
        }
    }
    public Path getCurrentlySelectedDirectoryPath() {
        if (fileTree == null || fileTree.getRoot() == null) return null;

        File selected = null;

        var selectedItem = fileTree.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            selected = selectedItem.getValue();
        }

        // Nichts ausgewählt -> root -VH
        if (selected == null) {
            selected = fileTree.getRoot().getValue();
        }
        if (selected == null) return null;

        // Wenn Datei ausgewählt -> Ordner in dem das File liegt -VH
        if (selected.isFile()) {
            File parent = selected.getParentFile();
            return parent != null ? parent.toPath() : null;
        }

        // Wenn Ordner ausgewählt -> Ordner -VH
        return selected.toPath();
    }

    // String wenn das Chris so lieber ist -VH
    public String getCurrentlySelectedDirectoryPathString() {
        Path p = getCurrentlySelectedDirectoryPath();
        return p != null ? p.toAbsolutePath().toString() : null;
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
