package com.tully;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class PrimaryController {
    // FoodList currentFoods = new FoodList();

    FoodItem f1 = new FoodItem("Apple", 0.3, 0.2, 14.0, 52.0);
    FoodItem f2 = new FoodItem("Chicken Breast", 31.0, 3.6, 0.0, 165.0);
    FoodItem f3 = new FoodItem("White Rice", 2.7, 0.3, 28.0, 130.0);
    FoodItem f4 = new FoodItem("Almonds", 21.0, 49.0, 22.0, 579.0);
    FoodItem f5 = new FoodItem("Boiled Egg", 6.3, 5.3, 0.6, 78.0);

    FoodList currentFoods = new FoodList((new FoodItem[]{f1, f2, f3, f4, f5}));

    @FXML
    private void switchToUserForm() throws IOException {
        App.setRoot("user_form");
    }

    @FXML
    private void switchToFoodLookup() throws IOException {
        App.setRoot("FoodLookupView");
    }

    @FXML
    private void switchToMacroChart(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("macro_chart.fxml"));
        Parent root = loader.load();

        MacroChartController controller = loader.getController();
        controller.setFoodList(currentFoods);

        App.getScene().setRoot(root);
    }

    @FXML
    private void openTrackedFoods() throws IOException {
        App.setRoot("TrackedFoods");
    }
}
