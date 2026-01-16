package at.ac.hcw.viergewinnt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class RemoveButtonController {
    @FXML
    private Label removeLabel;

    private boolean removed;

    @FXML
    private void handleRemoveButton() {

        Path targetPath = AppState.getSelectedDirectory();

        if (targetPath != null && Files.exists(targetPath)) {
            try {
                if (Files.isDirectory(targetPath)) {
                    deleteDirectoryRecursively(targetPath);
                } else {
                    // 3. It's a single file
                    Files.delete(targetPath);
                }
                System.out.println("Removed successfully: " + targetPath);
                removed = true;
            } catch (IOException e) {
                System.err.println("Error during removal: " + e.getMessage());
            }
        }

        // Close the window
        Stage stage = (Stage) removeLabel.getScene().getWindow();
        stage.close();
    }

    public boolean hasBeenRemoved (){
        return removed;
    }

    private void deleteDirectoryRecursively(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file); // Delete the file
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir); // Delete the now-empty directory
                return FileVisitResult.CONTINUE;
            }
        });
    }
}