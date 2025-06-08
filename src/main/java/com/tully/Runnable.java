package com.tully;

public class Runnable {
    public static void main(String[] args) {
        FoodWorker fw = new FoodWorker();

        FoodList foods = fw.search("white bread", 100);
        fw.printTable(foods);

        // FoodItem food1 = fw.createCustomFood("Literally Fat", 0, 99, 0, 1000);
        // FoodItem food2 = fw.createCustomFood("Tushonka Beef Stew", 15, 5, 0, 75);
        // FoodItem food3 = fw.createCustomFood("Sugar water", 0, 0, 30, 250);
        // FoodItem food4 = fw.createCustomFood("Awesome custom food item", 4, 7, 9, 400);

        // MealItem testMeal = fw.createCustomMeal("My meal", new FoodList(new FoodItem[]{food1, food2, food3, food4}));
        // System.out.println(testMeal);
        // System.out.println(Arrays.toString(testMeal.calculateMacros()));
    }
}