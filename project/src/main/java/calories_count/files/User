package calories_count.files;

public class User {

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

    // Getters and Setters
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
}
