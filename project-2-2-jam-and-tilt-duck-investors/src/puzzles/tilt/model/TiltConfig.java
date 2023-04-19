package puzzles.tilt.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Holds a configuration of the tilt puzzle in which the goal of the game is to tilt the board in such a way that a series
 * of tilts results in all green dots falling though a hole. However, any move that results in a blue dot falling through
 * a hole results in an invalid move and cant be complete as a result. Blue and green dots can move, while blockers don't
 * move with any tilt. Green and blue dots are stopped by either other dots or blockers. Tilts as well, can only move in
 * four directions, north, south, east, or west. Each configuration can also create four neighbors of itself based on the
 * possible four tilts.
 * @author Alex Lee
 * */

public class TiltConfig implements Configuration {
    // is assumed to be a square
    /*
    * G --> green slider
    * B --> blue slider ()
    * - --> an empty spot to slide through
    * * --> a bumper that makes a slider stop
    * O --> a hole that all green sliders can go through (can only be one, but can be anywhere)
    * */

    /**
     * the number of rows and columns that reside in the Board. Every board is a square (row=column)
     * */
    private static int DIM;
    /** the board of the pieces, which is represented by a 2d array of characters */
    private final char[][] board;

    /**
     * Gets the 2d array representing the board
     * @return the 2d array of characters
     * */
    public char[][] getBoard() {
        return this.board;
    }

    /**
     * Gets the dimensions of the board. Height and width are assumed to be the same
     * @return the dimensions of the board as a single int
     * */
    public static int getDIM() {
        return DIM;
    }

