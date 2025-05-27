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
        // User food input section 
        Label foodLabel = new Label("Food Lookup:");
        TextField foodInput = new TextField();
        Button searchButton = new Button("Search");
        Button addButton = new Button("Add to Daily Log");
        VBox inputSection = new VBox(15, foodLabel, foodInput, searchButton, addButton);
        inputSection.setPadding(new Insets(10));
        inputSection.setAlignment(Pos.TOP_LEFT);
        inputSection.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");
        // Info of selected food from database (Idk how we're going to do this, or if we can)
        Label previewTitle = new Label("Macronutrient Preview of Selected Food");
        Label proteinPreview = new Label("Protein: -- g");
        Label carbsPreview = new Label("Carbs: -- g");
        Label fatPreview = new Label("Fat: -- g");
        Label calPreview = new Label("Calories: -- cal");
        VBox previewBox = new VBox(5, previewTitle, proteinPreview, carbsPreview, fatPreview, calPreview);
        previewBox.setPadding(new Insets(10));
        previewBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");
        // Tracked entered foods table
        Label dailyLogTitle = new Label("Today's Tracked Foods");
        TableView<Void> trackedTable = new TableView<>();
        trackedTable.setPlaceholder(new Label("Placeholder text for empty table"));
        trackedTable.setPrefHeight(200);
        // Columns for info of entered foods in table
        TableColumn<Void, String> nameCol = new TableColumn<>("Food");
        nameCol.setPrefWidth(55);
        TableColumn<Void, String> proteinCol = new TableColumn<>("Protein (g)");
        proteinCol.setPrefWidth(80);
        TableColumn<Void, String> carbsCol = new TableColumn<>("Carbs (g)");
        carbsCol.setPrefWidth(80);
        TableColumn<Void, String> fatCol = new TableColumn<>("Fat (g)");
        fatCol.setPrefWidth(80);
        TableColumn<Void, String> caloriesCol = new TableColumn<>("Calories");
        caloriesCol.setPrefWidth(85);
        trackedTable.getColumns().addAll(nameCol, proteinCol, carbsCol, fatCol, caloriesCol);
        // Summary of total daily stats
        Label totalsTitle = new Label("Daily Totals");
        Label totalProtein = new Label("Protein: -- g");
        Label totalCarbs = new Label("Carbs: -- g");
        Label totalFat = new Label("Fat: -- g");
        Label totalCalories = new Label("Calories: --");
        VBox totalsBox = new VBox(15, totalsTitle, totalProtein, totalCarbs, totalFat, totalCalories);
        totalsBox.setPadding(new Insets(10));
        totalsBox.setStyle("-fx-border-color: lightgray; -fx-border-radius: 10;");
        // Export button to JSON (I think that's the filetype we're doing)
        Button exportButton = new Button("Export to JSON file");
        VBox rightColumn = new VBox(15, dailyLogTitle, trackedTable, totalsBox, exportButton);
        rightColumn.setPadding(new Insets(10));
        rightColumn.setPrefWidth(400);
        // Full layout stuff
        VBox leftColumn = new VBox(20, inputSection, previewBox);
        leftColumn.setPadding(new Insets(10));
        HBox root = new HBox(20, leftColumn, rightColumn);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
