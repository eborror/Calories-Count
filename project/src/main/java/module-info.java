module calories_count.files {
    requires javafx.controls;
    requires javafx.fxml;

    opens calories_count.files to javafx.fxml;
    exports calories_count.files;
}
