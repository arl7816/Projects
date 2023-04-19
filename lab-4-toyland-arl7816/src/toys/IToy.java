/**
* Acts as an interface for everything that is a Toy
* @author: Alex Lee
* */

package toys;

public interface IToy {
    /**
     * Gets the product code of an object
     * @return the product code
     * */
    int getProductCode();

    /**
     * Gets the name of the object
     * @return the name
     * */
    String getName();

    /**
     * Gets the happiness of the toy
     * @return the happiness
     * */
    int getHappiness();

    /**
     * Checks to see whether a object is retired based on happiness
     * @return the state of the toy being retired
     * */
    boolean isRetired();

    /**
     * gets the wear of the toy
     * @return the wear
     * */
    double getWear();

    /**
     * Increases the wear of an object
     * @param amount: the amount to increase wear by
     * */
    void increaseWear(double amount);

    /**
     * Plays with a Toy for a given amount of time
     * @param time: the amount of time a toy is played with
     * */
    void play(int time);
}
