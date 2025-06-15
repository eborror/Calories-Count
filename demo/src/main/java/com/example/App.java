package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calories Count");
        // Daily totals
        Label totalsTitle = new Label("Daily Totals");
        Label totalProtein = new Label("Protein: -- g");
        Label totalCarbs = new Label("Carbs: -- g");
        Label totalFat = new Label("Fat: -- g");
        Label totalCalories = new Label("Calories: --");
        VBox totalsBox = new VBox(10, totalsTitle, totalProtein, totalCarbs, totalFat, totalCalories);
        totalsBox.setPadding(new Insets(10));
        totalsBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 10;");
        // Tracked entered foods table
        Label dailyLogTitle = new Label("Today's Logged Foods");
        TableView<Void> trackedTable = new TableView<>();
        trackedTable.setPlaceholder(new Label("Placeholder text for empty table"));
        trackedTable.setPrefHeight(200);
        // Columns for info of entered foods in table
        TableColumn<Void, String> nameColumn = new TableColumn<>("Food");
        nameColumn.setPrefWidth(55);
        TableColumn<Void, String> proteinColumn = new TableColumn<>("Protein (g)");
        proteinColumn.setPrefWidth(80);
        TableColumn<Void, String> carbsColumn = new TableColumn<>("Carbs (g)");
        carbsColumn.setPrefWidth(80);
        TableColumn<Void, String> fatColumn = new TableColumn<>("Fat (g)");
        fatColumn.setPrefWidth(80);
        TableColumn<Void, String> caloriesColumn = new TableColumn<>("Calories");
        caloriesColumn.setPrefWidth(85);
        trackedTable.getColumns().addAll(nameColumn, proteinColumn, carbsColumn, fatColumn, caloriesColumn);
        // Export to JSON and food lookup buttons
        Button exportButton = new Button("Export to JSON file");
        Button openFoodLookup = new Button("Open Food Lookup");
        openFoodLookup.setOnAction(e -> showFoodLookup());
        VBox buttonsBox = new VBox(10, exportButton, openFoodLookup);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setAlignment(Pos.CENTER_LEFT);
        VBox rightColumn = new VBox(15, dailyLogTitle, trackedTable, totalsBox, buttonsBox);
        rightColumn.setPadding(new Insets(10));
        rightColumn.setPrefWidth(400);
        // Layout stuff
        HBox root = new HBox(20, rightColumn);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    // User food input section (Now in new window!!! Wowie!!!)
    private void showFoodLookup() {
        Stage foodStage = new Stage();
        foodStage.setTitle("Food Lookup");
        Label foodLabel = new Label("Food Lookup:");
        TextField foodInput = new TextField();
        Button searchButton = new Button("Search");
        Button addButton = new Button("Add to Daily Log");
         // Macronutrient preview box
        Label previewTitle = new Label("Macronutrient Preview of Selected Food");
        Label proteinPreview = new Label("Protein: -- g");
        Label carbsPreview = new Label("Carbs: -- g");
        Label fatPreview = new Label("Fat: -- g");
        Label calPreview = new Label("Calories: -- cal");
        VBox previewBox = new VBox(10, previewTitle, proteinPreview, carbsPreview, fatPreview, calPreview);
        previewBox.setPadding(new Insets(10));
        previewBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");
        VBox layout = new VBox(15, foodLabel, foodInput, searchButton, addButton, previewBox);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");
        Scene scene = new Scene(layout, 300, 350);
        foodStage.setScene(scene);
        foodStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
