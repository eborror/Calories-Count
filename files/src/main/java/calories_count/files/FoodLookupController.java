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

public class FoodLookupController {

    @FXML private TextField foodInput;
    @FXML private Label proteinPreview;
    @FXML private Label carbsPreview;
    @FXML private Label fatPreview;
    @FXML private Label calPreview;

    @FXML
    private void handleSearch() {
        String foodName = foodInput.getText().trim();

        FoodWorker foodWorker = new FoodWorker(); // instantiate
        FoodList results = foodWorker.search(foodName); // get list
        FoodItem item = results.length() > 0 ? results.get(0) : null; // get first match

        if (item != null) {
            proteinPreview.setText("Protein: " + item.getProtein() + " g");
            carbsPreview.setText("Carbs: " + item.getCarbs() + " g");
            fatPreview.setText("Fat: " + item.getFat() + " g");
            calPreview.setText("Calories: " + item.getCalories() + " cal");
        } else {
            proteinPreview.setText("Protein: ? g");
            carbsPreview.setText("Carbs: ? g");
            fatPreview.setText("Fat: ? g");
            calPreview.setText("Calories: ? cal");
        }
    }

    @FXML
    private void handleAdd() {
        String foodName = foodInput.getText().trim();

        FoodWorker foodWorker = new FoodWorker(); // instantiate
        FoodList results = foodWorker.search(foodName); // get list
        FoodItem item = results.length() > 0 ? results.get(0) : null; // get first match

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