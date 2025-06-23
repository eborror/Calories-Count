package calories_count.files;

//***********************************************************************
//  The WeightEntry class represents an entry in the user's weight log.
//***********************************************************************

import java.io.Serializable;
import java.time.LocalDate;

public class WeightEntry implements Serializable {
    private final LocalDate date;
    private final double weight;

    // Saves the date and weight # (in lbs) in an entry
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
