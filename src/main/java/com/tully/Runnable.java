package com.tully;

import java.util.LinkedHashMap;

public class Runnable {
    public static void main(String[] args) {
        FoodWorker fd = new FoodWorker();

        LinkedHashMap<String, double[]> foods = fd.search("cheese", 10);

        fd.printTable(foods);
    }
}