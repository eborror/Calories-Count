package com.tully;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

public class MacroChartController {
    @FXML
    private PieChart pieChart;

    @FXML
    private Label caption;

    private FoodList foodList;

    public void setFoodList(FoodList foodList) {
        this.foodList = foodList;
        populateChart();
    }

    private void populateChart() {
        double[] macros = foodList.getFoodInfo();
        double protein = macros[0];
        double fat = macros[1];
        double carbs = macros[2];

        if (protein + fat + carbs > 0) {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(Math.round(protein * 100.0) / 100.0 + "g Protein", protein),
                new PieChart.Data(Math.round(fat * 100.0) / 100.0 + "g Fat", fat),
                new PieChart.Data(Math.round(carbs * 100.0) / 100.0 + "g Carbs", carbs)
            );

            pieChart.setData(pieChartData);
            pieChart.setTitle("Macronutrient Ratio");
        }

        caption.setText("");
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("primary");
    }
}
