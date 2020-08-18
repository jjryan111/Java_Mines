/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mines;

import java.util.Random;

/**
 *
 * @author admin13
 */
final public class GameBoard {

    private Integer[][] board; // The back end board
    private final int numBombs; 
    private int flagsOnBombs = 0; // Keeps track of the number of flagged bombs for victory
   
    
    public GameBoard(int rows, int cols, int numBombs){
        board = new Integer[rows+2][cols+2]; // extra space needed so don't run over arrray bounds
        makeBoard();
        this.numBombs = numBombs;
        addBombs(this.numBombs);
        addHints();
//        revealZeroSquares(1,4);
        
    }
    public void makeBoard(){
        for (int i = 0; i< board.length; i++){
            for (int j = 0; j < board[0].length; j++){
             if ((i == 0 || i == (board.length -1)) || (j == 0 || j == (board[0].length -1))){
                 board[i][j] = 10;
             }
             else board[i][j] = 0;
            }
        }
    }
    public void addBombs(int numBombs){
        Random rnd = new Random();
        int row, col, remainingBombs= numBombs;
        while (remainingBombs > 0) { // Go to a random spot
            row = rnd.nextInt(board.length-1) + 1;
            col = rnd.nextInt(board[0].length-1) + 1;
            if (board[row][col]== 0){ // If there isn't a bomb there
                board[row][col] = 100; // Put one there
                remainingBombs--;
            }
        }
    }
    
    public void addHints(){ // Look for bombs and put in the hints where bombs are near
        for (int i = 0; i< board.length; i++){
            for (int j = 0; j < board[0].length; j++){
             if (board[i][j] >=100) surroundingSquares(i,j);
            }
        }
    }
    private void surroundingSquares(int row, int col){
        // Helper method for adding hints to the spaces surrounding a bomb
        // This is why we need the extra rows/columns
        for (int i = -1; i<=1; i++){
            for (int j = -1; j <=1; j++){
                board[row+i][col+j]+= 1;
            }
        }
    }
    
    public void printConsoleBoard(){ // For debugging
        for (int i = 0; i< board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                System.out.print(board[i][j] + "\t");
            } 
            System.out.println();
        }
    }
    
    private void revealZeroSquares(int row, int col){
        // This method recursively uncovers squares where 
        // there are no bombs in proximity and a square is played
        if (board[row][col] == 0){
            board[row][col] = 10;
            for (int i = -1; i<=1; i++){
                for (int j = -1; j <=1; j++){
                    revealZeroSquares(row+i, col+j);
                }
            } 
        }
    }
    
    public String getSquare(int row, int col){ // Returns the string to add to the buttons
        if (board[row][col] > 99) return "*";
        else {
            Integer temp = (board[row][col])-10;
            return temp.toString();
        }
    }
    
    public int getSquareVal(int row, int col){ // Returns the integer value of the square
        return board[row][col];
    }
    
    public boolean playIt(int row, int col){ // The left click play is made here
        if (board[row][col] > 99) { // The played square is a bomb
//            System.out.println("BOOM!");
            return false; // You lose
        }
        else{       
            if (board[row][col] == 0){
                revealZeroSquares(row, col);   
                // If a square with nothing in it is played adjacent squares 
                // with nothing in them are revealed
            }
            else if (board[row][col] < 10) { // The played square is a hint square
                board[row][col] += 10;
            }
            return true;
        }
    }
    
    public boolean flagIt(int row, int col){ // The right click play is made here
        boolean gameFinished = false;
        if (board[row][col] > 99){ // If the square is a bomb
            flagsOnBombs++;
            if (flagsOnBombs == numBombs) { // VICTORY!!
               gameFinished = true;
            }
        }
        return gameFinished;
    }
    
    public void unflagIt(int row, int col){
        if (board[row][col] > 99){
            flagsOnBombs--;
        }
    }
}
