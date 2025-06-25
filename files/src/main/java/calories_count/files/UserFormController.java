package calories_count.files;

//***********************************************************************
//  The UserFormController class handles user input for personal data,
//  calculates BMI/TDEE, and manages weight history logging.
//***********************************************************************

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class UserFormController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderBox;
    @FXML private TextField weightField;
    @FXML private TextField heightField;
    @FXML private Label bmiLabel;
    @FXML private ComboBox<String> activityBox;
    @FXML private Label tdeeLabel;
    @FXML private TextField logWeightField;
    @FXML private ListView<String> weightHistoryList;
    @FXML private ComboBox<String> goalBox;

    // Initializes form by setting combo box values and loading existing user info if available
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderBox.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        activityBox.setItems(FXCollections.observableArrayList("Sedentary", "Lightly Active",
                "Moderately Active", "Very Active", "Extra Active"));
        goalBox.setItems(FXCollections.observableArrayList("Cut", "Maintain", "Bulk"));

        User previousUser = loadUserFromFile();
        if (previousUser != null) {
            // Fill the form with saved user info
            nameField.setText(previousUser.getName());
            ageField.setText(String.valueOf(previousUser.getAge()));
            genderBox.setValue(previousUser.getGender());
            weightField.setText(String.valueOf(previousUser.getWeight()));
            heightField.setText(String.valueOf(previousUser.getHeight()));
            activityBox.setValue(previousUser.getActivityLevel());
            goalBox.setValue(previousUser.getCalorieGoal());

            int goalOffset;
            String calorieGoal = previousUser.getCalorieGoal();
            if (calorieGoal == "Bulk") {
                goalOffset = 500;
            } else if (calorieGoal == "Cut") {
                goalOffset = -500;
            } else {
                goalOffset = 0;
            }

            double bmi = previousUser.calculateBMI();
            String bmiCategory;
            if (bmi < 18.5) {
                bmiCategory = "(Underweight)";
            } else if (bmi < 24.9) {
                bmiCategory = "(Healthy Weight)";
            } else if (bmi < 29.9) {
                bmiCategory = "(Overweight)";
            } else if (bmi < 34.9) {
                bmiCategory = "(Class 1 Obesity)";
            } else if (bmi < 39.9) {
                bmiCategory = "(Class 2 Obesity)";
            } else {
                bmiCategory = "(Class 3 Obesity)";
            }
            bmiLabel.setText(String.format("BMI: %.2f %s", bmi, bmiCategory));
            tdeeLabel.setText(String.format("TDEE: %d Calories", Math.round(previousUser.calculateTDEE())));

            App.currentUser = previousUser;
            updateWeightHistory();
        }

        Platform.runLater(() -> genderBox.requestFocus()); // Keep name prompt text visible
    }

    // Handles the 'calculate' button click
    // Utilizes user info to create a new instance of 'User', and sets value of BMI/TDEE to display
    @FXML
    private void handleCalculate() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String gender = genderBox.getValue();
        double weight = Double.parseDouble(weightField.getText());
        double height = Double.parseDouble(heightField.getText());
        String activityLevel = activityBox.getValue();
        String calorieGoal = goalBox.getValue();

        User user = new User(calorieGoal, name, age, gender, weight, height, activityLevel);
        App.currentUser = user;

        int goalOffset;
        if (calorieGoal == "Bulk") {
            goalOffset = 500;
        } else if (calorieGoal == "Cut") {
            goalOffset = -500;
        } else {
            goalOffset = 0;
        }
        saveUserToFile(user);

        double bmi = user.calculateBMI();
        double tdee = user.calculateTDEE();
        String bmiCategory;
        if (bmi < 18.5) {
            bmiCategory = "(Underweight)";
        } else if (bmi < 24.9) {
            bmiCategory = "(Healthy Weight)";
        } else if (bmi < 29.9) {
            bmiCategory = "(Overweight)";
        } else if (bmi < 34.9) {
            bmiCategory = "(Class 1 Obesity)";
        } else if (bmi < 39.9) {
            bmiCategory = "(Class 2 Obesity)";
        } else {
            bmiCategory = "(Class 3 Obesity)";
        }
        bmiLabel.setText(String.format("BMI: %.2f %s", bmi, bmiCategory));
        tdeeLabel.setText(String.format("TDEE: %d Calories", Math.round(tdee)));
    }

    // Handles the 'log weight' button click
    // Adds a new entry to the user's weight log and updates the display
    @FXML
    private void handleLogWeight() {
        if (App.currentUser == null) return;

        try {
            double newWeight = Double.parseDouble(logWeightField.getText());
            App.currentUser.addWeightEntry(newWeight);
            updateWeightHistory();
            saveUserToFile(App.currentUser);
            logWeightField.clear();
        } catch (NumberFormatException ignored) {
        }
    }

    // Updates the list view with the current weight log
    private void updateWeightHistory() {
        weightHistoryList.getItems().clear();
        for (WeightEntry entry : App.currentUser.getWeightLog()) {
            weightHistoryList.getItems().add(entry.toString());
        }
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("primary");
    }

    // Saves user data to a file called 'user.dat'
    private void saveUserToFile(User user) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.dat"))) {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads user data from the local file if available
    private User loadUserFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.dat"))) {
            return (User) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null; // Return null if file doesn't exist or error occurs
        }
    }

}
