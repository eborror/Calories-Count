package calories_count.files;

//***********************************************************************
//  The User class represents a user and stores their health information.
//  Contains methods for calculating BMI/BMR/TDEE, and maintains a weight
//  log.
//***********************************************************************

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // User info
    private final String name;
    private final int age;
    private final String gender;
    private final double weight; // pounds
    private final double height; // inches
    private final String activityLevel;
    private final String calorieGoal;
    private final List<WeightEntry> weightLog = new ArrayList<>();

    public User(String calorieGoal, String name, int age, String gender, double weight, double height, String activityLevel) {
        this.calorieGoal = calorieGoal;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.activityLevel = activityLevel;
    }

    // Getters for user info
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public String getActivityLevel() { return activityLevel; }
    public String getCalorieGoal() { return calorieGoal; }
    public List<WeightEntry> getWeightLog() { return weightLog; }

    // Calculates the user's Body Mass Index (BMI)
    public double calculateBMI() {
        if (height == 0) return 0;
        return (weight / (height * height)) * 703;
    }

    // Calculates the user's Basal Metabolic Rate (BMR)
    public double calculateBMR() {
        if (gender.equalsIgnoreCase("male")) {
            return 66 + (6.23 * weight) + (12.7 * height) - (6.8 * age);
        } else {
            return 655 + (4.35 * weight) + (4.7 * height) - (4.7 * age);
        }
    }

    // Calculates the user's Total Daily Energy Expenditure (TDEE)
    // Based on their BMR and activity level
    public double calculateTDEE() {
        double bmr = calculateBMR();
        double multiplier;
        
        int goalOffset;
        if (this.getCalorieGoal().equals("Moderate Bulk")) {
            goalOffset = 500;
        } else if (this.getCalorieGoal().equals("Light Bulk")) {
            goalOffset = 250;
        } else if (this.getCalorieGoal().equals("Light Cut")) {
            goalOffset = -250;
        } else if (this.getCalorieGoal().equals("Moderate Cut")) {
            goalOffset = -500;
        } else {
            goalOffset = 0;
        }
        String level = activityLevel.toLowerCase();

        switch (level) {
            case "sedentary":
                multiplier = 1.2;
                break;
            case "lightly active":
                multiplier = 1.375;
                break;
            case "moderately active":
                multiplier = 1.55;
                break;
            case "very active":
                multiplier = 1.725;
                break;
            case "super active":
                multiplier = 1.9;
                break;
            default:
                multiplier = 1.2; // default
                break;
        }

        return bmr * multiplier + goalOffset;
    }

    // Adds a new entry to the user's weight log with the current date
    public void addWeightEntry(double weight) {
        weightLog.add(new WeightEntry(LocalDate.now(), weight));
    }
}
