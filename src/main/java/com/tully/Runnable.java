package com.tully;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Runnable {
    public static void main(String[] args) {
        FoodWorker fw = new FoodWorker();

        FoodList foods = fw.search("white bread", 10);
        fw.printTable(foods);

        FoodItem food1 = new FoodItem("Literally Fat", 0, 99, 0, 1000);
        FoodItem food2 = new FoodItem("Tushonka Beef Stew", 15, 5, 0, 75);
        FoodItem food3 = new FoodItem("Sugar water", 0, 0, 30, 250);
        FoodItem food4 = new FoodItem("Awesome custom food item", 4, 7, 9, 400);

        FoodList twoFoods = new FoodList(new FoodItem[]{food1, food2, food3, food4});

        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String classpath = System.getProperty("java.class.path");
        String pathSeparator = System.getProperty("path.separator");
        String path = classpath.split(pathSeparator)[0];
        
        path += "/" + formattedDate;
        twoFoods.saveToJsonFile(path);

        FoodList foodsFromFile = new FoodList();
        foodsFromFile.loadFromJsonFile(path);

        System.out.println("\n\nFrom file: " + path + "\n" + foodsFromFile);
    }
}