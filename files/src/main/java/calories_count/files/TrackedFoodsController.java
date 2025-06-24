package calories_count.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TrackedFoodsController implements Initializable {

    @FXML private TableView<FoodItem> trackedTable;
    @FXML private TableColumn<FoodItem, String> nameColumn;
    @FXML private TableColumn<FoodItem, Double> proteinColumn;
    @FXML private TableColumn<FoodItem, Double> carbsColumn;
    @FXML private TableColumn<FoodItem, Double> fatColumn;
    @FXML private TableColumn<FoodItem, Double> caloriesColumn;

    @FXML private Label totalProtein;
    @FXML private Label totalCarbs;
    @FXML private Label totalFat;
    @FXML private Label totalCalories;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        proteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));
        carbsColumn.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        fatColumn.setCellValueFactory(new PropertyValueFactory<>("fat"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));

        ObservableList<FoodItem> foodsToday = FXCollections.observableArrayList();
        double totalP = 0, totalC = 0, totalF = 0, totalCal = 0;

        String today = LocalDate.now().toString(); // e.g. 2025-06-19
        boolean onTodaySection = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("foodLog.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("===")) {
                    onTodaySection = line.contains(today);
                    continue;
                }
                if (onTodaySection && !line.isBlank() && !line.startsWith("name")) {
                    String[] tokens = line.trim().split("\\s+");

                    int firstNumberIndex = -1;
                    for (int i = 0; i < tokens.length; i++) {
                        try {
                            Double.parseDouble(tokens[i]);
                            firstNumberIndex = i;
                            break;
                        } catch (NumberFormatException ignored) {
                        }
                    }

                    if (firstNumberIndex > 0 && tokens.length - firstNumberIndex >= 4) {
                        StringBuilder nameBuilder = new StringBuilder();
                        for (int i = 0; i < firstNumberIndex; i++) {
                            nameBuilder.append(tokens[i]).append(" ");
                        }

                        try {
                            String name = nameBuilder.toString().trim();
                            double protein = Double.parseDouble(tokens[firstNumberIndex]);
                            double fat = Double.parseDouble(tokens[firstNumberIndex + 1]);
                            double carbs = Double.parseDouble(tokens[firstNumberIndex + 2]);
                            double calories = Double.parseDouble(tokens[firstNumberIndex + 3]);

                            FoodItem item = new FoodItem(name, protein, fat, carbs, calories);
                            foodsToday.add(item);
                            totalP += protein;
                            totalF += fat;
                            totalC += carbs;
                            totalCal += calories;

                        } catch (NumberFormatException e) {
                            System.out.println("Skipping line: " + line);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        trackedTable.setItems(foodsToday);
        totalProtein.setText(String.format("Protein: %.1fg", totalP));
        totalCarbs.setText(String.format("Carbs: %.1fg", totalC));
        totalFat.setText(String.format("Fat: %.1fg", totalF));
        totalCalories.setText(String.format("Calories: %d", Math.round(totalCal)));
    }

    /*@FXML
    private void handleExport() {
        System.out.println("Exporting to JSON...hopefully");
    }*/

    @FXML
    private void openFoodLookup() {
        try {
            App.setRoot("FoodLookupView");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void returnToPrimary() {
        try {
            App.setRoot("primary");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
