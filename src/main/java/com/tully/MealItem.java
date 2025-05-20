package com.tully;

import java.util.ArrayList;

// TODO: Finish this up!
public class MealItem {
    private String name;
    private ArrayList<FoodItem> foods;

    public MealItem(String name, ArrayList<FoodItem> foods) {
        this.name = name;
        this.foods = foods;

        System.out.println(this.calculateMacros(foods));
    }

    public double[] calculateMacros(FoodItem[] foodItems) {
        // The double[] is in the order {protein, fat, carbs, calories}
        double pSum, fSum, cSum, calSum;
        pSum = fSum = cSum = calSum = 0.0;

        for (FoodItem food : foodItems) {
            pSum += food.getProtein();
            fSum += food.getFat();
            cSum += food.getCarbs();
            calSum += food.getCalories();
        }

        return new double[]{pSum, fSum, cSum, calSum};
    }

    public double[] calculateMacros(ArrayList<FoodItem> foodItems) {
        // The double[] is in the order {protein, fat, carbs, calories}
        double pSum, fSum, cSum, calSum;
        pSum = fSum = cSum = calSum = 0.0;

        for (FoodItem food : foodItems) {
            pSum += food.getProtein();
            fSum += food.getFat();
            cSum += food.getCarbs();
            calSum += food.getCalories();
        }

        return new double[]{pSum, fSum, cSum, calSum};
    }
}
