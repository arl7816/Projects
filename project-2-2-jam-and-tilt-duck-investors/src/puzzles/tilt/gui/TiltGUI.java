package puzzles.tilt.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltConfig;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;

/**
 * Holds the controller and viewer for the tilt puzzle in which, the user can
 * tilt the board in four directions. North, south, west, and east, unless the blue ball
 * falls into the hole. The user can also, load new games, restart the game, or ask for a hint, in which
 * the GUI will play the move the hint gives. If the game has no solutions available, the hint, won't cause a move
 * @author Alex Lee
 * */
public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /** The total height of the GUI */
    private final double HEIGHT = 850;
    /** The total Width of the Gui */
    private final double WIDTH = 910;
    /** the model that manages the game and notifies viewers of the changes */
    private TiltModel model;
    /** The gridpane that holds the contents of the game (dots, holes, and barriers) */
    private GridPane centerGrid;

    // for demonstration purposes
    /** The image of the green disk */
    private final Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));
    /** The image of the blue disk */
    private final Image blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"));
    /** The image of the block */
    private final Image block = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"));
    /** The image of the hole */
    private final Image hole = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"));

    /** the borderpane that holds the main contents of the game (acts as the main component) */
    private BorderPane borderPane;
    /** The button hat tilts the board upward */
    private Button northButton;
    /** The button that tilts the board downward */
    private Button southButton;
    /** the button that tilts the board to the right */
    private Button eastButton;
    /** the button that tilts the board to the left */
    private Button westButton;

    /** the restart button */
    private Button restart;

    /** A label that notifies the user of events such as a movement being made */
    private Label message;

    /** keeps track of whether the previous move was a hint or not */
    private boolean receivedHint;

    /**
     * Loads the basic contents of the game upon starting the GUI model.
     * */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.model = new TiltModel();
        this.model.loadFile(filename);
        this.model.addObserver(this);
        this.receivedHint = false;
    }

    /**
     * Creates the buttons that control the tilt (up, down, right, left)
     * */
    private void createButtons(){
        Button upButton = new Button();
        upButton.setText("\uD83E\uDC81");
        upButton.setPrefSize(WIDTH-105, 40);
        upButton.setOnAction((event) -> {
            if (!this.model.gameOver()) {
                this.model.makeTilt('N');
            }else{
                this.message.setText("Already Solved");
            }
        });

        Button downButton = new Button();
        downButton.setText("\uD83E\uDC83");
        downButton.setPrefSize(WIDTH-105, 40);
        downButton.setOnAction((event) -> {
            if (!this.model.gameOver()) {
                this.model.makeTilt('S');
            }else{
                this.message.setText("Already Solved");
            }
        });

        Button westButton = new Button();
        westButton.setText("\uD83E\uDC80");
        westButton.setPrefSize(40, HEIGHT);
        westButton.setOnAction((event) -> {
            if (!this.model.gameOver()) {
                this.model.makeTilt('W');
            }else{
                this.message.setText("Already Solved");
            }
        });

        Button eastButton = new Button();
        eastButton.setText("\uD83E\uDC82");
        eastButton.setPrefSize(40, HEIGHT);
        eastButton.setOnAction((event -> {
            if (!this.model.gameOver()) {
                this.model.makeTilt('E');
            }else{
                this.message.setText("Already Solved");
            }
        }));

        this.northButton = upButton;
        this.southButton = downButton;
        this.eastButton = eastButton;
        this.westButton = westButton;
    }

    /***
     * In charge of setting up the basic GUI interface for the game
     * @param stage: the current window the gui takes place in
     * @throws Exception:
     */
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();
        this.borderPane = borderPane;
        BorderPane rightBorder = new BorderPane();
        this.setGrid();

        borderPane.setCenter(this.centerGrid);

        createButtons();

        this.message = new Label("Loaded: " + this.model.getFileName());
        this.message.setFont(Font.font("Arial", 20));

        borderPane.setLeft(this.westButton);
        borderPane.setTop(new VBox(message, this.northButton));
        borderPane.setBottom(this.southButton);
        borderPane.setRight(rightBorder);

        rightBorder.setLeft(this.eastButton);

        Button loadButton = new Button("Load");
        loadButton.setOnAction((event) -> loadGame(stage));
        loadButton.setBackground(Background.fill(Color.CYAN));
        loadButton.setFont(Font.font("Arial", 20));

        Button restart = new Button("Restart");
        restart.setOnAction((event) -> this.model.reset());
        restart.setBackground(Background.fill(Color.RED));
        restart.setFont(Font.font("Arial", 20));
        this.restart = restart;

        Button hint = new Button("Hint");
        hint.setOnAction((event) -> {
            if (!this.model.gameOver()) {
                this.model.getHint();
            }else{
                this.message.setText("Already Solved");
            }
        }
        );
        hint.setBackground(Background.fill(Color.YELLOW));
        hint.setFont(Font.font("Arial", 20));

        VBox vbox = new VBox(loadButton, restart, hint);

        rightBorder.setRight(vbox);

        stage.setTitle("Tilt");
        stage.setHeight(HEIGHT);
        stage.setWidth(WIDTH);
        stage.setResizable(true);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Adds in an image view to the center grid
     * @param row: the row of the image view
     * @param col: the column of the image view
     * @param img: the image to be placed in the center grid
     * @param but: a button to which the image gets its size from
     * */
    private void addImageView(int row, int col, Image img, Button but){
        ImageView view = new ImageView(img);
        view.setPreserveRatio(true);
        view.setFitWidth(but.getPrefWidth());
        view.setFitHeight(but.getPrefHeight());
        this.centerGrid.add(view, col, row);
    }

    /**
     * Adds in a button to the center grid and a corresponding image based on the models board
     * @param row: the row the button
     * @param col: the column of the button
     * */
    private void addGridButton(int row, int col){
        /*
         * G --> green slider
         * B --> blue slider ()
         * - --> an empty spot to slide through
         * * --> a bumper that makes a slider stop
         * O --> a hole that all green sliders can go through (can only be one, but can be anywhere)
         * */

        Button but = new Button();
        but.setBorder(Border.stroke(Color.GRAY));

        double width = (WIDTH-80) / TiltConfig.getDIM();
        double height = (HEIGHT-135) / TiltConfig.getDIM();
        but.setPrefSize(width, height);

        switch (this.model.getSpot(row, col)) {
            case 'G' -> {
                addImageView(row, col, greenDisk, but);
            }
            case 'B' -> {
                addImageView(row, col, blueDisk, but);
            }
            case '*' -> {
                addImageView(row, col, block, but);
            }
            case 'O' -> {
                addImageView(row, col, hole, but);
            }
            default -> {
                but.setBackground(Background.fill(Color.WHITE));
                this.centerGrid.add(but, col, row); // why does the row and col need to be reversed???
            }
        }
    }

    /**
     * Sets up the grid based on the dimensions and board of the model. Such that it places, empty spots, green dots,
     * blue dots, blocks, and a hole that green dots can fall through.
     * */
    private void setGrid(){
        this.centerGrid = new GridPane();
        this.borderPane.setCenter(this.centerGrid);

        for (int row = 0; row<TiltConfig.getDIM(); row++){
            for (int col=0; col<TiltConfig.getDIM(); col++){
                addGridButton(row, col);
            }
        }
    }

    /**
     * Loads a game from a given file that the user can choose from
     * @param stage: the current stage of the GUI
     * */
    private void loadGame(Stage stage){
        //create a new FileChooser
        FileChooser fileChooser = new FileChooser();
        //set the directory to the boards folder in the current working directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tilt"));
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Load a game board.");
        //open up a window for the user to interact with.
        File selectedFile = fileChooser.showOpenDialog(stage);
        this.model.loadFile(selectedFile.toString());
    }

    /**
     * Updates the state of the GUI based on what the model informs this viewer of, such as
     * 1) the board moving
     * 2) a failed movement
     * 3) a loaded game
     * 4) failure to load a game
     * 5) a hint was used
     * 6) a hint failed due to no solutions possible
     * Based on the update, the board will change, to correspond with the models configuration of the puzzle
     * @param tiltModel: the model that was changed
     * @param message: the type of change that occurred within the model
     * */
    @Override
    public void update(TiltModel tiltModel, String message) {
        if (message.equals(TiltModel.MOVED)){
            if (!receivedHint) {
                this.message.setText("");
            }else{
                receivedHint = false;
            }

            setGrid();
            if (this.model.gameOver()){
                this.message.setText("Game Over! You WON");
                this.restart.setBackground(Background.fill(Color.LIGHTGREEN));
            }
        }

        if (message.equals(TiltModel.LOADED)){
            setGrid();
            this.restart.setBackground(Background.fill(Color.RED));
            this.message.setText("Loaded: " + this.model.getFileName().substring(this.model.getFileName().indexOf("data")));
        }

        if (message.equals(TiltModel.FAILED_LOAD)){
            this.message.setText("Failed to Load a file");
        }

        if (message.equals(TiltModel.FAILED_MOVE)){
            this.message.setText("Invalid move, blue dots cant fall through the hole");
        }

        if (message.equals(TiltModel.FAILED_HINT)){
            this.message.setText("No Solution Possible");
        }

        if (message.startsWith(TiltModel.HINT)){
            String hint = message.split(" ")[1];
            this.message.setText("Next Move");
            receivedHint = true;
            switch (hint){
                case "north" -> this.model.makeTilt('N');
                case "south" -> this.model.makeTilt('S');
                case "east" -> this.model.makeTilt('E');
                case "west" -> this.model.makeTilt('W');
            }
        }
    }

    /** Launched the GUI application and begins the game */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
        } else {
            Application.launch(args);
        }
    }
}
