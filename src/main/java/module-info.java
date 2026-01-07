module at.ac.hcw.viergewinnt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens at.ac.hcw.viergewinnt to javafx.fxml;
    exports at.ac.hcw.viergewinnt;
}