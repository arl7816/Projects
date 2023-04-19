package magnets;

import backtracking.Configuration;
import test.IMagnetTest;

import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The representation of a magnet configuration, including the ability
 * to backtrack and also give information to the JUnit tester.
 *
 * This implements a more optimal pruning strategy in isValid():
 * - Pair checked each time a new cell is populated
 * - Polarity checked each time a new cell is populated
 * - When last column or row is populated, the pos/neg counts are checked
 *
 * @author RIT CS
 * @author Alex Lee
 */
public class MagnetsConfig implements Configuration, IMagnetTest {
    /** a cell that has not been assigned a value yet */
    private final static char EMPTY = '.';
    /** a blank cell */
    private final static char BLANK = 'X';
    /** a positive cell */
    private final static char POS = '+';
    /** a negative cell */
    private final static char NEG = '-';
    /** left pair value */
    private final static char LEFT = 'L';
    /** right pair value */
    private final static char RIGHT = 'R';
    /** top pair value */
    private final static char TOP = 'T';
    /** bottom pair value */
    private final static char BOTTOM = 'B';
    /** and ignored count for pos/neg row/col */
    private final static int IGNORED = -1;

    // add private state here
    /** the number of rows in the configuration */
    private static int rows;
    /** the number of cols in the configuration */
    private static int cols;
    /** an array representing the total number of positive magnets in a given row */
    private static int[] posRowCount;
    /** an array representing the total number of negative magnets in a given row */
    private static int[] negRowCount;
    /** an array representing the total number of positive magnets in a column */
    private static int[] posColCount;
    /** an array representing the total number of negative magnets in a column */
    private static int[] negColCount;
    /** A 2D array representing the orientation of a magnet pairs */
    private static char[][] orientationList; // this should be final somehow
    /** A 2D array representing the entire grid (. = empty, + = positive, - = negative, x = neutral) */
    private final char[][] grid;
    /** an array of length 2, that holds the row and column coordinates of a cursor */
    private final int[] cursor;

    /** keeps track of the total amount of negative and positive charges in the rows of the grid.
     * Row 0, keeps the total negative numbers.
     * Row 1, keeps the total positive numbers. */
    int[][] sumRows; // row 0 = neg, row 1=pos
    /** keeps track of the total amount of negative and positive charges in the columns of the grid.
     * Row 0, keeps the total negative numbers.
     * Row 1, keeps the total positive numbers. */
    int[][] sumCols;

    /**
     * Read in the magnet puzzle from the filename.  After reading in, it should display:
     * - the filename
     * - the number of rows and columns
     * - the grid of pairs
     * - the initial config with all empty cells
     *
     * @param filename the name of the file
     * @throws IOException thrown if there is a problem opening or reading the file
     */
    public MagnetsConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            // read first line: rows cols
            String[] fields = in.readLine().split("\\s+");
            rows = Integer.parseInt(fields[0]);
            cols = Integer.parseInt(fields[1]);

            fields = in.readLine().split("\\s+"); // the positive row counts
            posRowCount = new int[rows];
            for (int i=0; i<fields.length; i++){
                posRowCount[i] = Integer.parseInt(fields[i]);
            }

            fields = in.readLine().split("\\s+"); // the positive column counts
            posColCount = new int[cols];
            for (int i=0; i<fields.length; i++){
                posColCount[i] = Integer.parseInt(fields[i]);
            }

            fields = in.readLine().split("\\s+"); // negative row counts
            negRowCount = new int[rows];
            for (int i=0; i<fields.length; i++){
                negRowCount[i] = Integer.parseInt(fields[i]);
            }

            fields = in.readLine().split("\\s+"); // negative column counts
            negColCount = new int[cols];
            for (int i=0; i<fields.length; i++){
                negColCount[i] = Integer.parseInt(fields[i]);
            }

            int row=-1;
            String input;
            orientationList = new char[rows][cols];
            while ((input = in.readLine()) != null){ // gets the orientation of each spot
               row++;
               fields = input.split("\\s+");
               for (int col=0; col<fields.length; col++){
                   orientationList[row][col] = fields[col].charAt(0);
               }
            }

            this.cursor = new int[]{0, -1};
            this.grid = new char[rows][cols];
            for (char[] chars : this.grid) {
                Arrays.fill(chars, '.');
            }

