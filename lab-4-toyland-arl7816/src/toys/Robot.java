/**
 * Holds the Robot class which is an extension of the abstract BatteryPowered class
 * @author: Alex Lee
 * */

package toys;

public class Robot extends BatteryPowered{
    /** speed of flying robots */
    public final static int FLY_SPEED = 25;
    /** the speed of objects that run instead of flying */
    public final static int RUN_SPEED = 10;
    /** the initial speed of any given object */
    public final static int INITIAL_SPEED = 0;
    /** the base product code (starts at 500 and goes up by 1 every time a robot is initialized) */
    private static int baseProductCode = 500;

    /** the distance of an object (starts at the initial speed) */
    private int distance;
    /** keeps track of whether this robot can fly */
    private boolean canFly;

    /**
     * The constructor
     * @param name: the name of the robot
     * @param numBatteries: the number of batteries for this robot
     * @param flying: does this robot fly
     * */
    protected Robot(String name, int numBatteries, boolean flying){
        super(baseProductCode, name, numBatteries);
        baseProductCode++;
        this.canFly = flying;
        this.distance = INITIAL_SPEED;
    }

    /**
     * tell you whether this object can fly
     * @return whether this robot can fly
     * */
    public boolean isFlying(){
        return this.canFly;
    }

    /**
     * Gets the distance
     * @return the distance
     * */
    public int getDistance(){
        return this.distance;
    }

    /**
     * Robots special play which notifies the user of how the object behaves, changes the distance, then increases the wear
     * 1. distance increased by time * {flySpeed/runSpeed}
     * 2. User notified that {name} is {flying/running} around with total distance: {distance}
     * 3. wear increased by {flySpeed/runSpeed}
     * 4. the batteries are than used based on the time played
     * @param time: the amount of play time
     * */
    @Override
    protected void specialPlay(int time) {
        if (isFlying()){
            this.distance += time * FLY_SPEED;
            System.out.println("\t" + this.getName() + " is flying around with total distance: " + this.getDistance());
            this.increaseWear(FLY_SPEED);
        }else{
            this.distance += time * RUN_SPEED;
            System.out.println("\t" + this.getName() + " is running around with total distance: " + this.getDistance());
            this.increaseWear(RUN_SPEED);
        }
        this.useBatteries(time);
    }

    /**
     * Gets a string representation of a Robot object in the form...
     * Toy{PC:?, N:?, H:?, R:?, W:?}, BatteryPowered{BL:?, NB:?},  Robot{F:?, D:?}
     * F -> can this object fly?
     * D -> total distance
     * @return representation of a robot
     * */
    @Override
    public String toString() {
        return super.toString() + ", Robot{F:" + this.isFlying() + ", D:" + this.getDistance() + "}";
    }
}
