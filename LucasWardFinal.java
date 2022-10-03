/* Lucas Ward
 * CSC 191
 * Final Project
 * April 30, 2020
 * Purpose: Create playable "Find the Queen" game in a gui using recursive methods and 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

//create the board superclass that will create the gui, thusly extending the JFrame
class Board extends JFrame {
    Scanner in = new Scanner(System.in);
    
    //int array that stores how many buttons will be placed
    private JButton[][] buttons;
    //store the boardsize, measured as both length and width
    int boardSize;
    //add JLabel to keep score
    private JLabel gameScore;
    private JLabel queensLeft;
    private JPanel buttonPanel;
    private JPanel scorePanel;
    
    //Board constructor
    Board() {

    }//end constructor
    
    //getter and setter for board size
    public int getBoardSize() {
        return boardSize;
    }
    protected void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }
    
    //getter and setter for buttonPanel JPanel
    public JPanel getButtonPanel() {
        return buttonPanel;
    }
    protected void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }
    
    //getter and setter for scorePanel JPanel
    public JPanel getScorePanel() {
        return scorePanel;
    }
    protected void setScorePanel(JPanel scorePanel) {
        this.scorePanel = scorePanel;
    }
    
    //getter and setter for gameScore JLabel
    public JLabel getGameScore() {
        return gameScore;
    }
    protected void setGameScore(JLabel gameScore) {
        this.gameScore = gameScore;
    }
    
    //getter and setter for the queens left JLabel
    public JLabel getQueensLeft() {
        return queensLeft;
    }
    protected void setQueensLeft(JLabel queensLeft) {
        this.queensLeft = queensLeft;
    }
    
    //getter and setter for buttons[][] 
    public JButton[][] getButtons(){
        return buttons;
    }
    protected void setButtons(JButton[][] buttons){
        this.buttons = buttons;
    }    
    
}//end Board class

//create a queens subclass that extends from the board superclass, performs more specific tasks to the game itself
class Queens extends Board {
    Scanner in = new Scanner(System.in);
    
    //JPanel for holding the game buttons
    private JPanel buttonPanel;
    //int array that stores how many buttons will be placed
    final JButton[][] buttons;
    //store the boardsize, measured as both length and width
    int boardSize;
    
    //add JLabel to display score and queens left
    private JLabel gameScore = getGameScore();
    private JLabel queensLeft = getQueensLeft();
    
    //add JPanel to display the gameScore and queensLeft JLabels
    private JPanel scorePanel = getScorePanel();
    
    //keep track of the score and queens left; act as counters while running through programs
    private int score;
    private int queensRemaining;
    
    //2d array to store the locations of the queens for the game
    private int[][] queens;
    
    boolean firstClick = true;  //tracks if the first click is what initiates the action listener
    boolean stop = false;
    boolean gameEnded;
    
    
    Queens() {
        
        System.out.println("Enter the size of the game board: ");
        boardSize = in.nextInt();
        buttons = new JButton[boardSize][boardSize];
        score = 0;
        queens = new int[2][(boardSize*boardSize)/4]; //intialize 2d array that holds x and y values of queen locations
        queensRemaining = (boardSize*boardSize)/4;
        addQueens();
        setButtons();   //method initializes JButton 2d array with actual buttons
        createGUI();
    }
    private void setButtons(){
        
        //initialize buttons inside of buttons array
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new JButton("*");   //create JButtons with initial *
                buttons[i][j].setFont(new Font("TimesNewRoman",Font.PLAIN, 35));
                
                buttons[i][j].addActionListener((ActionEvent event) -> {
                    buttonClicked(event.getSource(), firstClick);
                } //end of our actionPerformed method
                );
            }
        }
    }//end setButtons method

    //adds functionality to the buttons
    private void buttonClicked(Object b, boolean firstClick) {
        stop = false;
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                if (b.equals(buttons[x][y])) {
                    for (int i = 0; i < queens[0].length; i++) {
                        if(queens[0][i]==x && queens[1][i]==y && firstClick){   //only replaces the queen if first click is the one that finds it
                            
                            buttons[x][y].setText("\u2655");
                            //set the queens as found and disable the user from repeatedly clicking the same spot for score
                            queens[0][i] = -1;
                            queens[1][i] = -1;
                            
                            gameScore.setText("Score: "+ ++score);
                            queensLeft.setText("Queens Remaining: " + --queensRemaining);
                            return; //end button clicked method early
                        }else if(queens[0][i] == x && queens[1][i] == y && !firstClick)
                            stop = true;
                            
                    }
                    
                }
            }
        }
        //only runs if there was no queen selected  //recursive calls if there are tiles present to the sides
        clearButtons(b); //recursively call itself back, split into separate method to tidy up::: not working properly as seen in gameplay
        
        //check if the game ends by the button pressing
        gameEnded = false;
        if(!gameEnded)
            checkGameEnd();
    }
    
    //recursive method to clear buttons
    private void clearButtons(Object b) {
        boolean hiddenQueen = false;
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                if (b.equals(buttons[x][y])) {
                    if (!buttons[x][y].getText().equals("\u2655")) {  //
                        if (buttons[x][y].getText().equals("")) {
                            return;
                        }
                        for (int i = 0; i < queens[0].length; i++) {
                            if(queens[0][i]==x && queens[1][i]==y)
                                hiddenQueen = true;
                        }
                        if(!hiddenQueen)    //only sets the spot to be empty if there is no queen hidden there
                            buttons[x][y].setText("");
                        
                        //the eight recursive calls to check touching spaces
                        if (x > 0 && !stop) {
                            buttonClicked(buttons[x - 1][y], false);
                        }
                        if (y < boardSize - 1 && !stop) {
                            buttonClicked(buttons[x][y + 1], false);
                        }
                        if (x < boardSize - 1 && !stop) {
                            buttonClicked(buttons[x + 1][y], false);
                        }
                        if (x > 0 && y > 0 && !stop) {
                            buttonClicked(buttons[x - 1][y - 1], false);
                        }
                        if (y > 0 && !stop) {
                            buttonClicked(buttons[x][y - 1], false);
                        }
                        if (x > 0 && y < boardSize - 1 && !stop) {
                            buttonClicked(buttons[x - 1][y + 1], false);
                        }
                        
                        if (x < boardSize - 1 && y < boardSize - 1 && !stop) {
                            buttonClicked(buttons[x + 1][y + 1], false);
                        }
                        
                        if (x < boardSize - 1 && y > 0 && !stop) {
                            buttonClicked(buttons[x + 1][y - 1], false);
                        }
                    }
                }
            }

        }
    }
    
    //check if the game has ended, either win or lose outcome
    private void checkGameEnd() {
        
        if(queensRemaining == 0){
            JLabel winNote = new JLabel("\tCongratulations, you found all the queens!");
            winNote.setFont(new Font("SansSerif",Font.PLAIN, 14));
            scorePanel.add(winNote);
            buttonPanel.setVisible(false);
            return;
        }
        
        //method will null return if the location of * is not the location of a hidden queen 
        boolean queenFound = false;
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                if(buttons[x][y].getText().equals("")) {//if the button is empty continue searching
                } else {    //find a non empty button and determine if it is a queen 
                    if(!buttons[x][y].getText().equals("\u2655")){
                        for (int i = 0; i < queens[0].length; i++) {    //determine if the * there is a hidden queen
                            if(x == queens[0][i] && y == queens[1][i])
                                queenFound = true;  //break if the spot is a hidden queen
                        }
                        
                    }
                } 
            }
        }
        if(!queenFound) {
            JLabel loseNote = new JLabel("Sorry, you lost the game. Better luck next time!");
            loseNote.setFont(new Font("SansSerif", Font.PLAIN, 14));
            scorePanel.add(loseNote);
            buttonPanel.setVisible(false);
            gameEnded = true;
        }
        
    }
    
    //make queens array
    private void makeQueensArray() {
        Random rnd = new Random();
        for (int i = 0; i < (boardSize*boardSize)/4; i++) {
            queens[0][i] = rnd.nextInt(boardSize);
            queens[1][i] = rnd.nextInt(boardSize);
        }
    }
    
    //check that the queens are not placed in the same spot
    private void addQueens() {
        boolean containsDupes = true;  //set to true if there are duplicate queen locations
        
        //add queens to board locations
        makeQueensArray();
        while (containsDupes) {
            containsDupes = false;
            //check for duplicates
            for (int i = 0; i < (boardSize*boardSize)/4; i++) {
                for (int j = i+1; j < (boardSize*boardSize)/4; j++) {
                    if((queens[0][i] == queens[0][j]) && (queens[1][i] == queens[1][j]) && i!=j){
                        containsDupes = true;
                    }
                }
            }
            //make new coordinates if there is a duplicate found
            if(containsDupes)
                makeQueensArray();
        }
////////////////////////////////////////////////Uncomment this line to display the locations of the queens
//        for (int i = 0; i < (boardSize*boardSize)/4; i++) {
//            System.out.println("(" + queens[0][i] + ", " + queens[1][i]+")");
//        }
    }
    
    //method to fully create the interface seen by the user
    private void createGUI() {
        //get content pane to add gui components to
        Container contentPane = getContentPane();
        
        //create the buttonPanel JPanel and set to GridLayout to display buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(boardSize,boardSize));
        
        //add buttons to buttonPanel
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                
                buttonPanel.add(buttons[i][j]);
            }
        }
        contentPane.add(buttonPanel, BorderLayout.CENTER);  //Add the buttonPanel to the contentpane
        
        //add the score panel above the button panel, formatted to disply left to right with FlowLayout
        scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        contentPane.add(scorePanel, BorderLayout.NORTH);
        
        gameScore = new JLabel();
        gameScore.setFont(new Font("SansSerif",Font.PLAIN, 14));
        gameScore.setText("Score: "+score);
        scorePanel.add(gameScore);
        
        //add Queensleft JLabel to the scorePanel to display above buttons
        queensLeft = new JLabel();
        queensLeft.setFont(new Font("SansSerif",Font.PLAIN, 14));
        queensLeft.setText("Queens Remaining: "+queensRemaining);
        scorePanel.add(queensLeft);

        //set application window
        setTitle("Rescue the Queens");
        setSize(1150,750);
        setVisible(true);
    }     
}//end Queens class

//main class for project
public class LucasWardFinal {

    public static void main(String[] args) {
        Queens board = new Queens();
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}//end main class