            this.sumRows = new int[2][rows]; // row 0 = neg, row 1 = pos
            this.sumCols = new int[2][cols]; // col 0 = neg, col 1 = pos

        } // <3 Jim --> thx Jim

        // Display the information
        System.out.println("File: " + filename);
        System.out.println("Rows: " + rows + ", Columns: " + cols);

        System.out.println("Pairs:");
        for (char[] chars: orientationList){
            for (char let: chars){
                System.out.print(let + " ");
            }
            System.out.println();
        }

        System.out.println("Initial config:");
        System.out.println(this.toString());
    }

    /**
     * The copy constructor which advances the cursor, creates a new grid,
     * and populates the grid at the cursor location with val
     * @param other the board to copy
     * @param val the value to store at new cursor location
     */
    private MagnetsConfig(MagnetsConfig other, char val){
        this.grid = new char[rows][cols];
        this.sumRows = new int[2][rows]; // row 0 = neg, row 1=pos
        this.sumCols = new int[2][cols];

        // clones the grid
        for (int row = 0; row<other.grid.length; row++){
            for (int col=0; col< other.grid[row].length; col++){
                this.grid[row][col] = other.getVal(row, col);

                char currentVal = other.getVal(row, col);
                if (currentVal == '-'){
                    this.sumRows[0][row]++;
                    this.sumCols[0][col]++;
                }else if (currentVal == '+'){
                    this.sumRows[1][row]++;
                    this.sumCols[1][col]++;
                }
            }
        }

        // clones the cursor
        this.cursor = new int[]{other.cursor[0], other.cursor[1]};

        // move the cursor forward
        if (this.cursor[1] + 1 >= cols){
            this.cursor[0]++;
            this.cursor[1] = 0;
        }else{
            this.cursor[1]++;
        }

        this.grid[this.cursor[0]][this.cursor[1]] = val;
        if (val == '-'){
            this.sumRows[0][this.cursor[0]]++;
            this.sumCols[0][this.cursor[1]]++;
        }else if (val == '+'){
            this.sumRows[1][this.cursor[0]]++;
            this.sumCols[1][this.cursor[1]]++;
        }
    }


    /**
     * Generate the successor configs.  For minimal pruning, this should be
     * done in the order: +, - and X.
     *
     * @return the collection of successors
     */
    @Override
    public List<Configuration> getSuccessors() {
        List<Configuration> successors = new ArrayList<Configuration>();

        successors.add(new MagnetsConfig(this, '+'));
        successors.add(new MagnetsConfig(this, '-'));
        successors.add(new MagnetsConfig(this, 'X'));

        return successors;
    }

    /**
     * Checks to see if the corresponding element of a given element matches within a pair
     * (Pairs include, - => +, + => -, X=>X). The corresponding elements are figured out by their
     * associated char value, where 'T' = Top, 'B' = Bottom, 'L' = Left, 'R' = Right
     * @param row: the index of the row within the grid
     * @param col: the index of the column within the grid
     * @param currentSpot: the char value of the newly populated grid element
     * @return true if an element doesn't have a conflicting pair, false otherwise
     * */
    private boolean checkOrientation(int row, int col, char currentSpot){
        boolean result = true;
        switch (this.getPair(row, col)) {
            case 'B' -> {
                char top = this.getVal(row - 1, col);
                if (top == '-' && currentSpot != '+'){
                    result = false;
                }

                if (currentSpot == '-' && top != '+'){
                    result = false;
                }

                if (top == '+' && currentSpot != '-'){
                    result = false;
                }

                if (currentSpot == '+' && top != '-'){
                    result = false;
                }
            } case 'R' -> {
                char left = this.getVal(row, col - 1);
                if (left == '-' && currentSpot != '+'){
                    result = false;
                }

                if (currentSpot == '-' && left != '+'){
                    result = false;
                }

                if (left == '+' && currentSpot != '-'){
                    result = false;
                }

                if (currentSpot == '+' && left != '-'){
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * Checks if the populated cell has any orthogonal cells that are the same polarity
     * (excluding X to X relationships)
     * @param row: the index of the row within the grid
     * @param col: the index of the column within the grid
     * @param currentSpot: the char value of the grid newly populated element
     * @return true if no orthogonal cells have the same polarity, false otherwise
     * */
    private boolean checkSurrounding(int row, int col, char currentSpot){
        boolean result = true;

        // check to make sure top and left don't share the same sign
        if (row != 0){
            char topChar = this.getVal(row-1, col);
            if (topChar == currentSpot){
                if (currentSpot != 'X'){
                    result = false;
                }
            }
        }
        if (col != 0){
            char leftChar = this.getVal(row, col-1);
            if (leftChar == currentSpot){
                if (currentSpot != 'X'){
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * Checks if any of the rows or columns have too many positive or negative charges
     * @param row: the index of the row in the grid
     * @param col: the index of the column in the grid
     * @return true if nothing exceeds its limits, false otherwise
     * */
    private boolean checkExceed(int row, int col){
        boolean result = true;

        if (this.sumRows[0][row] > this.getNegRowCount(row) && this.getNegRowCount(row) != -1){
            result = false;
        }

        if (this.sumRows[1][row] > this.getPosRowCount(row) && this.getPosRowCount(row) != -1){
            result = false;
        }

        if (this.sumCols[0][col] > this.getNegColCount(col) && this.getNegColCount(col) != -1){
            result = false;
        }

        if (this.sumCols[1][col] > this.getPosColCount(col) && this.getPosColCount(col) != -1){
            result = false;
        }

        return result;
    }

    /**
     * check if each row and column has the total amount of positive and negative charges specified
     * @return true is every row and column has the specified amount of charges, false otherwise
     * */
    private boolean checkGoal(){
        for (int row=0; row<this.grid.length; row++){
            if (this.sumRows[0][row] != this.getNegRowCount(row) && this.getNegRowCount(row) != -1){
                return false;
            }
            if (this.sumRows[1][row] != this.getPosRowCount(row) && this.getPosRowCount(row) != -1){
                return false;
            }

            for (int col=0; col<this.grid[row].length; col++){
                if (this.sumCols[0][col] != this.getNegColCount(col) && this.getNegColCount(col) != -1){
                    return false;
                }
                if (this.sumCols[1][col] != this.getPosColCount(col) && this.getPosColCount(col) != -1){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks to make sure a successor is valid or not.  For minimal pruning,
     * each newly placed cell at the cursor needs to make sure its pair
     * is valid, and there is no polarity violation.  When the last cell is
     * populated, all row/col pos/negative counts are checked.
     *
     * @return whether this config is valid or not
     */
    @Override
    public boolean isValid() {
        boolean result = true;

        int row = this.getCursorRow();
        int col = this.getCursorCol();
        char currentSpot = this.getVal(row, col);

        // checks to make sure the pairs are correct
        result = checkOrientation(row, col, currentSpot);

        // check to make sure top and left don't share the same sign
        if (result){ // used to avoid reevaluation (once result is false, it should return false)
            result = checkSurrounding(row, col, currentSpot);
        }


        // checks to see if the max number of charges in a row/column has exceeded its limit
        if (result){ // used to avoid reevaluation (once result is false, it should return false)
            result = checkExceed(row, col);
        }

        // if the end of the grid is reached, check if the goal was achieved
        if (this.cursor[0] == rows-1 && this.cursor[1] == cols-1 && result){
            result = checkGoal();
        }

        return result;
    }

    /**
     * checks if the goal of the grid has been reached.
     * {because of the isValid method, we only need to check if this is the final coordinate
     * on the grid}
     * @return true if grid fills all the requirements of the final grid, false otherwise
     * */
    @Override
    public boolean isGoal() {
        return this.cursor[0] == rows-1 && this.cursor[1] == cols-1;
    }

    /**
     * Returns a string representation of the puzzle including all necessary info.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        // top row
        result.append("+ ");
        for (int col = 0; col < getCols(); ++col) {
            result.append(getPosColCount(col) != IGNORED ? getPosColCount(col) : " ");
            if (col < getCols() - 1) {
                result.append(" ");
            }
        }
        result.append(System.lineSeparator());
        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            if (col != getCols() - 1) {
                result.append("--");
            } else {
                result.append("-");
            }
        }
        result.append(System.lineSeparator());

        // middle rows
        for (int row = 0; row < getRows(); ++row) {
            result.append(getPosRowCount(row) != IGNORED ? getPosRowCount(row) : " ").append("|");
            for (int col = 0; col < getCols(); ++col) {
                result.append(getVal(row, col));
                if (col < getCols() - 1) {
                    result.append(" ");
                }
            }
            result.append("|").append(getNegRowCount(row) != IGNORED ? getNegRowCount(row) : " ");
            result.append(System.lineSeparator());
        }

        // bottom row
        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            if (col != getCols() - 1) {
                result.append("--");
            } else {
                result.append("-");
            }
        }
        result.append(System.lineSeparator());

        result.append("  ");
        for (int col = 0; col < getCols(); ++col) {
            result.append(getNegColCount(col) != IGNORED ? getNegColCount(col) : " ").append(" ");
        }
        result.append(" -").append(System.lineSeparator());
        return result.toString();
    }

    // IMagnetTest

    /**
     * Gets the total number of rows
     * @return the rows
     * */
    @Override
    public int getRows() {
        return rows;
    }

    /**
     * Gets the total number of columns
     * @return the columns
     * */
    @Override
    public int getCols() {
        return cols;
    }

    /**
     * Gets the amount of positive magnets in a given row
     * @return the amount
     * */
    @Override
    public int getPosRowCount(int row) {
        return posRowCount[row];
    }

    /**
     * Gets the amount of positive magnets in a given column
     * @return the amount;
     * */
    @Override
    public int getPosColCount(int col) {
        return posColCount[col];
    }

    /**
     * Gets the amount of negative magnets in a given row
     * @return the amount
     * */
    @Override
    public int getNegRowCount(int row) {
        return negRowCount[row];
    }

    /**
     * Gets the amount of negative magnets in a column
     * @return the amount
     * */
    @Override
    public int getNegColCount(int col) {
        return negColCount[col];
    }

    /**
     * gets the orientation of a give spot based on the row and column
     * @return the letter representing the orientation (L, R, T, B)
     * */
    @Override
    public char getPair(int row, int col) {
        return orientationList[row][col];
    }

    /**
     * Gets the value at a specific spot on the grid given a row and column
     * @return the value of the spot
     * */
    @Override
    public char getVal(int row, int col) {
        return this.grid[row][col];
    }

    /**
     * Gets the row of the cursor
     * @return the row
     * */
    @Override
    public int getCursorRow() {
        return this.cursor[0];
    }

    /**
     * Gets the column of the cursor
     * @return the column
     * */
    @Override
    public int getCursorCol() {
        return this.cursor[1];
    }
}
