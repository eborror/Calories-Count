package com.tully;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToUserForm() throws IOException {
        App.setRoot("user_form");
    }

    @FXML
    private void switchToFoodLookup() throws IOException {
        App.setRoot("FoodLookupView");
    }

    @FXML
    private void openTrackedFoods() throws IOException {
        App.setRoot("TrackedFoods");
    }
}
