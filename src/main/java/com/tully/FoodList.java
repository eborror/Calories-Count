package com.tully;

import java.util.ArrayList;
import java.util.Iterator;

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

    @Override
    public String toString() {
        String outString = "";
        for (FoodItem food : foods) {
            outString += food;
        }

        return outString;
    }

    // Support for iterating over FoodList similar to an array
    @Override
    public Iterator<FoodItem> iterator() {
        return foods.iterator();
    }
}