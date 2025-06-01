package com.tully;

import java.util.ArrayList;

public class MealItem {
    private String name;
    private ArrayList<FoodItem> foods;

    public MealItem(String name, ArrayList<FoodItem> foodItems) {
        this.name = name;
        this.foods = foodItems;
    }

    public MealItem(String name) {
        this.name = name;
        this.foods = new ArrayList<>();
    }

    public MealItem() {
        this.name = "";
        this.foods = new ArrayList<>();
    }

    public void addFood(FoodItem food) {
        foods.add(food);
    }

    public void removeFood(FoodItem food) {
        if (foods.contains(food)) {
            foods.remove(food);
        } else {
            System.out.println("Found no occurrence of food %s in %s.".formatted(food.getName(), this.getName()));
        }
    }

    public String toString() {
        String out = "";
        for (FoodItem food : foods) {
            out += food;
        }

        return out;
    }

    public double[] calculateMacros() {
        // The double[] is in the order {protein, fat, carbs, calories}
        double pSum, fSum, cSum, calSum;
        pSum = fSum = cSum = calSum = 0.0;

        for (FoodItem food : foods) {
            pSum += food.getProtein();
            fSum += food.getFat();
            cSum += food.getCarbs();
            calSum += food.getCalories();
        }
        pSum = Math.round(pSum * 100.0) / 100.0;
        fSum = Math.round(fSum * 100.0) / 100.0;
        cSum = Math.round(cSum * 100.0) / 100.0;
        calSum = Math.round(calSum * 100.0) / 100.0;
        
        return new double[]{pSum, fSum, cSum, calSum};
    }

    public String getName() {
        return name;
    }

    public ArrayList<FoodItem> getFoods() {
        return foods;
    }
}
