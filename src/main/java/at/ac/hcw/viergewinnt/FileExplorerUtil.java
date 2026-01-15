package at.ac.hcw.viergewinnt;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

public final class FileExplorerUtil {

    private FileExplorerUtil() {
        // no instances, no bad decisions
    }

    /**
     * Opens the native system file picker and returns the selected path.
     * Returns null if the user cancels.
     */
    public static String pickFilePath() {
        FileDialog dialog = new FileDialog((Frame) null, "Select a File", FileDialog.LOAD);
        dialog.setVisible(true);

        String file = dialog.getFile();
        String dir = dialog.getDirectory();

        if (file == null || dir == null) {
            return null; // user changed their mind
        }

        return new File(dir, file).getAbsolutePath();
    }
}


