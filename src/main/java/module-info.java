module at.ac.hcw.viergewinnt {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.ac.hcw.viergewinnt to javafx.fxml;
    exports at.ac.hcw.viergewinnt;
}