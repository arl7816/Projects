/**
 * Creates the PlayDough which acts as an extension of the abstract class Toy
 * @author Alex Lee
 * */

package toys;

public class PlayDough extends Toy{
    /** the amount the wear is multiplied */
    public final static double WEAR_MULTIPLIER = 0.05;
    /** the starting product code (increases by 1 everytime an object is initialized) */
    private static int baseCode = 100;  // base code starts at 100 and increases by 1 everytime an object is created

    /** the color of the playdough*/
    private final Color color;

    /**
     * Class constructor
     * @param name: the name of the object,
     * @param color: the color of the object
     * */
    protected PlayDough(String name, Color color){
        super(baseCode, name);
        baseCode++;
        this.color = color;
    }

    /**
     * Gets the color of a PlayDough Object
     * */
    public Color getColor() {
        return this.color;
    }

    /**
     * implements the spacialPlay from the parent abstract class
     * increases the wear of the object by the time played multiplied with the wear multiplier
     * @param time: the amount of time the object is played with
     * */
    protected void specialPlay(int time){
        System.out.println("\tArts and crafting with " + this.getColor() + " " + this.getName());
        this.increaseWear(WEAR_MULTIPLIER * time);
    }

    /**
     * Gets a string representation of a Play Dough object in the form of...
     * Toy{PC:##, N:$$, H:##, R:T/F, W:##}, PlayDough{C:$$$}
     * C -> color of the dough
     * @return representation of a dough object
     * */
    @Override
    public String toString() {
        return super.toString() + ", PlayDough{C:" + this.getColor() + "}";
    }
}
