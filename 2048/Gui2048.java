/** Gui2048.java */
/** PSA8 Release */

/* Name: Austin Nim
 * Login: Cse8bwarm
 * Pid: A92137507
 * Date: March 3, 2016
 * File: Gui2048.java
 */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

public class Gui2048 extends Application
{
   private String outputBoard; // The filename for where to save the Board
   private Board board; // The 2048 Game Board

   private static final int TILE_WIDTH = 106;

   private static final int TEXT_SIZE_LOW = 30; // Low value tiles (2,4,8,etc)
   private static final int TEXT_SIZE_MID = 25; // Mid value tiles 
   //(128, 256, 512)
   private static final int TEXT_SIZE_HIGH = 20; // High value tiles 
   //(1024, 2048, Higher)

   // Fill colors for each of the Tile values
   private static final Color COLOR_EMPTY = Color.rgb(100, 100, 100, 0.75);
   private static final Color COLOR_2 = Color.rgb(230, 240, 230);
   private static final Color COLOR_4 = Color.rgb(190, 224, 240);
   private static final Color COLOR_8 = Color.rgb(130, 177, 250);
   private static final Color COLOR_16 = Color.rgb(100, 149, 250);
   private static final Color COLOR_32 = Color.rgb(90, 124, 250);
   private static final Color COLOR_64 = Color.rgb(40, 200, 160);
   private static final Color COLOR_128 = Color.rgb(50, 210, 114);
   private static final Color COLOR_256 = Color.rgb(100, 204, 80);
   private static final Color COLOR_512 = Color.rgb(100, 230, 80);
   private static final Color COLOR_1024 = Color.rgb(250, 100, 100);
   private static final Color COLOR_2048 = Color.rgb(250, 50, 150);
   private static final Color COLOR_OTHER = Color.BLACK;
   private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

   private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
   // For tiles >= 8

   private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
   // For tiles < 8

   private GridPane pane;

   /** Add your own Instance Variables here */
   //Stores rectange objects, which will be accessed in order to change the
   //color each tile as the values within them change.
   private Rectangle[][] tile;
   //Updates the value within each tile.
   private Text[][] textTile;
   //Keeps track of the score.
   private Text scoreText;
   //Creates a stackpane which will hold the grid pane and game over overlay.
   private StackPane stackPane;
   //Stores the size of the gap between tiles.
   private int gap;
   private boolean canMoveStill;

   /* Name: start
    * Purpose: The purpose of this method is to initialize the game board for
    *          2048. In addition it contains methods that handle the actualy
    *          gameplay of 2048.
    * Parameters: Stage primaryStage is the main component that contains all the
    *             panes that are created. primaryStage is the stage that will
    *             display the actual board of 2048.
    * Return: Returns void.
    */ 
   @Override
      public void start(Stage primaryStage)
      {
         // Process Arguments and Initialize the Game Board
         processArgs(getParameters().getRaw().toArray(new String[0]));

         // Create the pane that will hold all of the visual objects
         pane = new GridPane();
         pane.setAlignment(Pos.CENTER);
         pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
         pane.setStyle("-fx-background-color: rgb(200, 180, 180)");
         // Set the variable gap 
         setGap();
         this.canMoveStill = true;
         //Create a stackpane which will hold the grid pane and the game over
         //overlay.
         stackPane = new StackPane();
         //Sets the gap between tiles
         pane.setHgap(getGap()); 
         pane.setVgap(getGap());
      
         /** Add your Code for the GUI Here */
         //Initializes the scene and then creates the display. 
         Scene scene = new Scene(stackPane, 500, 600);
         primaryStage.setTitle("Gui2048");
         primaryStage.setScene(scene);
         primaryStage.show();
         this.createRectangle(pane, this.board.getGrid().length, scene);
         this.createHeader(pane, this.board.getGrid().length);
         stackPane.getChildren().addAll(pane);
         scene.setOnKeyPressed(new myKeyHandler());
      }