    /**
     * The constructor of the board, which creates the initial model of the game.
     * @param fileName: the file that holds the initial configurations information for the board
     * */
    public TiltConfig(String fileName) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(fileName));

        DIM = Integer.parseInt(input.readLine());
        this.board = new char[DIM][DIM];

        String line;
        int row=-1;
        while ((line = input.readLine()) != null){
            row++;
            String[] spots = line.split(" ");
            for (int col=0; col<spots.length; col++){
                this.board[row][col] = spots[col].charAt(0);
            }
        }
        input.close();
    }

    /**
     * A private constructor used to crete neighbors of a configuration.
     * @param board: the new board state
     * */
    private TiltConfig(char[][] board){
        this.board = board.clone();
    }

    public TiltConfig getTiltConfig(char dir){
        TiltConfig config = null;
        char[][] newBoard = null;
        switch (dir) {
            case 'N' -> newBoard = moveNorth(copyBoard());
            case 'S' -> newBoard = moveSouth(copyBoard());
            case 'E' -> newBoard = moveEast(copyBoard());
            case 'W' -> newBoard = moveWest(copyBoard());
        }

        if (newBoard != null){
            config = new TiltConfig(newBoard);
        }

        return config;
    }

    /**
     * Checks to see if the current configuration of is the solution to the project.
     * A solution is only a solution when everything single green dot falls through the hole
     * @return true if the current state is a solution, false otherwise
     * */
    @Override
    public boolean isSolution() {
        boolean result = true;
        for (char[] row: this.board){
            for (char col: row){
                if (col == 'G'){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * copy's the contents of the configurations board as to avoid states referencing the same states board
     * @return the copied board
     * */
    private char[][] copyBoard(){
        char[][] newBoard = new char[DIM][DIM];
        for (int row=0; row<newBoard.length; row++){
            System.arraycopy(this.board[row], 0, newBoard[row], 0, newBoard[row].length);
        }
        return newBoard;
    }

    /**
     * Checks if a spot can move, in a particular direction, or will fall off the edge if moved
     * (assumes that the spot is only moving up or down aka dirRow=0 or dirCol=0)
     * @param row: the current row of the spot
     * @param col: the current column of the spot
     * @param dirRow: the amount the row changes every movement
     * @param dirCol: the amount the column changes every movement
     * @param board: the board the spot resides on
     * @return true if the spot won't fall off the edge, false otherwise
     * */
    private boolean canMove(int row, int col, int dirRow, int dirCol, char[][] board){
        //row+dirRow < board.length - 1 && row+dirRow > 0 && col+dirCol < board[row].length - 1 && col+dirCol > 0
        boolean result = true;

        if (dirRow == 0){
            // we must be moving the column
            int nextColumn = col+dirCol;
            result = (nextColumn <= board[row].length - 1 && nextColumn >= 0);
        }else{
            // we must be moving the row
            int nextRow = row+dirRow;
            result = (nextRow <= board.length - 1 && nextRow >= 0);
        }

        return result;
    }

    /**
     * moves a spot until it either falls through a hole, reaches the edge of the board, or collides with an unmovable
     * object. Movement will stop if a Green (G) dot falls through the hole (O).
     * Assumes, the object is either a green dot (G) or a blue dot (B)
     * (will return null if a blue dot (B) falls through a hole (O))
     * @param row: the current row of the spot
     * @param col: the current column of the spot
     * @param dirRow: the amount the row changes every movement
     * @param dirCol: the amount the column changes every movement
     * @param board: the board the spot resides on
     * @return the board after the movement of a piece was completed
     * */
    private char[][] move(int row, int col, int dirRow, int dirCol, char[][] board){
        char spot = board[row][col];

        int startRow = row;
        int startCol = col;

        while (canMove(row, col, dirRow, dirCol, board)){
            startRow = row;
            startCol = col;

            row += dirRow;
            col += dirCol;
            char newSpot = board[row][col];

            if (newSpot == '*' || newSpot == 'B' || newSpot == 'G'){ // something is in the way
                break;
            }

            if (newSpot == 'O' && spot == 'B'){ // invalid move
                return null;
            }

            if (newSpot == 'O' && spot == 'G'){ // the piece should fall through
                board[startRow][startCol] = '.';
                break;
            }

            // if no problems arise, swap the spots and keep going
            board[row][col] = spot;
            board[startRow][startCol] = '.';
        }

        return board;
    }

    /**
     * Moves every movable piece upward; movable pieces consisting of green dots (G) and blue dots (B)
     * (If a blue piece falls through the hole, a move becomes invalid and returns null)
     * @param board: the board of the configuration that has its pieces moved
     * @return the new state of the board after everything has been moved
     * */
    private char[][] moveNorth(char[][] board){
        for (int row=0; row<board.length; row++){
            for (int col=0; col<board[row].length; col++){
                if (board[row][col] == 'G' || board[row][col] == 'B'){
                    board = move(row, col, -1, 0, board);
                    if (board == null){return null;}
                }
            }
        }
        return board;
    }

    /**
     * Moves every movable piece downward; movable pieces consisting of green dots (G) and blue dots (B)
     * (If a blue piece falls through the hole, a move becomes invalid and returns null)
     * @param board: the board of the configuration that has its pieces moved
     * @return the new state of the board after everything has been moved
     * */
    private char[][] moveSouth(char[][] board){
        // same as North but start at the end and dir is -1
        for (int row=board.length - 1; row >=0; row--){
            for (int col=board[row].length - 1; col >=0; col--){
                if (board[row][col] == 'G' || board[row][col] == 'B'){
                    board = move(row, col, 1, 0, board);
                    if (board == null){return null;}
                }
            }
        }
        return board;
    }

    /**
     * Moves every movable piece right; movable pieces consisting of green dots (G) and blue dots (B)
     * (If a blue piece falls through the hole, a move becomes invalid and returns null)
     * @param board: the board of the configuration that has its pieces moved
     * @return the new state of the board after everything has been moved
     * */
    private char[][] moveEast(char[][] board){
        for (int row=board.length - 1; row >=0; row--){
            for (int col=board[row].length - 1; col >=0; col--){
                if (board[col][row] == 'G' || board[col][row] == 'B'){
                    board = move(col, row, 0, 1, board);
                    if (board == null){return null;}
                }
            }
        }
        return board;
    }

    /**
     * Moves every movable piece left; movable pieces consisting of green dots (G) and blue dots (B)
     * (If a blue piece falls through the hole, a move becomes invalid and returns null)
     * @param board: the board of the configuration that has its pieces moved
     * @return the new state of the board after everything has been moved
     * */
    private char[][] moveWest(char[][] board){
        for (int row=0; row<board.length; row++){
            for (int col=0; col<board[row].length; col++){
                if (board[col][row] == 'G' || board[col][row] == 'B'){
                    board = move(col, row, 0, -1, board);
                    if (board == null){return null;}
                }
            }
        }
        return board;
    }

    /**
     * Gets the neighbors of the current configuration. Neighbors include the board being moved up, down, right, left
     * (If a blue piece falls through the hole, a neighbor is returned as null and is ignored)
     * @return a queue of the neighbors
     * */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> queue = new LinkedList<>();
        char[][] tempBoard;
        // moves everything north

        tempBoard = moveNorth(copyBoard());
        if (tempBoard != null && !Arrays.deepEquals(tempBoard, this.board)){
            TiltConfig config = new TiltConfig(tempBoard);
            queue.add(config);
        }else{
            queue.add(null);
        }

        // move everything south
        tempBoard = moveSouth(copyBoard());
        if (tempBoard != null && !Arrays.deepEquals(tempBoard, this.board)){
            TiltConfig config = new TiltConfig(tempBoard);
            queue.add(config);
        }else{
            queue.add(null);
        }

        // move everything east
        tempBoard = moveEast(copyBoard());
        if (tempBoard != null && !Arrays.deepEquals(tempBoard, this.board)){
            queue.add(new TiltConfig(tempBoard));
        }else{
            queue.add(null);
        }

        // move everything west
        tempBoard = moveWest(copyBoard());
        if (tempBoard != null && !Arrays.deepEquals(tempBoard, this.board)){
            queue.add(new TiltConfig(tempBoard));
        }else{
            queue.add(null);
        }

        return queue;
    }

    /**
     * checks to see if two configurations are the same
     * Two configurations are the same if their boards and dimensions are the same
     * @return true if they are the same, false otherwise
     * */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof TiltConfig tilt){
            result = Arrays.deepEquals(this.board, tilt.board);
        }
        return result;
    }

    /**
     * Gets the hashcode of the object
     * @return the hashcode
     * */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }

    /**
     * Converts the configuration into a string representation of the object.
     * String representation of an object is the board itself
     * @return the board in string form
     * */
    @Override
    public String toString() {
        String printStatement = "\n";

        for (char[] row: this.board){
            for (char col: row){
                printStatement += col + " ";
            }
            printStatement += "\n";
        }

        return printStatement;
    }
}
