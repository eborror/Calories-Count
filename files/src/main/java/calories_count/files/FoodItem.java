package calories_count.files;

public class FoodItem {
    private String name;
    private double protein, fat, carbs, calories;

    public FoodItem() {
        this.name = "";
        this.protein = this.fat = this.carbs = this.calories = 0.0;
    }

    public FoodItem(String name, double protein, double fat, double carbs, double calories) {
        if (protein < 0) {
            protein = 0;
        }
        if (fat < 0) {
            fat = 0;
        }
        if (carbs < 0) {
            carbs = 0;
        }
        if (calories < 0) {
            calories = 0;
        }
        
        this.name = name.strip();
        // Round values to two decimal places
        this.protein = Math.round(protein * 100.0) / 100.0;
        this.fat = Math.round(fat * 100.0) / 100.0;
        this.carbs = Math.round(carbs * 100.0) / 100.0;
        this.calories = Math.round(calories * 100.0) / 100.0;
    }

    public double[] getFoodInfo() {
        return new double[]{protein, fat, carbs, calories};
    }


    public String getName() {
        return name;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getCalories() {
        return calories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "%-45s %6.1fg %7.1fg %8.1fg %8.0f\n".formatted(name, this.getProtein(), this.getFat(), this.getCarbs(), this.getCalories());
    }
}