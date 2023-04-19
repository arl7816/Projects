/**
 * Holds the abstract class BatteryPowered which extends the abstract class Toy
 * Holds the methods and attributes that all battery powered toys should have, such as,
 * RCCars and Robots
 *
 * @author Alex Lee
 * */

package toys;

public abstract class BatteryPowered extends Toy{
    /** the maximum charge of any object */
    public final static int FULLY_CHARGED = 100;
    /** The point at which any objects battery is depleted */
    public final static int DEPLETED = 0;

    /** the total number of batteries */
    private int numBatteries;
    /** the current charge of the battery */
    private int batteryLevel;

    /**
     * The constructor of the class (Cant be initialized due to being abstract)
     * @param productCode: the product code of the object
     * @param name: the name of the object
     * @param numBatteries: the number of batteries the object holds
     * */
    protected BatteryPowered(int productCode, String name, int numBatteries){
        super(productCode, name);
        this.numBatteries = numBatteries;
        this.batteryLevel = FULLY_CHARGED;
    }

    /**
     * Gets the current charge of the battery
     * @return the charge of the battery
     * */
    public int getBatteryLevel(){
        return this.batteryLevel;
    }

    /**
     * Gets the number of batteries
     * @return the number of batteries
     * */
    public int getNumBatteries(){
        return this.numBatteries;
    }

    /**
     * Uses the battery based on the following equation: time + the total number of batteries
     * if the battery becomes depleted than the user is notified, followed by a recharge and notification of the recharge
     * @param time: the amount play time
     * */
    public void useBatteries(int time){
        this.batteryLevel -= (time + this.numBatteries);
        if (this.batteryLevel < DEPLETED){
            this.batteryLevel = 0;
            System.out.println("\tDEPLETED:" + this.toString());
            this.batteryLevel = FULLY_CHARGED;
            System.out.println("\tRECHARGED:" + this.toString());
        }
    }

    /**
     * Gets a string representation of the battery powered toy in the form of...
     * Toy{PC:?, N:?, H:?, R:?, W:?}, BatteryPowered{BL:?, NB:?}
     * BL -> battery level
     * NB -> the number of batteries
     * @return representation of the battery powered toy
     * */
    @Override
    public String toString() {
        return super.toString() + ", BatteryPowered{BL:" + this.getBatteryLevel() + ", NB:" + this.getNumBatteries() + "}";
    }
}
