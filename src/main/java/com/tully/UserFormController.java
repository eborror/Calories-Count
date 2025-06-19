package com.tully;

import java.io.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.application.Platform;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderBox.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        activityBox.setItems(FXCollections.observableArrayList("Sedentary", "Lightly Active",
                "Moderately Active", "Very Active", "Extra Active"));

        User previousUser = loadUserFromFile();
        if (previousUser != null) {
            nameField.setText(previousUser.getName());
            ageField.setText(String.valueOf(previousUser.getAge()));
            genderBox.setValue(previousUser.getGender());
            weightField.setText(String.valueOf(previousUser.getWeight()));
            heightField.setText(String.valueOf(previousUser.getHeight()));
            activityBox.setValue(previousUser.getActivityLevel());

            bmiLabel.setText(String.format("BMI: %.2f", previousUser.calculateBMI()));
            tdeeLabel.setText(String.format("TDEE: %.2f", previousUser.calculateTDEE()));

            App.currentUser = previousUser;
            updateWeightHistory();
        }

        Platform.runLater(() -> genderBox.requestFocus()); // Keep name prompt text visible
    }

    @FXML
    private void handleCalculate() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String gender = genderBox.getValue();
        double weight = Double.parseDouble(weightField.getText());
        double height = Double.parseDouble(heightField.getText());
        String activityLevel = activityBox.getValue();

        User user = new User(name, age, gender, weight, height, activityLevel);
        App.currentUser = user;

        saveUserToFile(user);

        double bmi = user.calculateBMI();
        double tdee = user.calculateTDEE();

        bmiLabel.setText(String.format("BMI: %.2f", bmi));
        tdeeLabel.setText(String.format("TDEE: %.2f", tdee));
    }

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

    private void saveUserToFile(User user) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("user.dat"))) {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User loadUserFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("user.dat"))) {
            return (User) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null; // Return null if file doesn't exist or error occurs
        }
    }

}

