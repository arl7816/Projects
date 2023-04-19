/**
 * Holds the class RCcar which extends the abstract class BatteryPowered
 * @author Alex Lee
 * */

package toys;

public class RCCar extends BatteryPowered{
    /** the starting speed of the object */
    public final static int STARTING_SPEED = 10;
    /** the amount the speed increases */
    public final static int SPEED_INCREASE = 5;
    /** the starting product code of the object (starts at 400 and goes up by 1 every initialization) */
    private static int baseProductCode = 400;
    /** the speed of the object. starts at STARTING_SPEED */
    private int speed;

    /**
     * Class constructor
     * @param name: the name of the car
     * @param numBatteries: the number of batteries the car holds
     * */
    protected RCCar(String name, int numBatteries){
        super(baseProductCode, name, numBatteries);
        baseProductCode++;
        this.speed = STARTING_SPEED;
    }

    /**
     * Gets the speed of the object
     * @return the current speed
     * */
    public int getSpeed(){
        return this.speed;
    }

    /**
     * plays with the toy for a given amount of toy
     * First: the user is notified of what the car does, then the batteries get used based on time, the wear increases
     * based on the speed, and finally, the speed increases
     * @param time: the time the car is played with
     * */
    @Override
    protected void specialPlay(int time) {
        System.out.println("\t" + this.getName() + " races around at " + this.getSpeed() + "mph!");
        this.useBatteries(time);
        this.increaseWear(this.getSpeed());
        this.speed += SPEED_INCREASE;
    }

    /**
     * Gets a string representation of a Rc car in the form of...
     * Toy{PC:?, N:?, H:?, R:?, W:?}, BatteryPowered{BL:?, NB:?}, RCCar{S:?}
     * S -> the speed of the object
     * @return representation of the car
     * */
    @Override
    public String toString() {
        return super.toString() + ", RCCar{S:" + this.getSpeed() + "}";
    }
}
