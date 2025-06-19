//***********************************************************************
//   WeightEntry.java           House Tully
//
//  The WeightEntry class represents an entry in the user's weight log.
//***********************************************************************

package calories_count.files;

import java.io.Serializable;
import java.time.LocalDate;

public class WeightEntry implements Serializable {
    private LocalDate date;
    private double weight;

    // Saves the date and weight # (in lbs) in an entry
    public WeightEntry(LocalDate date, double weight) {
        this.date = date;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return date + ": " + weight + " lbs"; 
    }
}
