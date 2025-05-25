package calories_count.files;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int age;
    private String gender;
    private double weight; // pounds
    private double height; // inches

    public User(String name, int age, String gender, double weight, double height) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }

    // Method for calculating BMI
    public double calculateBMI() {
        if (height == 0) return 0;
        return (weight / (height * height)) * 703;
    }

    // Method for calculating BMR
    public double calculateBMR() {
        if (gender.equalsIgnoreCase("male")) {
            return 66 + (6.23 * weight) + (12.7 * height) - (6.8 * age);
        } else {
            return 655 + (4.35 * weight) + (4.7 * height) - (4.7 * age);
        }
    }
}