   /* Name: myKeyHandler
    * Purpose: This class handles the input from the keyboard, which then
    *          adjusts the board. 
    * Parameters: No parameters.
    * Return: No return type. 
    */ 
   private class myKeyHandler implements EventHandler<KeyEvent>
   {
   /* Name: handle
    * Purpose: The purpose of this method is to handle all the user input.
    *          Depending on what the user enters the tiles will either move or
    *          stand still. In addition, there is an option to save the board.
    * Parameters: KeyEvent e is part of the Keyevent class which stores the
    *             information of the key that was hit.
    * Return: Returns void.
    */ 
  @Override
         public void handle(KeyEvent e)
         {
            /* KeyEvent Processing Code Goes Here */
            if (board.canMove(Direction.UP) || board.canMove(Direction.LEFT) ||
               board.canMove(Direction.RIGHT) || board.canMove(Direction.DOWN))
            {
               if(e.getCode().equals(KeyCode.UP))
               {
                  moveUp(Direction.UP);
               }
               else if(e.getCode().equals(KeyCode.DOWN))
               {
                  moveDown(Direction.DOWN);
               }
               else if(e.getCode().equals(KeyCode.LEFT))
               {
                  moveLeft(Direction.LEFT);
               }
               else if(e.getCode().equals(KeyCode.RIGHT))
               {
                  moveRight(Direction.RIGHT);
               }
               else if (e.getCode().equals(KeyCode.R))
               {
                  canRotate();
               }
               else if(e.getCode().equals(KeyCode.S))
               {
                  try
                  {
                     board.saveBoard(outputBoard);
                     System.out.println("Saving board to " + outputBoard);
                  }
                  catch (IOException a)
                  {
                     System.out.println("saveBoard threw an Exception");
                  }
               }
            }
            else 
            {
               gameOverBoard();
            }
         }
   }

   /** Add your own Instance Methods Here */

   /* Name: setGap()
    * Purpose: This setter sets the gap size.
    * Parameters: No parameters.
    * Return: Returns void.
    */
   private void setGap()
   {
         if (this.board.getGrid().length <= 9)
         {
            this.gap = 15;
         }
         else if (this.board.getGrid().length <= 15)
         {
            this.gap = 10;
         }
         else if(this.board.getGrid().length <=20)
         {
            this.gap = 5;
         }
         else if(this.board.getGrid().length <=25)
         {
            this.gap = 3;
         }
         else if(this.board.getGrid().length >= 30)
         {
            this.gap = 1;
         }

   }
   /* Name:getGap
    * Purpose: This getter returns the value of the field variable gap.
    * Parameters: No parameters.
    * Return: Returns an int, which is the gap size between each tile.
    */ 
   private int getGap()
   {
      return this.gap;
   }

   /* Name: createRectangle
    * Purpose: The purpose of this method is to initialize the initial board for
    *          2048. It adds a specified number of nodes into the grid pane.
    * Parameters: GridPane pane is used to add the tiles onto the GUI. Int
    *             gridSize determines how many times will be added to the board.
    * Return: Returns void.
    */ 
   private void createRectangle(GridPane pane,int gridSize, Scene scene)
   {

      //Creates the grid by intializing a 2d array, and placing a rectangle
      //object in each position of the array.
      this.tile = new Rectangle[gridSize][gridSize];
      this.textTile = new Text[gridSize][gridSize];
      for (int row = 0; row < gridSize; row++)
      {
         for (int column = 0; column < gridSize; column++)
         {
            //Creates a new rectangle with a specific height and width based
            //upon how big the board is.
            this.tile[row][column] = new Rectangle();
            int resize = 4/gridSize;
            this.tile[row][column].heightProperty().bind(pane.heightProperty()
            .subtract((getGap() + 100+this.gap*(gridSize-1))).divide(gridSize));
            this.tile[row][column].widthProperty().bind(pane.widthProperty()
            .subtract((50 + this.gap*(gridSize -1))).divide(gridSize)); 
            this.textTile[row][column] = new Text();
            //Checks to see where the two initial random tiles are and changes
            //the color scheme accordingly.
            if (this.board.getGrid()[row][column] == 2)
            {
               this.tile[row][column].setFill(this.COLOR_2);
               this.textTile[row][column].setText("2");
            }
            else if (this.board.getGrid()[row][column] == 4)
            {
               this.tile[row][column].setFill(this.COLOR_4);
               this.textTile[row][column].setText("4");
            }
            else
            {
               this.tile[row][column].setFill(this.COLOR_EMPTY);
               this.textTile[row][column].setText("");
            }
            int fontSize = TEXT_SIZE_LOW - gridSize;
            if (fontSize < 6)
            {
               fontSize = 6;
            }
            this.textTile[row][column].setFill(COLOR_VALUE_DARK);
            this.textTile[row][column].setFont(Font.font("Comic San MS",
                     FontWeight.BOLD, fontSize));
            //Adds the tile and text onto the pane.
            pane.add(this.tile[row][column], column , row + 2);
            pane.add(this.textTile[row][column], column  , row + 2);
            //Aligns the text.
            GridPane.setHalignment(this.textTile[row][column], HPos.CENTER);
         }
      }
   }

