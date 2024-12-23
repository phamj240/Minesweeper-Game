//Johnathan Pham: pham0549
//Andi Nguyen: nguy5131

import java.util.Queue;
import java.util.Random;

public class Minefield {
    /**
     * Global Section
     */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /*
     * Class Variable Section
     *
     */

    public Cell[][] minefield;
    public int rows;
    public int cols;
    public int flags;
    public int mines;
    public boolean isGameOver;

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */

    /**
     * Minefield
     * <p>
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     *
     * @param rows    Number of rows.
     * @param columns Number of columns.
     * @param flags   Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {
        minefield = new Cell[rows][columns]; //creates the minefield based on the user's choice of difficulty
        this.cols = columns;
        this.rows = rows;
        this.flags = flags;
        this.mines = flags;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield[i][j] = new Cell(false, "-"); //sets every cell to the blank value
            }
        }
    }

    /**
     * evaluateField
     *
     * @function: Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     */
    public void evaluateField() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!minefield[i][j].getStatus().equals("M")) {
                    minefield[i][j].setStatus("0"); //sets all cells that aren't a mine to 0 initially
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (minefield[i][j].getStatus().equals("M")) {
                    for (int a = i - 1; a < i + 2; a++) { //check adjacent cells of all mines. Checks Top-Left of mine going to the right then Middle-Left of the mine going to the right then Bottom-Right of the mine going to the right.
                        for (int b = j - 1; b < j + 2; b++) {
                            if (a >= 0 && a < rows && b >= 0 && b < cols && (!minefield[a][b].getStatus().equals("M"))) { //checks if the cell adjacent to the mine is on the board (covers cases if mines are on the edge of the board)
                                if (minefield[a][b].getStatus().equals("0")) { //If cell is a 0, set it to 1
                                    minefield[a][b].setStatus("1");
                                }
                                else {
                                    minefield[a][b].setStatus(Integer.toString(Integer.parseInt(minefield[a][b].getStatus())+1)); //Increments the cell's value by one (loops above allow for a cell to increment more than once depending on the number of mines adjacent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * createMines
     * <p>
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     *
     * @param x     Start x, avoid placing on this square.
     * @param y     Start y, avoid placing on this square.
     * @param mines Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        if (x < 0 || x >= rows || y < 0 || y >= cols) { //makes sure the starting x and y are in bounds
            return;
        }
        int count = 0;
        int i;
        int j;
        Random rand = new Random();
        while (count < mines) { //Loops until all mines are placed
            i = rand.nextInt(0, rows); //generates a random x coordinate
            j = rand.nextInt(0, cols); //generates a random y coordinate
            if ((i != x || j != y) && !minefield[i][j].getStatus().equals("M")) { //Makes sure that the random coordinate isn't the same as the starting coordinate. Also makes sure that there isn't already a mine in the cell
                minefield[i][j].setStatus("M"); //Creates the mine
                count++;
            }
        }
    }

    /**
     * guess
     * <p>
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     *
     * @param x    The x value the user entered.
     * @param y    The y value the user entered.
     * @param flag A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        if (x < 0 || x >= rows || y < 0 || y >= cols) { //Makes sure the guessed cell is in bounds
            return false;
        }

        if (flag) { //Checks if in flag mode
            if (flags > 0) { //Makes sure there are enough flags
                minefield[x][y].setStatus("F"); //Adds the flag
                minefield[x][y].setRevealed(true);
                flags--;
            }
            return false;
        }
        else {
            if (minefield[x][y].getStatus().equals("M")) { //Checks if the user hit a mine
                minefield[x][y].setRevealed(true);
                System.out.println("Game Over");
                isGameOver = true;
                return true; //Ends the game
            }
            else if (minefield[x][y].getStatus().equals("0")) { //Reveal all 0's if guessed cell was a 0
                revealZeroes(x, y);
            }
            else {
                minefield[x][y].setRevealed(true); //Reveals the cell if not a mine nor a 0
            }
            return false;
        }
    }

    /**
     * gameOver
     * <p>
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
        if (flags == 0) {
            boolean lost = false;
            for (int i = 0; i < rows && !lost; i++) {
                for (int j = 0; j < cols && !lost; j++) {
                    if (minefield[i][j].getStatus().equals("M")) { //checks if there are any mines left over after using all mines
                        System.out.println("Game Over");
                        lost = true;
                    }
                }
            }
            if (!lost) { //if there are no flags left over and all mines are flagged, game is over and user wins
                System.out.println("You won");
            }
            return true;
        }
        return isGameOver;
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     * <p>
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        if (x < 0 || x >= rows || y < 0 || y >= cols) { //makes sure the cell is in bounds
            return;
        }
        Stack1Gen<int[]> stack = new Stack1Gen<>();
        stack.push(new int[]{x, y}); //push starting coordinates onto the stack
        while (!stack.isEmpty()) { //Loops until the stack is empty
            int[] coord = stack.pop();
            Cell curr = minefield[coord[0]][coord[1]]; //gets the current cell
            curr.setRevealed(true); //Reveals the current cell

            // If a neighboring cell has a status of "0" and is not already revealed, push its coordinates onto the stack

            if (coord[0] - 1 >= 0 && minefield[coord[0] - 1][coord[1]].getStatus().equals("0") && !minefield[coord[0] - 1][coord[1]].getRevealed()) {// checks the cell to the left
                stack.push(new int[]{coord[0] - 1, coord[1]});
            }
            if (coord[0] + 1 < cols && minefield[coord[0] + 1][coord[1]].getStatus().equals("0") && !minefield[coord[0] + 1][coord[1]].getRevealed()) { //checks cell to the right
                stack.push(new int[]{coord[0] + 1, coord[1]});
            }
            if (coord[1] - 1 >= 0 && minefield[coord[0]][coord[1] - 1].getStatus().equals("0") && !minefield[coord[0]][coord[1] - 1].getRevealed()) { //checks cell above
                stack.push(new int[]{coord[0], coord[1] - 1});
            }
            if (coord[1] + 1 < rows && minefield[coord[0]][coord[1] + 1].getStatus().equals("0") && !minefield[coord[0]][coord[1] + 1].getRevealed()) { //checks cell below
                stack.push(new int[]{coord[0], coord[1] + 1});
            }
        }
    }

    /**
     * revealStartingArea
     * <p>
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     * <p>
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x The x value the user entered.
     * @param y The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        Q1Gen<int[]> queue = new Q1Gen<>();
        queue.add(new int[]{x, y}); //Adds guessed cell coordinates to the queue

        while (queue.length() != 0) {
            int[] coord = queue.remove();
            Cell curr = minefield[coord[0]][coord[1]]; //gets the current cell
            curr.setRevealed(true); //Reveals the current cell
            if (curr.getStatus().equals("M")) {
                break;
            }

            // Add neighboring cells to the queue if they are not already revealed

            if (coord[0] - 1 >= 0 && !minefield[coord[0] - 1][coord[1]].getRevealed()) { //Check cell to the left
                queue.add(new int[]{coord[0] - 1, coord[1]});
            }
            if (coord[0] + 1 < rows && !minefield[coord[0] + 1][coord[1]].getRevealed()) { //Check cell to the right
                queue.add(new int[]{coord[0] + 1, coord[1]});
            }
            if (coord[1] - 1 >= 0 && !minefield[coord[0]][coord[1] - 1].getRevealed()) { //Check cell above
                queue.add(new int[]{coord[0], coord[1] - 1});
            }
            if (coord[1] + 1 < cols && !minefield[coord[0]][coord[1] + 1].getRevealed()) { //check cell below
                queue.add(new int[]{coord[0], coord[1] + 1});
            }
        }
    }

    /**
     * For both printing methods utilize the ANSI colour codes provided!
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected.
     */
    public void debug() {
        StringBuilder result = new StringBuilder();
        result.append("    ");
        for (int i = 0; i < rows; i++){ //Add column numbers
            result.append(i);
            result.append("   ");
        }
        result.append("\n");
        for (int i = 0; i < minefield.length; i++) {
            result.append(i); //Add row number
            result.append("|");
            for (int j = 0; j < minefield[0].length; j++) {
                result.append(color(minefield[i][j].getStatus())); //Adds the color-coded status of the current cell
            }
            result.append("\n");
        }
        System.out.println(result);
    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("    ");
        for (int i = 0; i < rows; i++){ //Adds the column numbers
            result.append(i);
            result.append("   ");
        }
        result.append("\n");
        for (int i = 0; i < minefield.length; i++){
            result.append(i); //adds the row numbers
            result.append("|");
            for (int j = 0; j <minefield[0].length; j++){
                if (!minefield[i][j].getRevealed()){ //Makes sure that cells that aren't revealed don't get revealed
                    result.append("\u3000-" + "\u3000"); //Makes sure that spacing is even on the board
                }
                else{
                    result.append(color(minefield[i][j].getStatus())); //Adds the color-coded status of the current cell
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    public String color(String status) { //Color-Codes each status and returns the color-coded status that was given
        if (status.equals("0")) {
            return (ANSI_YELLOW + "\u30000\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("1")) {
            return (ANSI_BLUE + "\u30001\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("2")) {
            return (ANSI_CYAN + "\u30002\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("3")) {
            return (ANSI_PURPLE + "\u30003\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("4")) {
            return (ANSI_GREEN + "\u30004\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("5")) {
            return (ANSI_YELLOW_BRIGHT + "\u30005\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("6")) {
            return (ANSI_BLUE_BRIGHT + "\u30006\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("7")) {
            return (ANSI_RED + "\u30007\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("8")) {
            return (ANSI_PURPLE_BACKGROUND + "\u30008\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("F")) {
            return (ANSI_WHITE_BACKGROUND + "\u3000F\u3000" + ANSI_GREY_BACKGROUND);
        }
        if (status.equals("M")) {
            return (ANSI_RED_BRIGHT + "\u3000M\u3000" + ANSI_GREY_BACKGROUND);
        }
        return "\u3000" + status + "\u3000";
    }

}
