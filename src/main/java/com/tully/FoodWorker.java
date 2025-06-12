package com.tully;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URI;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class FoodWorker {
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// CLEARTEXT API keys should NEVER be stored like this in production code!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // However, I am putting my API key in this code on PURPOSE
    // (because we don't have a server running this where I can store environment variables)
    // (and I need the group to be able to use the API key for testing)
    // Please don't abuse it thank you :)
    private static final String API_KEY = "vBk2rRWZ4Xv3uGFLcequgOEv4iOTyXCCf1IKdzwc";

    // private FoodList customFoods;
    // private MealList customMeals;

    public enum Sex {
        MALE,
        FEMALE
    }

    // Activity level explanation:
    // Sedentary = little to no exercise in a day
    // Light = light exercise/sports 1-3 days/week
    // Moderate = moderate exercise/sports 3-5 days/week
    // Heavy = hard exercise/sports 6-7 days a week
    // Extreme = very hard exercise/sports and physical job OR training hard 2x a week
    public enum Activity {
        SEDENTARY,
        LIGHT,
        MODERATE,
        HEAVY,
        EXTREME
    }

    public enum DataType {
        FOUNDATION("Foundation"),
        SURVEY_FNDDS("Survey%20%28FNDDS%29"),
        BRANDED("Branded"),
        SR_LEGACY("SR%20Legacy");

        private final String value;

        DataType(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    public FoodWorker() {
        // customFoods = new FoodList();
        // customMeals = new MealList();
    }

    /**
     * Queries the USDA's food database for the macronutrients (protein, fat, and carbs) as well as
     * the calories of a food item. Supports search operators including:
     * Quotation marks "" to search for an exact phrase (i.e. "green pepper" will only match green pepper products, as opposed to anything with the word "green" or "pepper" in it)
     * Plus sign + to make a word required (i.e. +candy corn matches only foods that have candy and weighs foods that have "corn" higher than those that don't)
     * Minus sign - to exclude foods with a certain word (i.e. candy -chocolate matches "candy" that doesn't contain the word "chocolate")
     * Asterisk * wildcard character to match any non-whitespace (i.e. *berry matches all foods with berry at the end of the name)
     * Parentheses () denote grouping to signify precedence (i.e. pizza -(pepperoni sausage) matches all pizza with neither pepperoni nor sausage)
     * 
     * @param foodQuery the food to search for in the database.
     * @param resultCount the number of foods that will be returned in the search.
     * @return a FoodList of FoodItems.
     **/
    public FoodList search(String foodQuery, int resultCount) {
        // API INFO/ESSENTIALS:
        // https://fdc.nal.usda.gov/api-guide
        // https://app.swaggerhub.com/apis/fdcnal/food-data_central_api/1.0.1#/FDC/getFoodsSearch

        // "dataType" in our urlString restricts the output to one of the following databases:
        // Foundation, Survey_FNDDS, Branded, and SR_Legacy
        // More info in the external links above
        DataType dataType = DataType.SURVEY_FNDDS;

        ArrayList<FoodItem> foodOut = new ArrayList<>();
        try {
            // Sanitize user input and validate other parameters
            foodQuery = URLEncoder.encode(foodQuery, StandardCharsets.UTF_8.toString());

            if (resultCount < 0) {
                throw new InvalidParameterException("ERROR: Page size must be at least zero.");
            } else  if (resultCount > 100) {
                System.out.println("WARNING: REQUEST SIZE LARGE. MAY CAUSE LAG");
            }
            
            String urlString = "https://api.nal.usda.gov/fdc/v1/foods/search"
                                + "?query=" + foodQuery
                                + "&dataType=" + dataType
                                + "&pageSize=" + resultCount
                                + "&sortBy=dataType.keyword"
                                + "&sortOrder=desc"
                                + "&api_key=" + API_KEY;

            // Convert the URL String to a URL object
            URL url = URI.create(urlString).toURL();
            
            // HttpUrlConnection uses GET by default
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            int status = con.getResponseCode();
            
            JSONObject jo = null;
            BufferedReader br = null;
            if (status == 200) {
                // Read the input stream from the response
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = br.readLine();

                // Convert the response to a JSONObject
                // (Our get request from /v1/foods/search will only have one line)
                jo = new JSONObject(line);
            } else {
                // Error code descriptions can be found here: https://api.data.gov/docs/developer-manual/
                System.out.println("FOOD SEARCH ERROR: HTTP CONNECTION FAILED WITH STATUS CODE " + status);
                return new FoodList();
            }
            if (br != null) { br.close(); }
            if (con != null) { con.disconnect(); }

            if (jo.getInt("totalHits") == 0) {
                System.out.println("FOOD SEARCH WARNING: SEARCH '" + foodQuery + "' RETURNED NO RESULTS.");
                return new FoodList();
            }
 
            double protein, fat, carbs, cals;
            if (jo != null) {
                protein = fat = carbs = cals = 0.0;
                JSONArray foodArr = jo.getJSONArray("foods");
                
                // Return a map whose keys are sorted by closest match to the user's input
                // and whose values are arrays of the macros + calories. i.e. foodItem -> [Protein, Fat, Carbs, Calories]
                for (int i = 0; i < foodArr.length(); i++) {
                    // Get the food returned by our request to the database
                    JSONObject foodJSON = foodArr.getJSONObject(i);
                    String foodName = foodJSON.getString("description").strip().replace(", NFS", "");

                    // Take the macronutrients and the calories
                    JSONArray nutrientArray = foodJSON.getJSONArray("foodNutrients");
                    
                    for (int j = 0; j < nutrientArray.length(); j++) {
                        protein = nutrientArray.getJSONObject(0).optDouble("value", 0.0);
                        fat = nutrientArray.getJSONObject(1).optDouble("value", 0.0);
                        carbs = nutrientArray.getJSONObject(2).optDouble("value", 0.0);
                        cals = nutrientArray.getJSONObject(3).optDouble("value", 0.0);
                    }

                    FoodItem food = new FoodItem(foodName, protein, fat, carbs, cals);
                    foodOut.add(food);
                }
            }
        } catch (IOException e) {
            System.out.println("FOOD QUERY ERROR: " + e);
        }

        return new FoodList(foodOut);
    }

    public FoodList search(String foodQuery) {
        return search(foodQuery, 10);
    }

    public FoodList search(String foodQuery, int resultCount, DataType dataType) {
        return search(foodQuery, resultCount, dataType);
    }

    public void printTable(FoodList foods) {
        System.out.println("|---------------------------------------------|--------|--------|--------|------------|");
        System.out.println("    %-37s %12s %6s %9s %11s".formatted("Food", "Protein", "Fat", "Carbs", "Calories"));
        System.out.println("|---------------------------------------------|--------|--------|--------|------------|");

        for (FoodItem food : foods) {
            String name = food.getName();
            double protein = food.getProtein();
            double fat = food.getFat();
            double carbs = food.getCarbs();
            double cals = food.getCalories();

            // Name wraparound for long entries
            if (name.length() <= 42) {
                System.out.printf("%-45s %6.1fg %7.1fg %8.1fg %8.0f\n", name, protein, fat, carbs, cals);
            } else if (name.length() <= 84) {
                System.out.printf("%-45s %6.1fg %7.1fg %8.1fg %8.0f\n", name.substring(0,42), protein, fat, carbs, cals);
                System.out.printf("%-42s\n", name.substring(42, Math.min(name.length(), 84)));
            } else if (name.length() <= 126) {
                System.out.printf("%-45s %6.1fg %7.1fg %8.1fg %8.0f\n", name.substring(0, 40), protein, fat, carbs, cals);
                System.out.printf("%-42s\n", name.substring(42, 84));
                System.out.printf("%-42s\n", name.substring(84, Math.min(name.length(), 126)));
            }
        }
    }

    /**
     * Calculate the total macronutrients + calories for any amount of foods.
     * Useful for calculating daily goals or other similar sums.
     * 
     * @param foods a map of food items to their respective macronutrient + calorie arrays.
     * @return a double[] of food information in the order [Protein, Fat, Carbs, Calories].
     */
    public double[] calculateMacros(FoodList foods) {
        double pSum, fSum, cSum, calSum;
        pSum = fSum = cSum = calSum = 0.0;

        for (FoodItem food : foods) {
            pSum += food.getProtein();
            fSum += food.getFat();
            cSum += food.getCarbs();
            calSum += food.getCalories();
        }

        return new double[]{pSum, fSum, cSum, calSum};
    }

    /**
     * Calculate the user's total daily energy expenditure given the following arguments:
     * 
     * @param weightKg Weight, in kilograms
     * @param heightCm Height, in centimeters
     * @param age Age, in years
     * @param sex Sex, (Sex.MALE or Sex.FEMALE)
     * @param activity Activity level, (Activity.SEDENTARY, Activity.LIGHT, Activity.MODERATE, Activity.HEAVY, Activity.EXTREME)
     * @return an estimate of the number of calories the user expends daily
     */
    public double dailyExpendedCalories(double weightKg, double heightCm, int age, Sex sex, Activity activity) {
        double totalExpenditure;
        if (sex == Sex.MALE) {
            totalExpenditure = (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5;
        } else {
            totalExpenditure = (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161;
        }

        switch (activity) {
            case SEDENTARY:
                return totalExpenditure * 1.2;
            case LIGHT:
                return totalExpenditure * 1.375;
            case MODERATE:
                return totalExpenditure * 1.55;
            case HEAVY:
                return totalExpenditure * 1.725;
            case EXTREME:
                return totalExpenditure * 1.9;
            default:
                return 0.0;
        }
    }

    /**
     * Calculate the user's BMI (body mass index)
     * 
     * @param weightKg Weight, in kilograms
     * @param heightCm Height, in centimeters
     */
    public double getBMI(double weightKg, double heightCm) {
        return weightKg / (heightCm * heightCm);
    }

    public double poundsToKilos(double lb) {
        return lb / 2.205;
    }

    public double kilosToPounds(double kg) {
        return kg * 2.205;
    }

    public double inchesToCm(double inches) {
        return inches * 2.54;
    }

    public double cmToInches(double cm) {
        return cm / 2.54;
    }

    /* CURRENTLY UNUSED (MAY BE FULLY IMPLEMENTED LATER?)
    public FoodItem createCustomFood(String name, double protein, double fat, double carbs, double calories) {
        FoodItem customFood = new FoodItem(name, protein, fat, carbs, calories);
        customFoods.add(customFood);

        return customFood;
    }


    public FoodItem createCustomFood(String name, double[] foodInfo) {
        if (foodInfo.length != 4) {
            System.out.println("ERROR: foodInfo array must be length 4. {Protein, Fat, Carbs, Calories}");
            return null;
        }
        FoodItem customFood = new FoodItem(name, foodInfo[0], foodInfo[1], foodInfo[2], foodInfo[3]);
        customFoods.add(customFood);

        return customFood;
    }

    public void removeCustomFood(FoodItem food) {
        if (customFoods.contains(food)) {
            customFoods.remove(food);
        }
    }

    public void removeCustomFood(String name) {
        for (FoodItem food : customFoods) {
            if (food.getName().equals(name)) {
                customFoods.remove(food);
            }
        }
    }

    public MealItem createCustomMeal(String name, FoodList foods) {
        MealItem meal = new MealItem(name, foods);
        customMeals.add(meal);

        return meal;
    }

    public void removeCustomMeal(MealItem meal) {
        if (customMeals.contains(meal)) {
            customMeals.remove(meal);
        }
    }

    public void removeCustomMeal(String name) {
        for (MealItem meal : customMeals) {
            if (meal.getName().equals(name)) {
                customMeals.remove(meal);
            }
        }
    }

    public FoodItem getCustomFood(String name) {
        for (FoodItem food : customFoods) {
            if (food.getName().equals(name)) {
                return food;
            }
        }

        // If no matching food item is found
        return new FoodItem();
    }

    public MealItem getCustomMeal(String name) {
        for (MealItem meal : customMeals) {
            if (meal.getName().equals(name)) {
                return meal;
            }
        }

        // If no matching meal item is found
        return new MealItem();
    }
    */
}