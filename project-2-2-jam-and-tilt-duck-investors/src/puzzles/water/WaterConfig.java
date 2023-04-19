package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * Holds a configuration of a water puzzle, such that holds that are filled to a certain weight
 * (default weight = 0). Each bucket can hold a maximum of a particular height that is unique to it.
 * Every bucket can do one of 3 things, it can drain itself, fill itself, or pour itself into another bucket
 * The end goal of the puzzle is to get one of the buckets to reach a certain weight
 *
 * @author Alex Lee
 * */
public class WaterConfig implements Configuration {
    /** the weight we want one of the buckets to get */
    private static int goal; // is the same for all configurations
    /** the capacities of each of the buckets */
    private static List<Integer> capacities; // the same for all configurations
    /** an array holding the weight of each bucket */
    private final int[] buckets;

    /**
     * Gets the goal of the water puzzle
     * @return the aspired weight of one of the buckets
     * */
    public int getGoal() {
        return goal;
    }

    /**
     * Gets an array holding the weight of each bucket
     * @return an array of integers
     * */
    public int[] getBuckets() {
        return this.buckets;
    }

    /**
     * Gets the capacities of every bucket
     * each index corresponds to a bucket in this.buckets
     * @return a list of integers representing bucket capacities
     * */
    public List<Integer> getCapacities() {
        return capacities;
    }

    /**
     * Converts the capacities of buckets into a string representation in the form of
     * [0, 1, 2, ..., N]
     * @return a string of bucket capacities
     * */
    public String stringCapacities(){
        String printStatement = "[";
        for (int i=0; i<capacities.size(); i++){
            printStatement += capacities.get(i);
            if (i != capacities.size() - 1){
                printStatement += ", ";
            }
        }
        printStatement += "]";
        return printStatement;
    }

    /**
     * A constructor for the starting configuration of a water puzzle
     * @param capacitiesStart: a list of integers representing the capacities of each of the buckets
     * @param goalStart: the weight one bucket must get to beat the puzzle
     * */
    public WaterConfig(List<Integer> capacitiesStart, int  goalStart){
        this.buckets = new int[capacitiesStart.size()];
        capacities = capacitiesStart;
        goal = goalStart;
    }

    /**
     * A private constructor meant to only be call by other WaterConfigs
     * @param buckets: an int array with the weight of water in each bucket
     *               (length of buckets needs to be the same among all instances of WaterConfig)
     * */
    private WaterConfig(int[] buckets){
        this.buckets = buckets;
    }

    /**
     * checks if this configuration is the solution to the puzzle
     * @return true if one of the buckets has the desired weight of water, false otherwise
     * */
    @Override
    public boolean isSolution() {
        boolean result = false;
        for (int bucket: buckets){
            if (bucket == goal){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Gets the neighbors as a collection of configurations by applying 3 actions to each bucket
     * 1. The bucket is filled
     * 2. The bucket is drained
     * 3. the bucket pours itself into every other bucket (creates an instance for each pour)
     *    3a. the bucket fills the other one, leaving some water in itself
     *    3a. the bucket pours its entire self into the bucket
     * @return a collection of neighbor configurations
     * */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> queue = new LinkedList<Configuration>();

        for (int i=0; i<this.buckets.length; i++){
            if (this.buckets[i] != capacities.get(i)) { // why fill a bucket that full
                int[] filled = this.buckets.clone();
                filled[i] = capacities.get(i);
                queue.add(new WaterConfig(filled));
            }

            if (this.buckets[i] != 0) { // why drain a bucket that empty
                int[] drained = this.buckets.clone();
                drained[i] = 0;
                queue.add(new WaterConfig(drained));
            }

            for (int k=0; k<this.buckets.length; k++){
                if (k == i || this.buckets[i] == 0){continue;} // prevents it from filling itself and prevents empty buckets from pouring

                int[] tempBuckets = this.buckets.clone();

                int space = capacities.get(k) - tempBuckets[k];

                if (tempBuckets[i] <= space){
                    tempBuckets[k] += tempBuckets[i];
                    tempBuckets[i] = 0;
                }else{
                    tempBuckets[k] += space;
                    tempBuckets[i] -= space;
                }

                queue.add(new WaterConfig(tempBuckets));
            }

        }
        return queue;
    }

    /**
     * Check if this object is equal to another
     * an object is equal if,
     * 1. Is an instance of a WaterConfig
     * 2. Every bucket has the same weight of water in it
     * @return true if they are equal, false otherwise
     * */
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof WaterConfig config) {
            for (int i = 0; i < this.buckets.length; i++) {
                if (this.buckets[i] != config.buckets[i]){
                    return false;
                }
            }
            result = true;
        }
        return result;
    }

    /**
     * gets the hashcode of the WaterConfig
     * @return the hashcode of the object
     * */
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.buckets) + capacities.hashCode() + goal;
    }

    /**
     * Gets the string representation of the objects bucket weights, in the form
     * [1, 2, 3, ..., N]
     * @return the weight of water in each bucket
     * */
    @Override
    public String toString() {
        String printStatement = "[";
        for (int i=0; i<this.buckets.length; i++){
            printStatement += this.buckets[i];
            if (i != this.buckets.length - 1){
                printStatement += ", ";
            }
        }
        printStatement += "]";

        return printStatement;
    }
}