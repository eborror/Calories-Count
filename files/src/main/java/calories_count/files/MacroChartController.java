package calories_count.files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MacroChartController {

    @FXML private PieChart macroChart;
    @FXML private VBox chartBox;
    @FXML private VBox chartPlaceholder;
    @FXML private Label macroRatio;
    @FXML private Label macroRatioLabel;

    @FXML
    private void initialize() {
        double totalProtein = 0, totalCarbs = 0, totalFat = 0;
        String today = LocalDate.now().toString();
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
                        } catch (NumberFormatException ignored) {}
                    }

                    if (firstNumberIndex > 0 && tokens.length - firstNumberIndex >= 4) {
                        try {
                            double protein = Double.parseDouble(tokens[firstNumberIndex]);
                            double fat = Double.parseDouble(tokens[firstNumberIndex + 1]);
                            double carbs = Double.parseDouble(tokens[firstNumberIndex + 2]);

                            totalProtein += protein;
                            totalFat += fat;
                            totalCarbs += carbs;

                        } catch (NumberFormatException ignored) {
                            System.out.println("Skipping line (invalid numbers): " + line);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            chartPlaceholder.setVisible(true);
            chartPlaceholder.setManaged(true);
            macroChart.setVisible(false);
            macroChart.setManaged(false);
            macroRatio.setVisible(false);
            macroRatio.setManaged(false);
            macroRatioLabel.setVisible(false);
            macroRatioLabel.setManaged(false);
            return;
        }

        // Show "log food" button in place of chart if no foods have been logged today
        if (totalProtein == totalCarbs && totalCarbs == totalFat && totalFat == 0) {
            chartPlaceholder.setVisible(true);
            chartPlaceholder.setManaged(true);
            macroChart.setVisible(false);
            macroChart.setManaged(false);
            macroRatio.setVisible(false);
            macroRatio.setManaged(false);
            macroRatioLabel.setVisible(false);
            macroRatioLabel.setManaged(false);
        } else {
            chartPlaceholder.setVisible(false);
            chartPlaceholder.setManaged(false);
            macroChart.setVisible(true);
            macroChart.setManaged(true);
            macroRatio.setVisible(true);
            macroRatio.setManaged(true);
            macroRatioLabel.setVisible(true);
            macroRatioLabel.setManaged(true);

            macroChart.getData().clear();
            macroChart.getData().add(new PieChart.Data("Protein:", totalProtein));
            macroChart.getData().add(new PieChart.Data("Carbs:", totalCarbs));
            macroChart.getData().add(new PieChart.Data("Fat:", totalFat));

            DecimalFormat df = new DecimalFormat("0.0");
            for (PieChart.Data data : macroChart.getData()) {
                data.setName(data.getName() + " " + df.format(data.getPieValue()) + "g");
            }


            double proteinCalories = totalProtein * 4;
            double carbCalories = totalCarbs * 4;
            double fatCalories = totalFat * 9;
            // 1g Protein ~ 4 Cal, 1g Carb ~ 4 Cal, 1g Fat ~ 9 Cal
            double macroCalorieEstimate = proteinCalories + carbCalories + fatCalories;
            long proteinPercent = Math.round((proteinCalories / macroCalorieEstimate) * 100);
            long carbPercent = Math.round((carbCalories / macroCalorieEstimate) * 100);
            long fatPercent = Math.round((fatCalories / macroCalorieEstimate) * 100);

            long total = proteinPercent + carbPercent + fatPercent;
            // Ensure that percentages equal 100
            if (total > 100) {
                if (proteinPercent >= carbPercent && proteinPercent >= fatPercent) {
                    proteinPercent--;
                } else if (carbPercent >= fatPercent) {
                    carbPercent--;
                } else {
                    fatPercent--;
                }
            } else if (total < 100) {
                if (proteinPercent >= carbPercent && proteinPercent >= fatPercent) {
                    proteinPercent++;
                } else if (carbPercent >= fatPercent) {
                    carbPercent++;
                } else {
                    fatPercent++;
                }
            }

            macroRatio.setText(String.format("%d%%             %d%%             %d%%", Math.round(proteinPercent), Math.round(carbPercent), Math.round(fatPercent)));
        }
    }

    @FXML
    private void switchToFoodLookup() throws IOException {
        App.setRoot("FoodLookupView");
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("primary");
    }
}
