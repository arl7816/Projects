/**
 * Holds the custom class toy Object. This class is a Superhero action figure and therefore extends ActionFigure
 * It has the ability to hold multiple types of powers and based on those powers the energy used will be different and
 * the play situations will be different as well
 * @author Alex Lee
 * */

package toys;

public class Superhero extends ActionFigure{
    /** the maximum flight speed */
    public final static int MAX_FLIGHT = 50;
    /** the maximum strength */
    public final static int MAX_STRENGTH = 100;
    /** the max invincibility */
    public final static int MAX_INVINCIBLE = 25;
    /** the maximum energy used with a laserbeam */
    public final static int MAX_LASER = 30;
    /** the maximum amount of affect teleportation can have on energy */
    public final static int MAX_TELEPORT = 2;

    /** the affect of flight (equals 1 if the object does not have the power)*/
    private int flight;
    /** the affect of strength (equals 1 if the object does not have the power)*/
    private int strength;
    /** the affect of invincible (equals 1 if the object does not have the power)*/
    private int invincible;
    /** the affect of laser (equals 0 if the object does not have the power)*/
    private int laser;
    /** the affect of teleport (equals 1 if the object does not have the power)*/
    private int teleport;

    /** the first superpower of the hero */
    private SuperPower power1;
    /** the second superpower of the hero*/
    private SuperPower power2;

    /**
     * gets the first power of the superhero
     * @return power 1
     * */
    public SuperPower getPower1(){
        return this.power1;
    }

    /**
     * Gets the second power of the superhero
     * @return power 2
     * */
    public SuperPower getPower2(){
        return this.power2;
    }

    /**
     * assigns the corresponding affects to variables to the power the hero has
     * For example, if the hero has flight; assigns the flight variable to the value of MAX_FLIGHT
     * @param power: the superpower the hero has
     * */
    private void assignEnergy(SuperPower power){
        switch (power) {
            case FLIGHT -> this.flight = MAX_FLIGHT;
            case STRENGTH -> this.strength = MAX_STRENGTH;
            case INVINCIBILITY -> this.invincible = MAX_INVINCIBLE;
            case LASERBEAM -> this.laser = MAX_LASER;
            case TELEPORTATION -> this.teleport = MAX_TELEPORT;
        }
    }

    /**
     * Based on the power displays a message to showcase what happened based on the heros powers
     * @param power: the power the hero has
     * */
    private void usePower(SuperPower power){
        String printStatement = switch (power) {
            case FLIGHT -> "\t" + this.getName() + " flies towards the enemy at lighting fast speed";
            case STRENGTH -> "\t" + this.getName() + "'s punch demands much power, but leaves quite the dent";
            case INVINCIBILITY ->
                    "\tKa-pow, the fearsome, enemy strikes back but the last thing he expects is your hero's iron-like skin!";
            case LASERBEAM -> "\t" + this.getName() + " backs away, unleashing a beam, as hot as the sun towards the enemy";
            case TELEPORTATION -> "\tPhew luckily " + this.getName() + " dodged the enemies quick attack using teleportation";
        };
        System.out.println(printStatement);
    }

    /**
     * Class constructor
     * @param name: the name of the superhero
     * @param age: the age of the superhero
     * @param quip: the heros quip
     * @param power1: the first power the superhero has
     * @param power2: the second power the superhero has
     * */
    protected Superhero(String name, int age, String quip, SuperPower power1, SuperPower power2){
        super(name, age, quip);
        this.power1 = power1;
        this.power2 = power2;

        this.flight = 1;
        this.invincible = 1;
        this.strength = 1;
        this.teleport = 1;
        this.laser = 0;

        assignEnergy(this.power1);
        assignEnergy(this.power2);
    }

    /**
     * plays with the superhero for a given amount of time
     * the energy consumed is equal to ((flight * strength)/invincible + laser) ^ teleport in an integer form
     * after which, the parent class Action Figure has its special play called with time*energy used as its parameter
     * @param time: the amount time the superhero is played with
     * */
    @Override
    protected void specialPlay(int time) {
        // ((flight * strength)/invincible + laser) ^ teleport
        int energy = (int) Math.pow((double)((this.flight * this.strength)/this.invincible) + this.laser, this.teleport);
        this.usePower(this.getPower1());
        this.usePower(this.getPower2());
        super.specialPlay(time * energy);
    }

    /**
     * Gets a string representation of s superhero in the form of...
     * Toy{PC:?, N:?, H:?, R:?, W:?}, Doll{HC:?, A:?, S:?}, ActionFigure{E:?}, Superhero{P1:?, P2:?}
     * P1 -> the superheros first power
     * P2 -> the superheros second power
     * @return representation of a superhero object
     */
    @Override
    public String toString() {
        return super.toString() + ", Superhero{P1:" + this.getPower1() + ", P2:" + this.getPower2() + "}";
    }
}