   /* Name: createHeader
    * Purpose: The purpose of this method is to create the header of the game,
    *          which includes the game's name and the player's score.
    * Parameters: GridPane pane is the pane which the score and game's name are
    *             added to. When the method is called it creates the display of
    *             the game.
    * Return: Returns void.
    */
   private void createHeader(GridPane pane, int gridSize)
   {
      //Creates the scoreboard
      this.scoreText = new Text();
      this.scoreText.setText("Score: " + this.board.getScore());
      this.scoreText.setFill(Color.WHITE);
      int fontSize = 44 - gridSize;
      if(fontSize < 14)
      {
         fontSize = 14;
      }
      this.scoreText.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
      pane.add(this.scoreText, gridSize/2, 0, gridSize/2 , 2);
      GridPane.setHalignment(scoreText, HPos.CENTER);
      //Creates the game's name "2048."
      Text nameText = new Text();
      nameText.setText("2048");
      nameText.setFill(Color.WHITE);
      nameText.setFont(Font.font("Arial", FontWeight.BOLD, 
               FontPosture.ITALIC, fontSize));
      pane.add(nameText, 0, 0, gridSize/2, 2);
      GridPane.setHalignment(nameText, HPos.CENTER);
   }

   /* Name: updateBoard
    * Purpose: The purpose of this method is to update the tiles of the display.
    * Parameters: No parameters.
    * Return: Returns void.
    */ 
   private void updateBoard()
   {
      //Loops through all tiles in the board and updates it's text and color
      //scheme. 
      Text number = new Text();
      for (int row = 0; row < this.board.getGrid().length; row++)
      {
         for (int column = 0; column < this.board.getGrid().length; column++)
         {
            int value = this.board.getGrid()[row][column];
            if (value == 0)
            {
               this.tile[row][column].setFill(this.COLOR_EMPTY);
               this.textTile[row][column].setText("");
            }
            else if (value == (2))
            {
               this.tile[row][column].setFill(this.COLOR_2);
               this.textTile[row][column].setText("2");
            }
            else if (value == (4))
            {  
               this.tile[row][column].setFill(this.COLOR_4);
               this.textTile[row][column].setText("4");
            }
            else if (value == (8))
            {
               this.tile[row][column].setFill(this.COLOR_8);
               this.textTile[row][column].setText("8");
            }
            else if (value == (16))
            {
               this.tile[row][column].setFill(this.COLOR_16);
               this.textTile[row][column].setText("16");
            }
            else if (value == (32))
            {
               this.tile[row][column].setFill(this.COLOR_32);
               this.textTile[row][column].setText("32");

            }
            else if (value == (64))
            {
               this.tile[row][column].setFill(this.COLOR_64);
               this.textTile[row][column].setText("64");

            }
            else if (value == (128))
            {
               this.tile[row][column].setFill(this.COLOR_128);
               this.textTile[row][column].setText("128");

            }
            else if (value == (256))
            {
               this.tile[row][column].setFill(this.COLOR_256);
               this.textTile[row][column].setText("256");

            }
            else if (value == (512))
            {
               this.tile[row][column].setFill(this.COLOR_512);
               this.textTile[row][column].setText("512");

            }
            else if (value == (1024))
            {
               this.tile[row][column].setFill(this.COLOR_1024);
               this.textTile[row][column].setText("1024");

            }
            else if (value == (2048))
            {
               this.tile[row][column].setFill(this.COLOR_2048);
               this.textTile[row][column].setText("2048");
            }
            else
            {
               this.tile[row][column].setFill(this.COLOR_OTHER);
               this.textTile[row][column].setText("" +
               this.board.getGrid()[row][column]);
            }
            //Updates the color of text.
            if(value >= 8)
            {
               this.textTile[row][column].setFill(COLOR_VALUE_LIGHT);
            }
            else
            {
               this.textTile[row][column].setFill(COLOR_VALUE_DARK);
            }
            int fontSize = 0; 
            //Updates the size of text.
            if (value < 8)
            {
               fontSize = TEXT_SIZE_LOW + 4 - this.board.getGrid().length;
            }
            else  if ( value >= 8 && value < 512)
            {
               fontSize = TEXT_SIZE_MID + 4 - this.board.getGrid().length;
            }
            else if (value >= 512)
            {
               fontSize = TEXT_SIZE_HIGH + 4 - this.board.getGrid().length;
            }
            //If the font size is too small it is set to 5.
            if(fontSize < 5)
            {
               fontSize = 5;
            }
            this.textTile[row][column].setFont(Font.font("Comic San MS",
                     FontWeight.BOLD, fontSize));
            //Updates the score.
            this.scoreText.setText("Score: " + this.board.getScore());
            GridPane.setHalignment(this.scoreText, HPos.CENTER);
            GridPane.setHalignment(this.textTile[row][column], HPos.CENTER);
         }
      }
   }

