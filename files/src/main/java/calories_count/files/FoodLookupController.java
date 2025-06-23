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

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FoodLookupController {

    @FXML private TextField foodInput;
    @FXML private Label proteinPreview;
    @FXML private Label carbsPreview;
    @FXML private Label fatPreview;
    @FXML private Label calPreview;
    @FXML private VBox resultsBox;
    @FXML private Label resultsPlaceholder;

    
    @FXML
    private void handleSearch() {
        resultsBox.getChildren().clear();

        String foodName = foodInput.getText().trim();
        if (foodName.isEmpty()) {
            resultsBox.getChildren().add(new Label("Search for a food!"));
            return;
        }

        FoodWorker foodWorker = new FoodWorker(); // instantiate
        FoodList results = foodWorker.search(foodName); // get list

        int length = results.length();
        final int MAX_RESULTS_LENGTH = 10;
        if (length == 0) {
            resultsBox.getChildren().add(new Label("No results found."));

            proteinPreview.setText("Protein: ? g");
            carbsPreview.setText("Carbs: ? g");
            fatPreview.setText("Fat: ? g");
            calPreview.setText("Calories: ? cal");
            return;
        } else if (length > 5) {
            length = MAX_RESULTS_LENGTH;
        }

        for (int i = 0; i < length; i++) {
            FoodItem item = results.get(i);
            HBox row = new HBox(10);
            row.setPrefWidth(400);
            row.setFillHeight(true);

            String name = item.getName();
            if (name.length() > 50) {
                name = name.substring(0, 17) + "...";
            }

            Label label = new Label(String.format("%s\n%.1fg P, %.1fg F, %.1fg C, %.0f Cal",
                    name, item.getProtein(), item.getFat(), item.getCarbs(), item.getCalories()));
            label.setWrapText(true);
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);

            Button addButton = new Button("Add");
            addButton.setMinWidth(60);
            addButton.setOnAction(e -> handleAdd(item));

            row.getChildren().addAll(label, addButton);
            resultsBox.getChildren().add(row);
        }
    }

    private void handleAdd(FoodItem item) {
        String foodName = foodInput.getText().trim();

        FoodWorker foodWorker = new FoodWorker(); // instantiate
        FoodList results = foodWorker.search(foodName); // get list

        if (item == null) return;

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
            writer.write(String.format("%s %.1f %.1f %.1f %.0f\n",
                    item.getName(),
                    item.getProtein(),
                    item.getFat(),
                    item.getCarbs(),
                    item.getCalories()));

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