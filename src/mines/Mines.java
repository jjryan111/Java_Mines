/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mines;


import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author admin13
 */
public class Mines extends Application {
    Text statusText, flagText, helpText;
    
    private final MyBtn[][] MYBUTTONS;
    private final int ROWS= 10;
    private final int COLS= 10;
    private final int NUMBOMBS = 10;
    private int flags = NUMBOMBS;
    private GameBoard game;
    private GridPane gpane;
    private final Font font1 = Font.font("Times", FontWeight.BOLD, 12);

    public Mines() {
        this.MYBUTTONS = new MyBtn[ROWS][COLS];
    }
    
    @Override
    public void start(Stage primaryStage) {
        // Make the front end board
        
        gpane = new GridPane();
        game = new GameBoard(ROWS, COLS, NUMBOMBS);
//        game.printConsoleBoard();
        statusText = new Text("Status: ");
        flagText = new Text("Flags remaining: " + flags);
        helpText = new Text("Left click to play, right click to flag");
        makeButtons();
        gpane.add(statusText, 25, 4);
        gpane.add(flagText, 25, 8);
        gpane.add(helpText, 25, 1);
        Scene scene = new Scene(gpane, 500, 400);
        primaryStage.setTitle("My Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void processButtonPress(MouseEvent event){ // Left and right click plays are executed
        int row, col;
        for (MyBtn btnCol[] : MYBUTTONS){ // Go through the button array
            for (MyBtn btn : btnCol){
                if (btn.equals(event.getSource())){ // Find the button that was clicked
                    row = btn.getButtonID()%100; // Decode the ID to get row and column
                    col = ((btn.getButtonID() - (btn.getButtonID()%100))/100); 
                    if ( event.getButton() == MouseButton.PRIMARY ){
                        lClickPlay(row, col);
                    }
                    else if ( event.getButton() == MouseButton.SECONDARY ){
                       rClickPlay(row, col, btn);
                        
                }
                flagText.setText("Flags remaining: " + flags);
                }
            }
        }
    } 

    public void updateButtonText(boolean done){ // Redraw the board after a play
        // This is necessary because when an empty square is played more than 
        // one button may be revealed
    int row, col;
    for (MyBtn btnCol[] : MYBUTTONS){
        for (MyBtn btn : btnCol){
            row =  btn.getButtonID()%100;
            col = ((btn.getButtonID() - (btn.getButtonID()%100))/100);
            if (!done){
                if ((game.getSquareVal(row, col) >= 10) && (game.getSquareVal(row, col) < 99 )){
               btn.setText(game.getSquare(row,col));
               btn.setFont(font1);
               btn.setTextFill(setButtonTextColor(game.getSquareVal(row, col)-10));
             }
            }
            else if (game.getSquareVal(row, col) > 99 ) btn.setText(game.getSquare(row,col));
            }
        }
    }
    public void makeButtons(){ // Build the front end board as an array of buttons
        for (int i=0; i<ROWS; i++)
        {
            for (int j = 0; j<COLS; j++){
            MYBUTTONS[i][j] = new MyBtn(((i+1)*100)+j+1); //Encode row and column into buttonID
            MYBUTTONS[i][j].setMinSize(25, 25);
            gpane.add(MYBUTTONS[i][j], i, j);
            MYBUTTONS[i][j].setOnMouseClicked(this::processButtonPress);
            }
        }
    }
    
    public void lClickPlay(int row, int col) { 
        // Gets the button played and passes it to the back end board
        if (game.playIt(row,col)){ // Makes the play and determines loss condition
            updateButtonText(false);
            statusText.setText("Playing");
        }
        else{
            updateButtonText(true);
            statusText.setText("LOSER!!");
        }
    }
    
    public void rClickPlay(int row, int col, MyBtn btn){
        if (btn.getText().equals("\u2690")){ // If already flagged you get a flag back
                game.unflagIt(row, col);
                flags++;
                btn.setText(" ");
            }
        else { 
            btn.setText("\u2690"); // Flag it and pass to back end to determine loss
            if (!game.flagIt(row, col)) {
                flags--;
                if (flags == 0){ // If you use up your flags you lose
                    updateButtonText(true);
                    statusText.setText("LOSER!!");
                }
            }
            else {
                updateButtonText(true); // Unless you flag only bombs
                statusText.setText("WINNER!!");
            }
        }
    }
    
    public Color setButtonTextColor(int val){
        switch(val){
            case 0:
                return Color.WHITE;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.BROWN;
            case 3:
                return Color.RED;
            case 4:
                return Color.YELLOW;
            case 5:
                return Color.DARKBLUE;
            case 6:
                return Color.GREEN;
            case 7:
                return Color.PURPLE;
            case 8:
                return Color.BLACK;
            default:
                return Color.DARKMAGENTA;
        }
             
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
