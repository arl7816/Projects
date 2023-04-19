package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.*;

/**
 * Represents a configuration of a String puzzle that holds an associated goal of letters
 * The puzzle consisting of capital letter combinations which each can form neighbors
 * by moving each letter up and down one spot in the alphabet such that they wrap around in the alphabet.
 * (Z<-A->B) and (Y<-Z->A)
 *
 * So a puzzle configuration of AB, has four neighbors
 * 1. CB
 * 2. ZB
 * 3. AC
 * 4. AA
 *
 * @author Alex Lee
 * */

public class StringConfig implements Configuration {
    /** the letters representing this configuration */
    private final String content;
    /** The letters representing the goal of the overall puzzle */
    private static String goal; // never changes, and is shared between all instances of the class

    /**
     * Gets the letters of configuration
     * @return the letters of the configuration
     * */
    public String getContent() {
        return this.content;
    }

    /**
     * Gets the end goal of the configuration
     * @return the goal
     * */
    public String getGoal() {
        return goal;
    }

    /**
     * Moves a letter up or down in the alphabet. Letters are always capital and will wrap around in the alphabet
     * so (Z<-A->B) and (Y<-Z->A)
     *
     * @param letter: the letter to be changed
     * @param direction: the amount the letter is moved (can only be -1,0, or 1)
     * @return the new letter in a String form
     * */
    private String moveLetter(char letter, int direction){
        letter += direction;
        if (letter > 'Z'){
            letter = 'A';
        }else if (letter < 'A'){
            letter = 'Z';
        }
        return Character.toString(letter);
    }

    /**
     * The constructor for a String puzzle configuration
     * @param content: the letters of the string configuration
     * @param endGoal: the letters that represent the end goal configuration
     * */
    public StringConfig(String content, String endGoal){
        this.content = content;
        goal = endGoal;
    }

    /**
     * A private constructor used to be called by a StringConfig
     * @param content: the letters of the string configuration
     * */
    private StringConfig(String content){
        this.content = content;
    }

    /**
     * checks if the current configuration is the end goal.
     * @return true if this is the end goal, false otherwise
     * */
    @Override
    public boolean isSolution() {
        return this.content.equals(goal);
    }

    /**
     * Gets the neighbors as a collection of configurations by moving each letter
     * forward or backwards in the alphabet
     * Such that the string AB results in 4 neighbors
     * 1. CB
     * 2. ZB
     * 3. AC
     * 4. AA
     * @return a collection of neighbor configurations
     * */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> queue = new LinkedList<>();

        for (int i=0; i<this.content.length(); i++){
            if (this.content.charAt(i) == ' '){ // handles the case of a blank word
                continue;
            }

            char letter = this.content.charAt(i);
            String upward = this.moveLetter(letter, -1);
            String downward = this.moveLetter(letter, 1);

            StringConfig upConfig = new StringConfig(
                    this.content.substring(0,i) + upward + this.content.substring(i+1)
            );

            StringConfig downConfig = new StringConfig(
                    this.content.substring(0,i) + downward + this.content.substring(i+1)
            );

            queue.add(upConfig);
            queue.add(downConfig);
        }

        return queue;
    }

    /**
     * checks if this object is equal to another
     * An object is equal, if
     * 1. Is an instance of a StringConfig
     * 2. the content of the string is the same
     * 3. the end goal is the same
     * @param obj: the object being evaluated
     * @return true if they are equal, false otherwise
     * */
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof StringConfig config){
            result = config.content.equals(this.content) && config.getGoal().equals(this.getGoal());
        }
        return result;
    }

    /**
     * Gets the hashcode of the StringConfig
     * @return the hashcode of the object
     * */
    @Override
    public int hashCode() {
        return this.content.hashCode() + goal.hashCode();
    }

    /**
     * Gets the string representation of the object in the form of the content
     * @return the content of the configuration
     * */
    @Override
    public String toString() {
        return this.content;
    }
}
