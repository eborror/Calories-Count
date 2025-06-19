package com.tully;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;

public class FoodLookupController {

    @FXML private TextField foodInput;
    @FXML private Label proteinPreview;
    @FXML private Label carbsPreview;
    @FXML private Label fatPreview;
    @FXML private Label calPreview;

    @FXML
    private void handleSearch() {
        // Nonfunctional, need to implement still
        proteinPreview.setText("Protein: ? g");
        carbsPreview.setText("Carbs: ? g");
        fatPreview.setText("Fat: ? g");
        calPreview.setText("Calories: ? cal");
    }

    @FXML
    private void handleAdd() {
        // Nonfunctional, need to implement still
        System.out.println("Food added to log. (not actually though..)");
    }

    @FXML
    private void openTrackedFoods() {
        try {
            App.setRoot("TrackedFoods");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("primary");
    }
}
