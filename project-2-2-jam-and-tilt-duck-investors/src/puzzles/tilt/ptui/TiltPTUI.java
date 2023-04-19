package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltConfig;
import puzzles.tilt.model.TiltModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * Acts as a viewer and controller for the MVC paradigm but instead of using a GUI interface, uses a text interface to
 * control the tilt puzzle game, in which your main goal is to tilt all the green pieces into the hole, without getting
 * any blue dots in. The board is represented by text based characters such that
 * G --> green dot
 * B --> blue dot
 * * --> barrier
 * . --> empty spot
 * O --> hole
 * @author Alex Lee
 * */
public class TiltPTUI implements Observer<TiltModel, String> {
    /** Holds the model of the game, which controls the configuration */
    private final TiltModel model;
    /** Used to get input from the player via the console */
    private final Scanner input;
    /** Keeps track of whether the game is active. If the game is still going, true, otherwise, false */
    private boolean gameOn = true;

    /** keeps track of whether the previous move was a hint or not */
    private boolean receivedHint;

    /**
     * Gets the user input from the console and acts based on the input. If the improper input is given, the controls are
     * displayed to the user. The controls include:
     * t (N,S,E,W) --> tilts the board if possible
     * h --> gives the hint of the board
     * r --> restarts the game
     * q --> quits the game
     * l (txt file) --> loads a file, if the file fails to load, the game is stopped and the player is notified.
     * @param input: the input given by the player in the console
     * @throws IndexOutOfBoundsException: if they player inputs something wrong for either t or l there is a chance of an
     * index out of bounds error occurring.
     * */
    private void userInput(String input) throws IndexOutOfBoundsException{
        // If a IO exception was raised, the user put in the wrong file, index out of bounds
        // means they input something wrong, default means they didn't put in a command
        switch (input.charAt(0)) {
            case 't' -> { // they want to tilt the board
                char dir = input.charAt(2); // assumes the user put one space after the t

                if (!this.model.gameOver()){
                    this.model.makeTilt(dir);
                }else {
                    System.out.println("Puzzle already solved");
                    this.displayBoard();
                }
            }
            case 'q' -> // they want to quit
                    this.model.quit();
            case 'h' -> { // they want to get a hint
                if (!this.model.gameOver()) {
                    this.model.getHint();
                }else{
                    System.out.println("Puzzle already solved");
                    this.displayBoard();
                }
            }
            case 'r' -> // they want to reset the board
                    this.model.reset();
            case 'l' -> { // they want to load a file
                String file = input.substring(2); // assumes there is a space
                this.model.loadFile(file);
            }
            default -> { // something was input wrong
                printControls();
            }
        }
    }

    /**
     * Prints the controls of the game to the console
     * */
    private void printControls(){
        System.out.println("""
                h(int)              -- hint next move
                l(oad) filename     -- load new puzzle file
                t(ilt) {N|S|E|W}    -- tilt the board in the given direction
                q(uit)              -- quit the game
                r(eset)             -- reset the current game""");
    }

    /**
     * Displays the game board as a two-dimensional array of characters to the console
     * */
    public void displayBoard(){
        for (int col = 0; col< TiltConfig.getDIM(); col++){
            System.out.print(" " + col);
        }

        for (int row=0; row<TiltConfig.getDIM(); row++){
            System.out.println();
            System.out.print(row);
            for (int col=0; col<TiltConfig.getDIM(); col++){
                System.out.print(" " + this.model.getSpot(row, col));
            }
        }
        System.out.println();
    }

    /**
     * The main run method that keeps the game going until either the user quits or wins as well as collecting there
     * input.
     * */
    private void run(){
        while (gameOn){
            String user = input.nextLine();
            try {
                userInput(user);
            }catch (IndexOutOfBoundsException e){
                printControls();
            }
        }
        // the game has ended
    }

    /**
     * Updates the state of the TUI based on what the model informs this viewer of, such as
     * 1) the board moving
     * 2) a failed movement
     * 3) a loaded game
     * 4) failure to load a game
     * 5) a hint was used
     * 6) a hint failed due to no solutions possible
     * Based on the update, the board will change, to correspond with the models configuration of the puzzle
     * @param model: the model that was changed
     * @param message: the type of change that occurred within the model
     * */
    @Override
    public void update(TiltModel model, String message) {
        if (message.equals(TiltModel.LOADED)){
            System.out.println("Loaded File: " + this.model.getFileName());
            this.displayBoard();
        }

        if (message.equals(TiltModel.MOVED)){
            if (!receivedHint) {
                System.out.println("Board tilted");
            }else{
                receivedHint = false;
                System.out.println("Hint used");
            }
            this.displayBoard();

            if (this.model.gameOver()){
                System.out.println("Congrats you won!!!");
            }
        }

        if (message.equals(TiltModel.FAILED_HINT)){
            System.out.println("No Solution possible");
            receivedHint = false;
            this.displayBoard();
        }

        if (message.equals(TiltModel.FAILED_LOAD)){
            System.out.println("Failed to load game");
            this.displayBoard();
        }

        if (message.equals(TiltModel.QUIT)){
            System.out.println("Quitting game");
            gameOn = false;
        }

        if (message.startsWith(TiltModel.HINT)){
            this.receivedHint = true;
            String dir = message.split(" ")[1];
            switch (dir){
                case "north" -> this.model.makeTilt('N');
                case "south" -> this.model.makeTilt('S');
                case "east" -> this.model.makeTilt('E');
                case "west" -> this.model.makeTilt('W');
            }
        }

        if (message.equals(TiltModel.FAILED_MOVE)){
            System.out.println("Invalid Move");
            this.displayBoard();
        }
    }

    /**
     * The constructor of the puzzle which creates the model and adds itself to the list of observers.
     * */
    public TiltPTUI(){
        this.model = new TiltModel();
        input = new Scanner(System.in);
        this.model.addObserver(this);
        this.receivedHint = false;
    }

    /**
     * The main method, which starts the game and the corresponding game loop
     * @param args: holds the starting file of the game
     * @throws IOException: if an incorrect file is given, may throw an error
     * */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }

        TiltPTUI ui = new TiltPTUI();
        ui.printControls();
        ui.model.loadFile(args[0]);
        ui.run();
    }
}
