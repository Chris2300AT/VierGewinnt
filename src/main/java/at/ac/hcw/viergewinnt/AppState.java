package at.ac.hcw.viergewinnt;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.file.Path;

public final class AppState {
    private static final ObjectProperty<Path> selectedDirectory =
            new SimpleObjectProperty<>(MainApplication.BASE_DIR);

    private AppState() {}

    //Immer ein Ordnerpfad, gedacht zum Weitergeben an die Buttons für Chris -VH
    public static Path getSelectedDirectory() {
        return selectedDirectory.get();
    }

    public static void setSelectedDirectory(Path p) {
        if (p != null) selectedDirectory.set(p);
    }

    public static ObjectProperty<Path> selectedDirectoryProperty() {
        return selectedDirectory;
    }
}
