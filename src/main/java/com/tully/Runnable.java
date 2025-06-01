package com.tully;

import java.util.ArrayList;
import java.util.Arrays;

import com.tully.FoodWorker.DataType;

public class Runnable {
    public static void main(String[] args) {
        FoodWorker fd = new FoodWorker();

        // ArrayList<FoodItem> foods = fd.search("white bread", 10);
        // fd.printTable(foods);

        FoodItem food1 = fd.createCustomFood("Ball of lard", 0, 99, 0, 1000);
        FoodItem food2 = fd.createCustomFood("Tushonka Beef Stew", 15, 5, 0, 75);
        FoodItem food3 = fd.createCustomFood("Sugar water", 0, 0, 30, 250);
        FoodItem food4 = fd.createCustomFood("Awesome custom food item", 4, 7, 9, 400);

        MealItem testMeal = fd.createCustomMeal("My meal", new ArrayList<FoodItem>(Arrays.asList(food1, food2, food3, food4)));

        System.out.println(testMeal);
        System.out.println(Arrays.toString(testMeal.calculateMacros()));
    }
}