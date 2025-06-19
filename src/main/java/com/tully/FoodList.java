package com.tully;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ArrayList wrapper class with useful food-related methods
 */
public class FoodList implements Iterable<FoodItem> {
    private ArrayList<FoodItem> foods;

    public FoodList() {
        this.foods = new ArrayList<FoodItem>();
    }

    public FoodList(FoodItem[] foods) {
        this.foods = new ArrayList<FoodItem>();
        for (FoodItem food : foods) {
            this.foods.add(food);
        }
    }

    public FoodList(ArrayList<FoodItem> foods) {
        this.foods = foods;
    }

    public Object[] getFoods() {
        return foods.toArray();
    }

    public void add(FoodItem food) {
        foods.add(food);
    }

    public void remove(FoodItem food) {
        foods.remove(food);
    }

    public void removeFromIndex(int index) {
        foods.remove(index);
    }

    public int length() {
        return foods.size();
    }

    public boolean contains(FoodItem food) {
        return foods.contains(food);
    }

    public double[] getFoodInfo() {
        // {protein, fat, carbs, calories}
        double[] info = new double[]{0.0, 0.0, 0.0, 0.0};
        for (FoodItem food : foods) {
            info[0] += food.getProtein();
            info[1] += food.getFat();
            info[2] += food.getCarbs();
            info[3] += food.getCalories();
        }

        return info;
    }

    @Override
    public String toString() {
        String outString = "";
        for (FoodItem food : foods) {
            outString += food + "\n";
        }

        return outString;
    }

    // Support for iterating over FoodList similar to an array
    @Override
    public Iterator<FoodItem> iterator() {
        return foods.iterator();
    }

    public void saveToJsonFile(String filePath) {
        JSONArray jsonArray = new JSONArray();

        for (FoodItem food : foods) {
            JSONObject jsonFood = new JSONObject();
            
            jsonFood.put("name", food.getName());
            jsonFood.put("protein", food.getProtein());
            jsonFood.put("fat", food.getFat());
            jsonFood.put("carbs", food.getCarbs());
            jsonFood.put("calories", food.getCalories());
            jsonArray.put(jsonFood);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonArray.toString(4));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromJsonFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray jsonArray = new JSONArray(tokener);

            foods.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonFood = jsonArray.getJSONObject(i);

                String name = jsonFood.getString("name");
                double protein = jsonFood.getDouble("protein");
                double fat = jsonFood.getDouble("fat");
                double carbs = jsonFood.getDouble("carbs");
                double calories = jsonFood.getDouble("calories");

                FoodItem food = new FoodItem(name, protein, fat, carbs, calories);
                foods.add(food);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}