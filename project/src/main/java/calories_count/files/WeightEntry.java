package calories_count.files;

import java.io.Serializable;
import java.time.LocalDate;

public class WeightEntry implements Serializable {
    private LocalDate date;
    private double weight;

    public WeightEntry(LocalDate date, double weight) {
        this.date = date;
        this.weight = weight;
    }

    public LocalDate getDate() { return date; }
    public double getWeight() { return weight; }

    @Override
    public String toString() {
        return date + ": " + weight + " lbs";
    }
}
