package parkingoffice;

import java.util.HashSet;

/**
 * A class that represent a parking lot. Every lot has a unique number and a collection
 * of the cars parked in it.
 *
 * @author Alex Lee
 */
public class LotData {

    /** A hashset consisting of cars; is used to know if a car has already been seen in the lot for the day */
    private HashSet<String> seenCars;

    /** the lot number of the Parking Lot */
    private final int lotNumber;

    /**
     * Gets the lot number associated with a Parking lot
     * @return the lot number
     * */
    public int getLotNumber() {
        return this.lotNumber;
    }

    /**
     * Constructor takes in the number of the lot.
     * @param n Lot number
     */
    public LotData(int n) {
        this.lotNumber = n;
        this.seenCars = new HashSet<String>();
    }

    /**
     * Returns a string reporting the number of unique cars seen in this lot.
     * The report string is of the form:
     *
     * "Lot {id} was used by {n} car(s) today.", where id is the lot's unique number and n is the number of cars.
     * @return the usage report for the day.
     */
    public String report() {
        return "Lot " + this.getLotNumber() + " was used by " + this.seenCars.size() + " car(s) today.";
    }

    /**
     * Takes in a license plate, records the information, and returns whether the car
     * is newly-seen in this lot this day.
     * @param plate A license plate
     * @return True if the car had not previously been seen.
     */
    public boolean sawCar(String plate) {
        boolean newlySeenCar = !seenCars.contains(plate);
        seenCars.add(plate);
        return newlySeenCar;
    }

    /**
     * Resets any data structures for a new day of ticketing.
     */
    public void newDay() {
        seenCars.clear();
    }
}