package calories_count.files;

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
    @FXML private Label bmrLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderBox.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));

        User previousUser = loadUserFromFile();
        if (previousUser != null) {
            nameField.setText(previousUser.getName());
            ageField.setText(String.valueOf(previousUser.getAge()));
            genderBox.setValue(previousUser.getGender());
            weightField.setText(String.valueOf(previousUser.getWeight()));
            heightField.setText(String.valueOf(previousUser.getHeight()));

            bmiLabel.setText(String.format("BMI: %.2f", previousUser.calculateBMI()));
            bmrLabel.setText(String.format("BMR: %.2f", previousUser.calculateBMR()));
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

        User user = new User(name, age, gender, weight, height);
        App.currentUser = user;

        saveUserToFile(user);

        double bmi = user.calculateBMI();
        double bmr = user.calculateBMR();

        bmiLabel.setText(String.format("BMI: %.2f", bmi));
        bmrLabel.setText(String.format("BMR: %.2f", bmr));
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
