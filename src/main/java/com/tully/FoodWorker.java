package com.tully;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FoodWorker {
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// CLEARTEXT API keys should NEVER be stored like this in production code!!!!
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // However, I am putting my API key in this code on PURPOSE
    // (because we don't have a server running this where I can store environment variables)
    // (and I need the group to be able to use the API key for testing)
    // Please don't abuse it thank you :)
    private static final String API_KEY = "vBk2rRWZ4Xv3uGFLcequgOEv4iOTyXCCf1IKdzwc";

    private ArrayList<FoodItem> customFoods;

    private ArrayList<FoodItem[]> customMeals; 

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

    public FoodWorker() {
        customFoods = new ArrayList<>();
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
     * @return a LinkedHashMap where each key is a food item (String) and each value a double[] of elements in the order {Protein, Fat, Carbs, Calories}. This map maintains insertion order of keys.
     */
    // TODO: REFACTOR THIS CODE TO WORK WITH NEW FoodItem objects (for better readability + maybe better performance)
    public LinkedHashMap<String, double[]> search(String foodQuery, int resultCount) {
        // API INFO/ESSENTIALS:
        // https://fdc.nal.usda.gov/api-guide
        // https://app.swaggerhub.com/apis/fdcnal/food-data_central_api/1.0.1#/FDC/getFoodsSearch

        // "dataType" in our urlString restricts the output to one of the following databases:
        // Foundation, Survey%20%28FNDDS%29, Branded, SR%20Legacy
        // More info in the links above (It is likely that this value will not need to change)
        String dataType = "Survey%20%28FNDDS%29";

        LinkedHashMap<String, double[]> foodOut = new LinkedHashMap<>();
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
                return new LinkedHashMap<>();
            }
            if (br != null) { br.close(); }
            if (con != null) { con.disconnect(); }

            if (jo.getInt("totalHits") == 0) {
                System.out.println("FOOD SEARCH WARNING: SEARCH '" + foodQuery + "' RETURNED NO RESULTS.");
                return new LinkedHashMap<>();
            }
 
            double protein, fat, carbs, cals;
            if (jo != null) {
                protein = fat = carbs = cals = 0.0;
                JSONArray foodArr = jo.getJSONArray("foods");
                
                // Return a map whose keys are sorted by closest match to the user's input
                // and whose values are arrays of the macros + calories. i.e. foodItem -> [Protein, Fat, Carbs, Calories]
                for (int i = 0; i < foodArr.length(); i++) {
                    // Get the food returned by our request to the database
                    JSONObject food = foodArr.getJSONObject(i);
                    String foodName = food.getString("description").strip().replace(", NFS", "");

                    // Take the macronutrients and the calories
                    JSONArray nutrientArray = food.getJSONArray("foodNutrients");
                    
                    for (int j = 0; j < nutrientArray.length(); j++) {
                        protein = nutrientArray.getJSONObject(0).optDouble("value", 0.0);
                        fat = nutrientArray.getJSONObject(1).optDouble("value", 0.0);
                        carbs = nutrientArray.getJSONObject(2).optDouble("value", 0.0);
                        cals = nutrientArray.getJSONObject(3).optDouble("value", 0.0);
                    }

                    // Add the food name as a key and its info array as the value (String -> double[4])
                    double[] outList = {protein, fat, carbs, cals};
                    foodOut.put(foodName, outList);
                }
            }
        } catch (IOException e) {
            System.out.println("FOOD QUERY ERROR: " + e);
        }

        return foodOut;
    }

    public LinkedHashMap<String, double[]> search(String foodQuery) {
        return search(foodQuery, 10);
    }

    // TODO: This is for testing, and it may be removed in the future
    public void printTable(LinkedHashMap<String, double[]> map) {
        System.out.println("|---------------------------------------------|--------|--------|--------|------------|");
        System.out.println("    %-37s %12s %6s %9s %11s".formatted("Food", "Protein", "Fat", "Carbs", "Calories"));
        System.out.println("|---------------------------------------------|--------|--------|--------|------------|");

        for (Map.Entry<String, double[]> entry : map.entrySet()) {
            String key = entry.getKey();
            double[] values = entry.getValue();

            // Name wraparound for long entries
            if (key.length() <= 42) {
                System.out.printf("%-45s %6.1fg %7.1fg %8.1fg %8.0f\n", key, values[0], values[1], values[2], values[3]);
            } else if (key.length() <= 84) {
                System.out.printf("%-45s %6.1fg %7.1fg %8.1fg %8.0f\n", key.substring(0,42), values[0], values[1], values[2], values[3]);
                System.out.printf("%-42s\n", key.substring(42, Math.min(key.length(), 84)));
            } else if (key.length() <= 126) {
                System.out.printf("%-45s %6.1fg %7.1fg %8.1fg %8.0f\n", key.substring(0, 40), values[0], values[1], values[2], values[3]);
                System.out.printf("%-42s\n", key.substring(42, 84));
                System.out.printf("%-42s\n", key.substring(84, Math.min(key.length(), 126)));
            }
        }
    }

    /**
     * Calculate the total macronutrients + calories for any amount of foods.
     * Useful for calculating daily goals or other similar sums.
     * 
     * @param map a map of food items to their respective macronutrient + calorie arrays.
     * @return a double[] of food information in the order [Protein, Fat, Carbs, Calories].
     */
    public double[] calculateMacros(Map<String, double[]> map) {
        // The double[] is in the order {protein, fat, carbs, calories}
        double pSum, fSum, cSum, calSum;
        pSum = fSum = cSum = calSum = 0.0;

        for (Map.Entry<String, double[]> entry : map.entrySet()) {
            double[] values = entry.getValue();

            pSum += values[0];
            fSum += values[1];
            cSum += values[2];
            calSum += values[3];
        }

        return new double[]{pSum, fSum, cSum, calSum};
    }

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


    public FoodItem createCustomFood(String name, double protein, double fat, double carbs, double calories) {
        FoodItem customFood = new FoodItem(name, protein, fat, carbs, calories);
        customFoods.add(customFood);

        return customFood;
    }

    public void removeCustomFood(FoodItem food) {
        customFoods.remove(food);
    }

    public void createCustomMeal(FoodItem[] args) {
        customMeals.add(args);
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
}