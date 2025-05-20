package com.tully;

import java.util.ArrayList;

public class Runnable {
    public static void main(String[] args) {
        FoodWorker fd = new FoodWorker();

        ArrayList<FoodItem> foods = fd.search("cheese", 10);

        fd.printTable(foods);

        fd.createCustomFood("Yummy yummy food", 10, 999, 0, 0);
        fd.createCustomMeal("My meal", foods);
    }
}