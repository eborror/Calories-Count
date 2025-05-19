package com.tully;

public class FoodItem {
    private String name;
    private double protein, fat, carbs, calories;

    public FoodItem(String name, double protein, double fat, double carbs, double calories) {
        if (protein < 0) {
            protein = 0;
        }
        if (fat < 0) {
            fat = 0;
        }
        if (carbs < 0) {
            carbs = 0;
        }
        if (calories < 0) {
            calories = 0;
        }
        
        this.name = name.strip();
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getCalories() {
        return calories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
}
