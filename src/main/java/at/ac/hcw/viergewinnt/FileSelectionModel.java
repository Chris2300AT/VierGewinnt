package at.ac.hcw.viergewinnt;

import java.nio.file.Path;

public class FileSelectionModel {
        private static FileSelectionModel instance;

    private Path selectedPath;

    private FileSelectionModel() {}

    public static FileSelectionModel getInstance() {
        if (instance == null) {
            instance = new FileSelectionModel();
        }
        return instance;
    }

    public Path getSelectedPath() { return selectedPath; }

    public void setSelectedPath(Path path) { this.selectedPath = path; }
}