   /* Name: moveUp
    * Purpose: Checks too see if the tiles can move up and then implements the
    *          move if the tiles can move in the specified direction.
    * Paremeters: Direction direction is used to determine which way the tiles
    *             will move.
    * Return: Returns void
    */ 
   private void moveUp(Direction direction)
   {
      //Checks to see if the move can be made first. Then the move method is
      //executed to movethe tiles.
      if(this.board.canMove(Direction.UP))
      {
         this.board.move(Direction.UP);
         this.board.addRandomTile();
         updateBoard();
         System.out.println("Moving up");
      }
      else
      {
         System.out.println("Cannot move up");
      }
   }

   /* Name: moveDown
    * Purpose: Checks too see if the tiles can move down and then implements the
    *          move if the tiles can move in the specified direction.
    * Paremeters: Direction direction is used to determine which way the tiles
    *             will move.
    * Return: Returns void
    */ 
   private void moveDown(Direction direction)
   {
      //Checks to see if the move can be made first. Then the move method is
      //executed to movethe tiles.
      if(this.board.canMove(Direction.DOWN))
      {
         this.board.move(Direction.DOWN);
         this.board.addRandomTile();
         updateBoard();
         System.out.println("Moving down");
      }
      else
      {
         System.out.println("Cannot move down");
      }
   }
   /* Name: moveLeft
    * Purpose: Checks too see if the tiles can move left and then implements the
    *          move if the tiles can move in the specified direction.
    * Paremeters: Direction direction is used to determine which way the tiles
    *             will move.
    * Return: Returns void
    */ 
   private void moveLeft(Direction direction)
   {
      //Checks to see if the move can be made first. Then the move method is
      //executed to movethe tiles.
      if(this.board.canMove(Direction.LEFT))
      {
         this.board.move(Direction.LEFT);
         this.board.addRandomTile();
         updateBoard();
         System.out.println("Moving left");
      }
      else
      {
         System.out.println("Cannot move left");
      }
   }
   /* Name: moveRight
    * Purpose: Checks too see if the tiles can move right and then implements
    *          the move if the tiles can move in the specified direction.
    * Paremeters: Direction direction is used to determine which way the tiles
    *             will move.
    * Return: Returns void
    */ 
   private void moveRight(Direction direction)
   {
      //Checks to see if the move can be made first. Then the move method is
      //executed to movethe tiles.
      if(this.board.canMove(Direction.RIGHT))
      {
         this.board.move(Direction.RIGHT);
         this.board.addRandomTile();
         updateBoard();
         System.out.println("Moving right");
      }
      else
      {
         System.out.println("Cannot move ");
      }
   }

