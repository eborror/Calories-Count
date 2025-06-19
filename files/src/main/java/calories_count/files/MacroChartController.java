package calories_count.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

public class MacroChartController {

    @FXML
    private PieChart macroChart;

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
        }

        macroChart.getData().clear();
        macroChart.getData().add(new PieChart.Data("Protein", totalProtein));
        macroChart.getData().add(new PieChart.Data("Carbs", totalCarbs));
        macroChart.getData().add(new PieChart.Data("Fat", totalFat));
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("primary");
    }
}
