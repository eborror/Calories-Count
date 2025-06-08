// package com.tully;

// public class MealItem {
//     private String name;
//     private MealList meals;

//     public MealItem(String name, MealList meals) {
//         this.name = name;
//         this.meals = meals;
//     }

//     public MealItem(String name, MealItem[] mealItems) {
//         this.name = name;
//         this.meals = new MealList();
//     }

//     public MealItem(String name) {
//         this.name = name;
//         this.meals = new MealList();
//     }

//     public MealItem() {
//         this.name = "";
//         this.meals = new MealList();
//     }

//     public void addFood(MealItem meal) {
//         meals.add(meal);
//     }

//     public void removeFood(MealItem meal) {
//         if (meals.contains(meal)) {
//             meals.remove(meal);
//         } else {
//             System.out.println("Found no occurrence of meal %s in %s.".formatted(meal.getName(), this.getName()));
//         }
//     }

//     public String toString() {
//         String out = "";
//         for (MealItem meal : meals) {
//             out += meal;
//         }

//         return out;
//     }

//     public double[] calculateMacros() {
//         // The double[] is in the order {protein, fat, carbs, calories}
//         double pSum, fSum, cSum, calSum;
//         pSum = fSum = cSum = calSum = 0.0;

//         for (MealItem meal : meals) {
//             pSum += food.getProtein();
//             fSum += food.getFat();
//             cSum += food.getCarbs();
//             calSum += food.getCalories();
//         }
//         pSum = Math.round(pSum * 100.0) / 100.0;
//         fSum = Math.round(fSum * 100.0) / 100.0;
//         cSum = Math.round(cSum * 100.0) / 100.0;
//         calSum = Math.round(calSum * 100.0) / 100.0;
        
//         return new double[]{pSum, fSum, cSum, calSum};
//     }

//     public String getName() {
//         return name;
//     }

//     public MealList getMeals() {
//         return meals;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public void setMeals(MealList foods) {
//         this.meals = meals;
//     }
// }
