package at.ac.hcw.viergewinnt;

import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

public final class FileExplorerUtil {

    private FileExplorerUtil() {}

    private static String startPath = MainApplication.BASE_DIR.toAbsolutePath().toString();;

    public static String pickFilePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");

        // We get the current window so the picker "pops up" over our app
        File file = fileChooser.showOpenDialog(new Stage());

        return (file != null) ? file.getAbsolutePath() : null;
    }

    public static String pickDirectoryPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Destination Folder");

        if (startPath != null && !startPath.isEmpty()) {
            File initDir = new File(startPath);
            if (initDir.exists() && initDir.isDirectory()) {
                directoryChooser.setInitialDirectory(initDir);
            }
        }

        File folder = directoryChooser.showDialog(new Stage());

        return (folder != null) ? folder.getAbsolutePath() : null;
    }
}


