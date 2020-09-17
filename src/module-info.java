module PathFindingVisualizer {
    requires javafx.fxml;
    requires javafx.controls;

    opens main.java.frontend;
    opens main.java.frontend.controllers to javafx.fxml;
}