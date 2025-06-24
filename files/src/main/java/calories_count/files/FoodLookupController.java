package calories_count.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FoodLookupController {
    @FXML private TextField foodInput;
    @FXML private VBox resultsBox;
    @FXML private HBox resultsPlaceholderBox;
    @FXML private Label resultsPlaceholder;
    @FXML private VBox weightBox;
    @FXML private TextField weightInput;
    
    FoodList results;

    @FXML
    public void initialize() {
        weightBox.setVisible(false);
        weightBox.setManaged(false);
        resultsPlaceholder.setVisible(true);
        resultsPlaceholder.setManaged(true);
    }

    @FXML
    private void handleSearch() {
        handleSearch(1.0, true);
    }

    private void handleSearch(double scaleFactor, boolean newSearch) {
        // Remove current results, if they exist (do not remove the placeholder text)
        resultsBox.getChildren().removeIf(node -> node != resultsPlaceholderBox);
        
        String foodName = foodInput.getText().trim();
        if (foodName.isEmpty()) {
            resultsPlaceholder.setText("Search for a food!");
            resultsPlaceholderBox.setVisible(true);
            resultsPlaceholderBox.setManaged(true);
            weightBox.setVisible(false);
            weightBox.setManaged(false);

            return;
        }

        if (newSearch == true) {
            FoodWorker foodWorker = new FoodWorker(); // instantiate
            results = foodWorker.search(foodName); // get list
            weightInput.setText("100");
        }

        int length = results.length();
        final int MAX_RESULTS_LENGTH = 8;
        if (length == 0) {
            resultsPlaceholder.setText("No results found.");
            resultsPlaceholderBox.setVisible(true);
            resultsPlaceholderBox.setManaged(true);

            weightBox.setVisible(false);
            weightBox.setManaged(false);
            return;
        } else if (length > 5) {
            length = MAX_RESULTS_LENGTH;
        }

        resultsPlaceholderBox.setVisible(false);
        resultsPlaceholderBox.setManaged(false);
        weightBox.setVisible(true);
        weightBox.setManaged(true);
        for (int i = 0; i < length; i++) {
            FoodItem item = results.get(i);

            HBox row = new HBox(10);
            row.setPrefWidth(400);
            row.setFillHeight(true);
            

            String name = item.getName();
            if (name.length() > 40) {
                name = name.substring(0, 41) + "...";
            }

            Label label = new Label(String.format("%s\n%.1fg P, %.1fg F, %.1fg C, %d Cal",
                    name, 
                    item.getProtein() * scaleFactor,
                    item.getFat() * scaleFactor, 
                    item.getCarbs() * scaleFactor, 
                    Math.round(item.getCalories() * scaleFactor)
                    ));
            label.setWrapText(true);
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);

            Button addButton = new Button("Add");
            addButton.setMinWidth(60);
            addButton.setOnAction(e -> handleAdd(item, scaleFactor));

            row.getChildren().addAll(label, addButton);
            resultsBox.getChildren().add(row);
        }
    }

    @FXML
    private void onWeightChanged(KeyEvent event) {
        try {
            if (!weightInput.getText().strip().isEmpty()) {
                double value = Double.parseDouble(weightInput.getText());

                if (value <= 0) {
                    handleSearch(0.0, false);
                } else {
                    // Normalize scaleFactor (100g = 1.0 multiplier since all foodItem data is per 100g)
                    handleSearch(value/100.0, false);
                }
            } else {
                handleSearch(0.0, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAdd(FoodItem item, double scaleFactor) {
        if (item == null) return;
        item.scaleFood(scaleFactor);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("foodLog.txt", true))) {
            String today = java.time.LocalDate.now().toString(); // e.g. "2025-06-19"

            // Check if today's date header already exists
            List<String> existingLines = Files.readAllLines(Path.of("foodLog.txt"));
            boolean headerExists = existingLines.stream().anyMatch(line -> line.contains(today));

            if (!headerExists) {
                writer.write("===" + today + "===\n");
                writer.write("name protein fat carbs calories\n");
            }

            // Write the food item
            writer.write(String.format("%s %.1f %.1f %.1f %d\n",
                    item.getName(),
                    item.getProtein(),
                    item.getFat(),
                    item.getCarbs(),
                    Math.round(item.getCalories())));

            System.out.println("Food logged.");
        } catch (IOException e) {
            e.printStackTrace();
        }
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