package com.tully;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class MacroChart extends Application {
    private static FoodList foodList;
    public static void setFoodList(FoodList list) {
        foodList = list;
    }

    @Override
    public void start(Stage stage) {
        if (foodList == null) {
            System.out.println("FoodList is null.");
            return;
        }

        Scene scene = new Scene(new Group());
        stage.setTitle("Macro Chart");
        stage.setWidth(420);
        stage.setHeight(720);

        double[] macros = foodList.getFoodInfo();
        double protein = macros[0];
        double fat = macros[1];
        double carbs = macros[2];

        PieChart chart = new PieChart();
        // Check that there are macros to graph
        if (protein + fat + carbs > 0) {
            ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data((Math.round(foodList.getFoodInfo()[0] * 100.0) / 100.0) + "g Protein", protein),
                new PieChart.Data((Math.round(foodList.getFoodInfo()[1] * 100.0) / 100.0) + "g Fat", fat),
                new PieChart.Data((Math.round(foodList.getFoodInfo()[2] * 100.0) / 100.0) + "g Carbs", carbs));

            chart.setData(pieChartData);
            chart.setTitle("Macronutrient Ratio");
        }

        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 Agency FB;");

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        caption.setText(String.valueOf(data.getPieValue()) + "%");
                    }
                });
        }

        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println(Font.getFamilies());
        
        FoodList foods = new FoodList();
        foods.add(new FoodItem("Olive Oil", 0, 14, 0, 120));
        foods.add(new FoodItem("Chicken Breast", 30.0, 3.6, 0.0, 150));
        foods.add(new FoodItem("Rice", 1, 0.3, 28.0, 100));

        setFoodList(foods);
        launch(args);
    }
}
