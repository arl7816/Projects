/**
* Creates the Toy class using the IToy interface. Toy class is an abstract class that holds the basic functions of all toys
* @author: Alex Lee
* */

package toys;

public abstract class Toy implements IToy{
    /** the initial happiness of any object */
    public final static int INITIAL_HAPPINESS = 0;
    /** the max happiness before an object retires */
    public final static int MAX_HAPPINESS = 100;
    /** the initial wear an object has */
    public final static double INITIAL_WEAR = 0.0;

    /** the product code */
    private int productCode;
    /** the name of the toy */
    private String name;
    /** the happiness of the toy */
    private int happiness;
    /** the total wear on the object */
    private double wear;

    /**
     * Class constructor for Toy (cant be used to initialize a Toy object)
     * @param productCode: the product code of the object
     * @param name: the name of the Toy object
     * */
    protected Toy(int productCode, String name){
        this.productCode = productCode;
        this.name = name;
        this.happiness = INITIAL_HAPPINESS;
        this.wear = INITIAL_WEAR;
    }

    /**
     * Gets the product code
     * @return the product code
     * */
    public int getProductCode(){
        return this.productCode;
    }

    /**
     * gets the name of an object
     * @return the name
     * */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the happiness of the toy
     * @return the happiness
     * */
    public int getHappiness() {
        return this.happiness;
    }

    /**
     * Checks to see whether an object is retired based on happiness
     * @return the state of the toy being retired
     * */
    public boolean isRetired(){
        // an object is retired when its happiness is greater than or equal to its max happiness
        return this.happiness >= MAX_HAPPINESS;
    }

    /**
     * gets the wear of the toy
     * @return the wear
     * */
    public double getWear(){
        return this.wear;
    }

    /**
     * Increases the amount of wear on an object
     * @param amount: the amount as which wear is increased
     * */
    public void increaseWear(double amount){
        this.wear += amount;
    }

    /**
     * Plays with an object, if the wears out, the user is notified that it is retired
     * @param time: the time the object is played with (correlates with happiness)
     * */
    public void play(int time){
       System.out.println("PLAYING(" + time + "): " + this.toString());

       specialPlay(time);
       this.happiness += time;
       if (this.isRetired()){
           System.out.println("RETIRED: " + this.toString());
       }
    }

    /**
     * An abstract method that all descendants of Toy call upon being played with
     * @param time: the amount of time an object is played with
     * */
    protected abstract void specialPlay(int time); // special play acts as an abstract method that every subclass of Toy must implement

    /**
     * Represents a Toy object in String form...
     * Toy{PC:##, N:$$, H:##, R:T/F, W:##}
     * PC -> product code
     * N -> name
     * H -> happiness
     * R -> isRetired
     * W -> total wear
     * @return representation of a toy object
     * */
    @Override
    public String toString() {
        return "Toy{PC:" + this.productCode + ", N:" + this.name + ", H:" + this.happiness + ", R:" + this.isRetired() + ", W:" + this.wear + "}";
    }
}
