package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.*;

/**
 * Implements the model for the Tilt Puzzle on the principle of MVC. Where this holds the configuration of the puzzle and
 * can make changes to it based on a seperate controller. Whenever changes are made, the model notifies all the viewers
 * of the change, so they can use the data of the model to update their own GUI views of the game.
 * @author Alex Lee
 * */

public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers;

    /** the current configuration */
    private TiltConfig currentConfig;
    private final Solver solver;

    /** keeps track of the original file in case the board is reset */
    private String fileName;

    /** A string used to notify viewers that the model has loaded a new board */
    public static final String LOADED = "LOADED";

    /** a string used to notify viewers that a hint has been used. Is also followed by its direction */
    public static final String HINT = "HINT";
    /** a string used to notify viewers that a hint failed due to there being no possible solution */
    public static final String FAILED_HINT = "FAILED HINT";

    /** a string used to notify viewers the board has moved */
    public static final String MOVED = "MOVED";

    /** a string used to notify viewers that a move would result in a failed moved */
    public static final String FAILED_MOVE = "FAILED-MOVE"; // blue fell through a hole

    /** is used to notify viewers that the user has quit */
    public static final String QUIT = "QUIT";

    /** is used to notify viewers that a particular board failed to load (should result in the game ending) */
    public static final String FAILED_LOAD = "FAILED_LOAD";

    /** gets the current configuration within the board */
    public TiltConfig getCurrentConfig() {
        return this.currentConfig;
    }

    /**
     * loads a given board given a text file containing the contents of the board. Will alert every viewer of a loaded
     * board
     * @param fileName: the name of the file used to load the board
     * */
    public void loadFile(String fileName){
        // alert = "LOADED"
        try{
            this.currentConfig = new TiltConfig(fileName);
            this.fileName = fileName;
            this.alertObservers(LOADED);
        }catch (Exception e){
            this.alertObservers(FAILED_LOAD);
        }

    }

    /** used to alert all viewers that a player has quit the game */
    public void quit(){
        this.alertObservers(QUIT);
    }

    /** tilts the board in a given direction (north, south, west, east). If the board fails to move (blue piece will fall
     * through the hole) then all viewers are notified of a failed_move, otherwise, notified of the move
     * @param dir: a char of the desired direction (n = north, s = south, e = east, w = west)]
     */
     public void makeTilt(char dir){
        // alert = "MOVED"
        TiltConfig tester = this.currentConfig.getTiltConfig(dir);
        if (tester != null){
            this.currentConfig = tester;
            this.alertObservers(MOVED);
        }else{
            this.alertObservers(FAILED_MOVE);
        }
    }

    /**
     * Gets the file name of the current configuration
     * @return the file name
     * */
    public String getFileName(){
        return this.fileName;
    }

    /**
     * Gets the character of the board at a certain spot on the board
     * @param row: the row of the spot
     * @param col: the column of the spot
     * @return the character at the given row and column of the board
     * */
    public char getSpot(int row, int col){
        return this.currentConfig.getBoard()[row][col];
    }

    /**
     * Gets the hint of the board, using a BFS search, which will than alert every viewer of the hint by giving
     * the constant HINT followed by a space than (north, south, east, west)
     *  */
    public void getHint(){
        // alert = "HINT"
        List<Configuration> configs = this.currentConfig.getNeighbors().stream().toList();
        //north, then south, then east, then west
        List<Configuration> path = solver.BFSSearch(this.currentConfig);

        if (configs.get(0) != null){
            if (configs.get(0).equals(path.get(1))){
                this.alertObservers(HINT + " north");
                return;
            }
        }
        if (configs.get(1) != null){
            if (configs.get(1).equals(path.get(1))){
                this.alertObservers(HINT + " south");
                return;
            }
        }
        if (configs.get(2) != null){
            if (configs.get(2).equals(path.get(1))){
                this.alertObservers(HINT + " east");
                return;
            }
        }
        if (configs.get(3) != null){
            if (configs.get(3).equals(path.get(1))){
                this.alertObservers(HINT + " west");
                return;
            }
        }

        this.alertObservers(FAILED_HINT);
    }

    /** resets the current board and will alert every viewer of the load */
    public void reset(){
        this.loadFile(fileName);
    }

    /**
     * checks if the game is over
     * @return true if the current configuration is the solution, false otherwise
     * */
    public boolean gameOver(){
        return this.currentConfig.isSolution();
    }

    /**
     * The view calls this to add itself as an observer.
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /** the constructor which creates the solver and observers of the model */
    public TiltModel(){
        this.solver = new Solver();
        this.observers = new LinkedList<>();
    }
}
