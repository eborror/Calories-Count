// package com.tully;

// import java.util.ArrayList;
// import java.util.Iterator;

// public class MealList implements Iterable<MealItem> {
//     private ArrayList<MealItem> meals;

//     public MealList() {
//         this.meals = new ArrayList<MealItem>();
//     }

//     public MealList(MealItem[] meals) {
//         this.meals = new ArrayList<MealItem>();
//         for (MealItem meal : meals) {
//             this.meals.add(meal);
//         }
//     }

//     public MealList(ArrayList<MealItem> meals) {
//         this.meals = new ArrayList<>(meals);
//     }

//     public MealItem[] getMeals() {
//         return meals.toArray(new MealItem[0]);
//     }

//     public void add(MealItem meal) {
//         meals.add(meal);
//     }

//     public void remove(MealItem meal) {
//         meals.remove(meal);
//     }

//     public void removeFromIndex(int index) {
//         meals.remove(index);
//     }

//     public int length() {
//         return meals.size();
//     }

//     public boolean contains(MealItem meal) {
//         return meals.contains(meal);
//     }

//     @Override
//     public String toString() {
//         StringBuilder outString = new StringBuilder();
//         for (MealItem meal : meals) {
//             outString += meal;
//         }
//         return outString.toString();
//     }

//     @Override
//     public Iterator<MealItem> iterator() {
//         return meals.iterator();
//     }
// }
