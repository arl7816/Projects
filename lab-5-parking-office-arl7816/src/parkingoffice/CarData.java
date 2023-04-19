package parkingoffice;

/**
 * A class that represent a singular car. Keeps track of its tickets and where it's allowed to park
 * @author Alex Lee
 * */

public class CarData implements Comparable{

    /** the license plate of the car*/
    private final String licencePlate;
    /** the lot number the car is allowed to park in */
    private int allowedParking;
    /** the total number of tickets a car has */
    private int tickets;

    /**
     * check whether a car in a parking lot
     * @param lot: the lot number
     * @return whether the car is allowed in the parking lot
     * */
    public boolean isOk(int lot){
        return lot == this.allowedParking;
    }

    /**
     * gives a singular ticket to a car
     * */
    public void giveTicket(){
        this.tickets++;
    }

    /**
     * Resets the tickets of a car back to 0
     * */
    public void payTickets(){
        this.tickets = 0;
    }

    /**
     * Gets the allowed parking of a car
     * @return the number of the allowed parking lot
     */
    public int getAllowedParking() {
        return this.allowedParking;
    }

    /**
     * Gets the licence plate of a car
     * @return the licence plate
     * */
    public String getLicencePlate() {
        return this.licencePlate;
    }

    /**
     * Gets the tickets a car has accrued
     * @return the total number of tickets a car has
     * */
    public int getTickets() {
        return this.tickets;
    }

    /**
     * The class constructor of a Car
     * @param licencePlate: the licence plate of a car in String form
     * @param allowedParking: the number of the lot, the car is allowed in
     * */
    public CarData(String licencePlate, int allowedParking){
        this.licencePlate = licencePlate;
        this.allowedParking = allowedParking;
    }

    /**
     * overrides the Super class Object's compareTo method
     * Compares cars based on the amount of tickets
     * If both tickets are the same, compares the cars alphabetically by licence plate
     * @param object: an object assumed to be of a CarData type
     * @return 1 if greater, -1 if less, 0 if equal
     * */
    @Override
    public int compareTo(Object object) {
        int result = 0;
        if (object instanceof CarData){
            CarData car = (CarData) object;
            if (this.tickets > car.tickets){
                result = 1;
            }else if (this.tickets < car.tickets){
                result = -1;
            }else{
                result = car.getLicencePlate().compareTo(this.getLicencePlate());
            }
        }
        return result;
    }

    /**
     * checks to see if two CarData objects are equal to each other
     * @param obj: the object being compared to
     * @return whether the two objects are the same
     * */
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof CarData){
            CarData car = (CarData) obj;
            result = car.getLicencePlate().equals(this.getLicencePlate()) && car.getAllowedParking() == this.getAllowedParking() && car.getTickets() == this.getTickets();
        }
        return result;
    }

    /**
     * Gets a string representation of the Car object in the form of...
     * {licence plate} (lot {lot allowed in}) : {tickets} ticket(s)
     * @return representation of the CarData
     * */
    @Override
    public String toString() {
        return this.getLicencePlate() + " (lot " + this.getAllowedParking() + ") : " + this.getTickets() + " ticket(s)";
    }

    /**
     * gets the hashcode of the CarData using the licence plate of the car (assume that no two cars can share the same
     * licence plate)
     * @return the hashcode
     * */
    @Override
    public int hashCode() {
        return this.getLicencePlate().hashCode();
    }
}