   /* Name: canRotate
    * Purpose: Rotates the board clockwise.
    * Parameters: No parameters.
    * Return: Returns void.
    */
   private void canRotate()
   {
      //Checks to see anymore moves can be made. If they can then the user is
      //able to rotate the board.
      if (this.board.isGameOver() == false)
      {
         boolean canRotate = true;
         this.board.rotate(canRotate);
         updateBoard();
         System.out.println("Rotating");
      }
      else
      {
         System.out.println("Cannot rotate");
      }
   }

   /* Name: gameOverBoard
    * Purpose: The purpose of this method is to check if the game is over, and
    *          create a display to reveal that the game is over.
    * Parameters: No parameters.
    * Return: Returns void.
    */ 
   private void gameOverBoard()
   {
      //isGameOver checks to see if any moves can be made and if no moves can be
      //made then it returns true.
      if (this.board.isGameOver() && this.canMoveStill)
      {
         this.canMoveStill = false;
         //Initializes the game over overlay.
         Rectangle gameOver = new Rectangle();
         //Binds the width and height to the stackpane's width and height.
         gameOver.heightProperty().bind(this.stackPane.heightProperty());
         gameOver.widthProperty().bind(this.stackPane.widthProperty());
         Text gameIsOver = new Text();
         gameIsOver.setText("GAME OVER!");
         gameOver.setFill(this.COLOR_GAME_OVER);
         gameIsOver.setFill(Color.BLACK);
         gameIsOver.setFont(Font.font("Arial", FontWeight.BOLD,
                            FontPosture.ITALIC, 60));
         //Adds the text and rectangle to the stackpane.
         this.stackPane.getChildren().addAll(gameOver, gameIsOver);
         StackPane.setAlignment(gameIsOver, Pos.CENTER);
      }
      else if (!this.board.isGameOver() && this.canMoveStill) 
      {
         this.canMoveStill = true;
      }
   }
   /** DO NOT EDIT BELOW */

   // The method used to process the command line arguments
   private void processArgs(String[] args)
   {
      String inputBoard = null;   // The filename for where to load the Board
      int boardSize = 0;          // The Size of the Board

      // Arguments must come in pairs
      if((args.length % 2) != 0)
      {
         printUsage();
         System.exit(-1);
      }

      // Process all the arguments 
      for(int i = 0; i < args.length; i += 2)
      {
         if(args[i].equals("-i"))
         {   // We are processing the argument that specifies
            // the input file to be used to set the board
            inputBoard = args[i + 1];
         }
         else if(args[i].equals("-o"))
         {   // We are processing the argument that specifies
            // the output file to be used to save the board
            outputBoard = args[i + 1];
         }
         else if(args[i].equals("-s"))
         {   // We are processing the argument that specifies
            // the size of the Board
            boardSize = Integer.parseInt(args[i + 1]);
         }
         else
         {   // Incorrect Argument 
            printUsage();
            System.exit(-1);
         }
      }

      // Set the default output file if none specified
      if(outputBoard == null)
         outputBoard = "2048.board";
      // Set the default Board size if none specified or less than 2
      if(boardSize < 2)
         boardSize = 4;

      // Initialize the Game Board
      try{
         if(inputBoard != null)
            board = new Board(inputBoard, new Random());
         else
            board = new Board(boardSize, new Random());
      }
      catch (Exception e)
      {
         System.out.println(e.getClass().getName() + 
               " was thrown while creating a " +
               "Board from file " + inputBoard);
         System.out.println("Either your Board(String, Random) " +
               "Constructor is broken or the file isn't " +
               "formated correctly");
         System.exit(-1);
      }
   }

   // Print the Usage Message 
   private static void printUsage()
   {
      System.out.println("Gui2048");
      System.out.println("Usage:  Gui2048 [-i|o file ...]");
      System.out.println();
      System.out.println("  Command line arguments come in pairs of the "+ 
            "form: <command> <argument>");
      System.out.println();
      System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
            "should be loaded");
      System.out.println();
      System.out.println("  -o [file]  -> Specifies a file that should be " + 
            "used to save the 2048 board");
      System.out.println("                If none specified then the " + 
            "default \"2048.board\" file will be used");  
      System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
            "board if an input file hasn't been"); 
      System.out.println("                specified.  If both -s and -i" + 
            "are used, then the size of the board"); 
      System.out.println("                will be determined by the input" +
            " file. The default size is 4.");
   }
}
