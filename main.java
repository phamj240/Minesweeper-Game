//Johnathan Pham: pham0549
//Andi Nguyen: nguy5131

//Import Section
import java.util.Random;
import java.util.Scanner;

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 * 
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */

public class main{
    public static void main(String[] args){
        //initialize all variables
        boolean flagMode = false;
        boolean debugMode = false;
        boolean quit = false;
        int rows = 0;
        int cols = 0;
        int flags = 0;

        String level = "";
        while (!level.equals("Easy") && !level.equals("Medium") && !level.equals("Hard") && !level.equals("quit")) {// Keep looping until a valid mode is entered
            Minefield minefield;
            System.out.println("Enter game mode (Easy/Medium/Hard) or 'quit' to quit");
            Scanner scanner = new Scanner(System.in);
            level = scanner.nextLine();

            if (level.equals("Easy")) { //set parameters of the minefield
                rows = 5;
                cols = 5;
                flags = 5;
            }
            else if (level.equals("Medium")) {//set parameters of the minefield
                rows = 9;
                cols = 9;
                flags = 12;
            }
            else if (level.equals("Hard")) {//set parameters of the minefield
                rows = 20;
                cols = 20;
                flags = 40;
            }
            else if (level.equals("quit")){
                quit = true;
            }
            else {
                System.out.println("Invalid mode. Please try again.");
            }
        }

        Minefield minefield = new Minefield(rows, cols, flags); //Creates the minefield given the user input

        if (!quit){
            System.out.println(minefield);
            System.out.println("Choose a starting cell: [row] [column]");
            Scanner scanner = new Scanner(System.in);
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            while (x < 0 || x >= rows || y < 0 || y >= rows){ //Makes sure that the user inputted a valid coordinate
                System.out.println("Invalid coordinate. Try again: [row] [column]");
                x = scanner.nextInt();
                y = scanner.nextInt();
            }
            minefield.createMines(x, y, minefield.mines); //Creates mines once the user inputs a valid starting coordinate
            minefield.evaluateField();
            minefield.revealStartingArea(x, y);
        }

        while(!minefield.gameOver() && !quit){ //Loops until the game is over or if the user quit
            System.out.println(minefield);
            if (flagMode){
                System.out.println("FLAG MODE");
            }
            if (debugMode){
                minefield.debug();
            }
            else{
                System.out.println(minefield);
            }
            System.out.println("Enter a coordinate: [row] [column]. If you wish to enter/exit flag mode (Remaining: " + minefield.flags + "), press 'flag'. Press 'quit' to quit or 'debug' to enter debug mode."); //Asks for user input
            Scanner scanner = new Scanner(System.in);
            int x = -1;
            int y= -1;
            String mode = "";
            if (scanner.hasNextInt()) { //Checks if the user inputted an integer
                x = scanner.nextInt();
                y = scanner.nextInt();
                while (x < 0 || x >= rows || y < 0 || y >= rows) { //Checks if the input is valid
                    System.out.println("Choose a valid coordinate. Try again: [row] [column]. If you wish to enter/exit flag mode (Remaining: " + minefield.flags + "), press 'flag'. Press 'quit' to quit or 'debug' to enter debug mode.");
                    if (scanner.hasNextInt()) {
                        x = scanner.nextInt();
                        y = scanner.nextInt();
                    }
                    else { //If the user didn't input an integer, check what mode
                        mode = scanner.nextLine();

                        if (mode.equals("quit")) {
                            quit = true;
                        }
                        else if (mode.equals("debug")) {
                            if (!debugMode) {
                                minefield.debug();
                            }
                            debugMode = !debugMode;
                        }
                        else if (mode.equals("flag")) {
                            if (!flagMode) {
                                System.out.println("FLAG MODE");
                            }
                            flagMode = !flagMode;
                        }
                    }
                }
            }

            else { //If the user didn't input an integer, check what mode

                mode = scanner.nextLine();

                if (mode.equals("quit")) {
                    quit = true;
                }
                else if (mode.equals("debug")) {
                    if (!debugMode) {
                        minefield.debug();
                    }
                    debugMode = !debugMode;
                }
                else if (mode.equals("flag")) {
                    if (!flagMode) {
                        System.out.println("FLAG MODE");
                    }
                    flagMode = !flagMode;
                }
            }


            minefield.guess(x, y, flagMode); //Calls guess() to handle user's action
        }
        minefield.debug(); //Prints out the board with all cells revealed once the game ends
    }
    
}
