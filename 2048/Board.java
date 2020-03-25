//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  W16-CSE8B-TA group                                      //
// Date:    1/17/16                                                 //
//------------------------------------------------------------------//

/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;

/* Name: Austin Nim
 * Login: Cse8bwarm
 * Pid: A92137507
 * Date: February 4, 2016
 * File: Board.java
 */ 

/* Name: Board
 * Purpose: The board class creates the initial  board for 2048.
 * Parameters: No parameters.
 * Return: No return type.
 */ 

public class Board
{
   public final int NUM_START_TILES = 2;
   public final int TWO_PROBABILITY = 90;
   public final int GRID_SIZE;

   private final Random random;
   private int[][] grid;
   private int score;
   private int[][] undo1;
   private int [][] undo2;
   private int previousScore;
   private int previousScore2;
   private int lastMove;

   /* Name: Board
    * Purpose: This constructor creates a new board with an indicated size. It
    *          creates two tiles on the board. 
    * Parameters: The parameters are int boardSize and Random random. int
    *             boardSize determines the dimensions of the board, and random
    *             helps choose whether a tile will be 2 or 4.
    * Return:
    */  
   public Board(int boardSize, Random random)
   {
      this.undo1 = null;
      this.undo2 = null;
      this.previousScore = 0;
      this.previousScore2 = 0;
      this.lastMove =0;

      //Makes sure that the board is 
      this.random = random;
      //Makes sure the board size is greater than 2, and if it isn't the grid
      //size is assigned to 4, which creats a 4 by 4 board.
      if (boardSize < 2)
      {
         GRID_SIZE = 4;
      }
      else
      {
         GRID_SIZE = boardSize;
      }

      this.grid = new int[GRID_SIZE][GRID_SIZE];
      this.score = 0;
      //Adds two tiles in random locations
      for (int i = 0; i < NUM_START_TILES; i++)
      {
         this.addRandomTile();
      }
   }

   /* Name: Board
    * Purpose:The purpose of this constructor is to create a board that had been
    *          saved.
    * Parameters: The parameters are String inputBoard and Random random.
    *             String inputBoard is used to initialize the board, which is
    *             done by accessing it's elements and creating a board from
    *             them.
    * Return: No return type.
    */  
   public Board(String inputBoard, Random random) throws IOException
   { 
      this.undo1 = null;
      this.undo2 = null;
      this.previousScore = 0;
      this.previousScore2 = 0;
      //reads the file and grabs each element in it
      Scanner input = new Scanner(new File(inputBoard));
      this.random = random;
      GRID_SIZE = input.nextInt();
      this.score = input.nextInt();
      this.grid = new int[GRID_SIZE][GRID_SIZE];
      //Develops the boards by going through each row
      for (int i = 0; i < GRID_SIZE; i++)
      {
         for (int j = 0; j < GRID_SIZE; j++)
         {
            this.grid[i][j] = input.nextInt();
         }
      }
      input.close();
   }

   /* Name: saveBoard
    * Purpose: The purpose of this method is to save the current game into a
    *          file, which can be read again to continue the game. 
    * Parameters: The method takes a string as it's parameter, which is used to
    *             indicate the name of the file where the game is saved. 
    * Return: The return type is void.
    */  
   public void saveBoard(String outputBoard) throws IOException 
   {
      PrintWriter board = new PrintWriter(outputBoard);
      board.println(this.GRID_SIZE);
      board.println(this.score);
      for(int i = 0; i < this.GRID_SIZE; i++)
      {
         for (int j = 0; j <this.GRID_SIZE; j++)
         {
            board.print(this.grid[i][j] + " ");
         }
         board.println();
      }
      board.close();
   }

   /* Name: addRandomTile
    * Purpose: The purpose of this method is to add either 2 or 4 to tiles in
    *          the game everytime a movement on the board is made. Also it is
    *          implemented when a new board is created to get the game started.
    * Parameters: The method has no parameters.
    * Return: The return type is void.
    */  
   public void addRandomTile()
   {
      //Blank is used to locate an open spot
      //Count is used to see if there are any open spots
      int blank = 0;
      int count = 0;
      //The for-loop goes through each row to see if there is an empty spot and
      //increments count if it is empty
      for(int i = 0; i <this.GRID_SIZE; i++)
      {  
         for (int j = 0; j < this.GRID_SIZE; j++)
         {  
            if(this.grid[i][j] == 0)
            {
               count++;
            }
         }
      }
      //If there is no empty spot the method ends
      if (count == 0)
      {
         return;
      }
      //Location is used to insert value at a certain position in the 2d array.
      int location = random.nextInt(count);
      int value = random.nextInt(100);
      if(value < TWO_PROBABILITY)
      {
         value = 2;
      }
      else
      {
         value = 4;
      }
      //The for-loop locates the empty spot at a certain location and then
      //sets the element at that position to value
      for (int i = 0; i < this.GRID_SIZE; i++)
      {
         for (int j = 0; j < this.GRID_SIZE; j++)
         { 
            if (this.grid[i][j] == 0)
            {
               blank++;
               if (blank - 1 == location)
               {
                  this.grid[i][j] = value;
               }
            }
         }
      }

   }
   /* Name: rotate
    * Purpose: The purpose of the method is to rotate the board either clockwise
    *          or counter-clockwise.
    * Parameters: The parameter is a boolean that dicates whether the board will
    *             rotate clockwise(true) or counter-clockwise(false).
    * Return: The return type is void.
    */  
   public void rotate(boolean rotateClockwise)
   {  
      //Creates a new 2d array to store values from grid
      int [][] storeRotation = new int[this.GRID_SIZE][this.GRID_SIZE];

      //If rotateClockwise is true then the program rotates the board clockwise,
      //and if it's false the program rotates the board counter clockwise.
      if (rotateClockwise == true)
      {
         for (int i = 0; i < this.GRID_SIZE; i++)
         {
            for (int j = 0; j< this.GRID_SIZE; j++)
            {
               //Grabs the value by going through each row and then filling each
               //column of storeRotation. Starts filling the leftmost column.
               int value =  this.grid[j][i];
               storeRotation[i][GRID_SIZE - 1 - j] = value;
            }
         }
      }
      else if (rotateClockwise == false)
      {
         for (int i = 0; i < this.GRID_SIZE; i++)
         {
            for (int j = 0; j < this.GRID_SIZE; j++)
            {
               //Goes through the last coulum and then stores that value first
               //in the top right position of storeRotation. Afterwards it fills
               //the rest of the row.
               int value = this.grid[this.GRID_SIZE-j- 1][this.GRID_SIZE-1-i];
               storeRotation[i][this.GRID_SIZE - j - 1] = value;
            }
         }
      }
      //Changes the values in grid to show the new board after being rotated.
      for (int i = 0; i < this.GRID_SIZE; i++)
      {
         for (int j = 0; j < this.GRID_SIZE; j++)
         {
            this.grid[i][j] = storeRotation[i][j];
         }
      }


   }

   /* Name:isInputFileCorrectFormat
    * Purpose: This methods checks to make sure that a board loaded from a file
    *          follows the requirements of a board. Overall, it makes sure that
    *          the file is not corrupt and the board loaded follows the overlay
    *          of the game 2048.
    * Parameters: The method takes in a string as it's parameter. This string is
    *             used to read a file and check if it's corrupt.
    * Return: The method returns a boolean, where true means the file is not
    *         corrupt. If the method returns false that means some aspect of the
    *         loaded board is incorrect in the file.
    */
   public static boolean isInputFileCorrectFormat(String inputFile)
   {
      //The try and catch block are used to handle any exceptions
      //Do not worry about the details, just write all your conditions inside 
      //the try block


      try
      {
         Scanner input = new Scanner (new File (inputFile));

         //Checks to see if there is something inside the file after the first
         //line
         if (input.hasNext() == false)
         {
            return false;
         }   

         int size = input.nextInt();
         int score = input.nextInt();
         int[][]grid = new int[size][size];

         //Checks to make sure the grid size is larger than 2 
         if (size < 2)
         {
            return false;    
         }

         //Checks to see if the score is actually an integer
         if(score != (int)score)
         {
            return false;
         }
         for(int i = 0; i < size; i++)
         {
            for(int j = 0; j< size; j++)
            {
               grid[i][j] = input.nextInt();
            }
         }


         //Checks to make sure that there is no element that is negative and 
         //that each element is a power of two.
         for(int i = 0; i < size; i++)
         {
            for(int j = 0; j< size; j++)
            {
               if (grid[i][j] <  0 || (grid[i][j]/2)%2 != 0)
               {
                  return false;
               }
            }
         }             
         //Checks to make sure that each row is the same length
         for(int i = 0; i < size; i++)
         {
            for(int j = 0; j< size; j++)
            {
               if (grid[i].length != grid[j].length)
               {
                  return false;
               }
            }
         }
         //Checks to make sure that the board is a square.
         //grid.length obtains the length of the column, while grid[j].length
         //gets the length of each row.

         for(int j = 0; j< size; j++)
         {
            if (grid.length != grid[j].length)
            {
               return false;
            }
         }
         input.close();

         String blank = new String ("");
         Scanner input2 = new Scanner(new File(inputFile));
         while (input2.hasNext())
         {  
            String inputWord = new String(input2.next());
            char[] charArray = inputWord.toCharArray();
            if(inputWord.equalsIgnoreCase(blank))
            {
               return false;
            }
            for(int i = 0; i< charArray.length; i++)
            {
               if(Character.isDigit(charArray[i]))
               {
                  return false;
               }
            }
         }
         input2.close();


         return true;
      }
      catch (Exception e)
      {
         return false;
      }


   }

   /* Name: move 
    * Purpose: The purpose of this method is to check is a move can be made and
    *          if it can then the move is made.
    * Parameters: The parameters for this method are Direction direction, which
    *             determines the move that program will execute.
    * Return: The method returns a boolean. If it returns true the move is
    *         executed, but it returns false the move is not executed.
    */ 
   public boolean move(Direction direction)
   {
      if (canMove(direction) == true)
      {
         if (direction.equals(Direction.RIGHT))
         {
            return moveRight();   
         }
         else if (direction.equals(Direction.LEFT))
         {
            return moveLeft();
         }
         else if (direction.equals(Direction.UP))
         {
            return moveUp();
         }
         else if (direction.equals(Direction.DOWN))
         {
            return moveDown();
         }
      } 
      return false;
   }

   /* Name: moveRight
    * Purpose: The purpose of this method is to move the tiles of the board
    *          right. It does this by creating an arraylist. Then it removes all
    *          the zeros and combines tiles that are the same. Next it re-adds
    *          the zeros, which take the place of the tiles and zeros that were
    *          replaced. The arraylist is sorted in order to move the zeros that
    *          have been added to the left of the tiles that do not equal 0.
    * Parameters: No parameters
    * Return: Returns a boolean, where true means the move was executed. This
    *         method is only executed if the method canMove returns true.
    */ 
   private boolean moveRight()
   {
      //Loops through each row and updates that row before moving onto the next.
      for(int row = 0; row < this.GRID_SIZE; row++)
      { 
         ArrayList<Integer> rowColumn= new ArrayList<Integer>();
         //Count is used to keep track of zeros and tiles that are removed
         int count = 0;
         //Creates the arraylist from each row
         for (int column = 0; column < this.GRID_SIZE; column++)
         {
            int value = this.grid[row][column];
            rowColumn.add(column,value);
         }
         //Removes all zeros in the arraylist
         for(int removeZero = 0; removeZero < rowColumn.size(); removeZero++)
         {
            if (rowColumn.get(removeZero) == 0)
            {
               rowColumn.remove(removeZero);
               count++;
               removeZero--;
            }
         }
         //Adds the value of the two elements in the list that are next to one
         //another. Starts from the right side which is the right side of the
         //row.
         for (int addElement = rowColumn.size()-1; addElement > 0; addElement--)
         {  

            if (rowColumn.get(addElement).equals(rowColumn.get(addElement-1)))
            {
               int newValue = (rowColumn.get(addElement)*2);
               rowColumn.set(addElement, newValue);
               rowColumn.set(addElement - 1,0); 
               this.score = this.score + newValue;
            }
         }
         //This loop adds 0 in place of removed zeros and non-zero tiles
         for (int addZero = 0; addZero < count; addZero++)
         {
            rowColumn.add(0);
         }
         //Sorts the array so that 0s are on the left side of non-zero tiles
         for (int sortArray = 0; sortArray < rowColumn.size(); sortArray++)
         {
            for (int sort = rowColumn.size() - 1; sort > 0; sort--)
            {
               if (rowColumn.get(sort) == 0)
               {
                  int store = rowColumn.get(sort - 1);
                  rowColumn.set(sort-1,rowColumn.get(sort));
                  rowColumn.set(sort, store);
               }
            }
         }
         //Copies the updated row back into the board
         for (int finalList = 0; finalList < this.GRID_SIZE; finalList++)
         {
            int valueInArray = rowColumn.get(finalList);
            this.grid[row][finalList] = valueInArray;
         }
      }

      return true;
   }

   /* Name: moveLeft
    * Purpose: The purpose of this method is to move the tiles of the board
    *          left. It does this by creating an arraylist. Then it removes all
    *          the zeros and combines tiles that are the same. Next it re-adds
    *          the zeros, which take the place of the tiles and zeros that were
    *          replaced.
    * Parameters: No parameters
    * Return: Returns a boolean, where true means the move was executed. This
    *         method is only executed if the method canMove returns true.
    */ 
   private boolean moveLeft()
   {
      //Loops through the rows of the board and updates a row before moving onto
      //the next row.
      for(int row = 0; row < this.GRID_SIZE; row++)
      { 
         ArrayList<Integer> rowColumn= new ArrayList<Integer>();
         int count = 0;
         //Creates the arraylist from each row
         for (int column = 0; column < this.GRID_SIZE; column++)
         {
            int value = this.grid[row][column];
            rowColumn.add(value);
         }
         //Removes zeros
         for(int removeZero = 0; removeZero < rowColumn.size(); removeZero++)
         {
            if (rowColumn.get(removeZero) == 0)
            {
               rowColumn.remove(removeZero);
               count++;
               removeZero--;
            }
         }
         //Adds the value of the two elements in the list that are next to one
         //another. Starts from the left side which is the left side of the row.
         for (int addElement = 0; addElement < rowColumn.size()-1; addElement++)
         {  

            if (rowColumn.get(addElement).equals(rowColumn.get(addElement + 1)))
            {
               int newValue = (rowColumn.get(addElement)*2);
               rowColumn.set(addElement, newValue);
               rowColumn.set(addElement+1, 0); 
               this.score = this.score +  newValue;
            }
         }
         //Adds zeros to replace the non-zero and zero tiles that were removed
         for (int addZero = 0; addZero < count; addZero++)
         {
            rowColumn.add(0);
         }
         //Sorts the array so that 0s are on the left side of non-zero tiles
         for (int sortArray = 0; sortArray < rowColumn.size(); sortArray++)
         {
            for (int sort = 0; sort < rowColumn.size() - 1; sort++)
            {
               if (rowColumn.get(sort) == 0)
               {
                  int store = rowColumn.get(sort + 1);
                  rowColumn.set(sort+1,rowColumn.get(sort));
                  rowColumn.set(sort, store);
               }
            }
         }        
         //Copies the updated row back into the board
         for (int finalList = 0; finalList < this.GRID_SIZE; finalList++)
         {
            int valueInArray = rowColumn.get(finalList);
            this.grid[row][finalList] = valueInArray;
         }
      }
      return true;
   }

   /* Name: moveUp
    * Purpose: The purpose of this method is to move the tiles of the board
    *          up. It does this by creating an arraylist. Then it removes all
    *          the zeros and combines tiles that are the same. Next it re-adds
    *          the zeros, which take the place of the tiles and zeros that were
    *          replaced.
    * Parameters: No parameters
    * Return: Returns a boolean, where true means the move was executed. This
    *         method is only executed if the method canMove returns true.
    */ 

   private boolean moveUp()
   {
      //Loops through each column and updates the specified column before
      //updating the rest of the columns.
      for(int column = 0; column < this.GRID_SIZE; column++)
      { 
         ArrayList<Integer> columnRow= new ArrayList<Integer>();
         //Count is used to keep track of zeros and tiles that are removed
         int count = 0;
         //Creates the arraylist from each row
         for (int row = 0; row < this.GRID_SIZE; row++)
         {
            int value = this.grid[row][column];
            columnRow.add(value);
         }
         //Remove zeros
         for(int removeZero = 0; removeZero < columnRow.size(); removeZero++)
         {
            if (columnRow.get(removeZero) == 0)
            {
               columnRow.remove(removeZero);
               count++;
               removeZero--;
            }
         }
         //Adds the value of the two elements in the list that are next to one
         //another. Starts from the left side, which is the top of each column.
         for (int addElement= 0; addElement < columnRow.size()-1 ; addElement++)
         {  

            if (columnRow.get(addElement).equals(columnRow.get(addElement + 1)))
            {
               int newValue = (columnRow.get(addElement)*2);
               columnRow.set(addElement, newValue);
               columnRow.set(addElement + 1, 0);
               this.score =this.score + newValue;
            }
         }
         //Adds zeros to replace non-zero and zero tiles that were removed
         for (int addZero = 0; addZero < count; addZero++)
         {
            columnRow.add(0);
         }
         //Sorts the array so that 0s are on the left side of non-zero tiles
         for (int sortArray = 0; sortArray < columnRow.size(); sortArray++)
         {
            for (int sort = 0; sort < columnRow.size() - 1; sort++)
            {
               if (columnRow.get(sort) == 0)
               {
                  int store = columnRow.get(sort + 1);
                  columnRow.set(sort+1,columnRow.get(sort));
                  columnRow.set(sort, store);
               }
            }
         }  
         //Copies the updated column back into the board
         for (int finalList = 0; finalList < this.GRID_SIZE; finalList++)
         {
            int valueInArray = columnRow.get(finalList);
            this.grid[finalList][column] = valueInArray;
         }
      }
      return true;
   }

   /* Name: moveDown
    * Purpose: The purpose of this method is to move the tiles of the board
    *          Down. It does this by creating an arraylist. Then it removes all
    *          the zeros and combines tiles that are the same. Next it re-adds
    *          the zeros, which take the place of the tiles and zeros that were
    *          replaced.The arraylist is sorted in order to move the zeros that
    *          have been added to the left of the tiles that do not equal 0.
    * Parameters: No parameters
    * Return: Returns a boolean, where true means the move was executed. This
    *         method is only executed if the method canMove returns true.
    */ 
   private boolean moveDown()
   {
      //Loops through each column and updates the specified column before moving
      //onto the next one.
      for(int column = 0; column < this.GRID_SIZE; column++)
      { 
         ArrayList<Integer> columnRow= new ArrayList<Integer>();
         //Count is used to keep track of zeros and tiles that are removed
         int count =0;
         //Creates the arraylist from each row
         for (int row = 0; row < this.GRID_SIZE; row++)
         {
            int value = this.grid[row][column];
            columnRow.add(value);
         }
         //Removes zeros
         for(int removeZero = 0; removeZero < columnRow.size(); removeZero++)
         {
            if (columnRow.get(removeZero) == 0)
            {
               columnRow.remove(removeZero);
               count++;
               removeZero--;
            }
         }
         //Adds the value of the two elements in the list that are next to one
         //another. Starts from the right side which is the bottom of the
         //columnn. 
         for (int addElement = columnRow.size()- 1; addElement > 0;addElement--)
         {  

            if (columnRow.get(addElement).equals(columnRow.get(addElement - 1)))
            {
               int newValue = (columnRow.get(addElement)*2);
               columnRow.set(addElement, newValue);
               columnRow.set(addElement - 1, 0);
               this.score =this.score +  newValue;
            }
         }
         //This loop adds 0 in place of removed zeros and non-zero tiles
         for (int addZero = 0; addZero < count; addZero++)
         {
            columnRow.add(0);
         }
         //Sorts the array so that 0s are on the left side of non-zero tiles
         for (int sortArray = 0; sortArray < columnRow.size(); sortArray++)
         {
            for (int sort = columnRow.size() - 1; sort > 0; sort--)
            {
               if (columnRow.get(sort) == 0)
               {
                  int store = columnRow.get(sort - 1);
                  columnRow.set(sort-1,columnRow.get(sort));
                  columnRow.set(sort, store);
               }
            }
         }
         //Copies the updated column back into the board
         for (int finalList = 0; finalList < this.GRID_SIZE; finalList++)
         {   
            int valueInArray = columnRow.get(finalList);
            this.grid[finalList][column] = valueInArray;
         }
      }
      return true;
   }

   /* Name: isGameOver()
    * Purpose: The purpose of this method is to check whether a move can be
    *          executed. If no move can be executed then the game ends.
    * Paramaters: No parameters. 
    * Return: The method returns a boolean. It either returns false, meaning
    *         the game is still going on, or it returns true which means no more
    *         moves can be made.
    */ 
   public boolean isGameOver()
   { 
      if (canMoveRight() == true)
      {
         return false;
      }
      else if (canMoveLeft()  == true)
      {
         return false;
      }
      else if (canMoveDown() == true)
      {
         return false;
      }
      else if (canMoveUp()  == true)
      {
         return false;
      }
      return true;
   }  

   /* Name: canMove
    * Purpose: The purpose of this method is to test whether the program can
    *         execute the command that the user inputed. It test whether the
    *         tiles can move right, left, up, or down.
    * Paramaters: The method takes in Direction direction as it's parameter.
    *             This parameter is used to determine which way the tiles will
    *             move.
    * Return: The method returns a boolean. If a move can be executed it returns
    *         true and false if the move cannot be executed.
    */ 
   public boolean canMove(Direction direction)
   { 
      if (direction.equals(Direction.RIGHT))
      {
         return canMoveRight();
      }
      else if (direction.equals(Direction.LEFT))
      {
         return canMoveLeft();
      }
      else if (direction.equals(Direction.UP))
      {
         return canMoveUp();
      }
      else if (direction.equals(Direction.DOWN))
      {
         return canMoveDown();
      }
      return false;
   }

   /* Name: canMoveRight
    * Purpose: The purpose of the method is to determine if the tiles on the
    *          board can move right.
    * Paramaters: No parameters
    * Return: The method returns a boolean. If it returns true that means the
    *         move is valid and false if the move cannot be executed.
    */ 
   private boolean canMoveRight()
   {  
      //Loops through the columns.
      for (int i = 0; i < this.GRID_SIZE; i++)
      { 
         ArrayList<Integer> checkArray = new ArrayList<Integer>();
         //Loops through all the rows.
         for (int j = 0; j< this.GRID_SIZE; j++)
         {
            int value = this.grid[i][j];
            checkArray.add(j,value);
         }
         //Loops through the arraylist
         for (int k = 0; k < checkArray.size() - 1; k++)
         {
            //Makes sure that the row contains a tile
            if (checkArray.get(k) != 0)
            {
               //Checks to see if either the tile on the right is the same or
               //zero
               if (checkArray.get(k) == checkArray.get(k+1) ||
                     checkArray.get(k+1) == 0)
               {
                  return true;
               }
            }
         }
      }
      return false;
   }

   /* Name: canMoveLeft
    * Purpose: The purpose of the method is to determine if the tiles on the
    *          board can move left.
    * Paramaters: No parameters
    * Return: The method returns a boolean. If it returns true that means the
    *         move is valid and false if the move cannot be executed.
    */ 
   private boolean canMoveLeft()
   {  
      //Loops through the columns.
      for (int i = 0; i < this.GRID_SIZE; i++)
      { 
         ArrayList<Integer> checkArray = new ArrayList<Integer>();
         //Loops through all the rows.
         for (int j = 0; j< this.GRID_SIZE; j++)
         {
            int value = this.grid[i][j];
            checkArray.add(j,value);
         }
         //Loops through the arraylist
         for (int k = checkArray.size() - 1; k > 0; k--)
         {
            //Makes sure that the row contains a tile
            if (checkArray.get(k) != 0)
            {
               //Checks to see if either the tile on the left is the same or
               //zero
               if (checkArray.get(k) == checkArray.get(k-1) ||
                     checkArray.get(k-1) == 0)
               {
                  return true;
               }
            }
         }
      }
      return false;
   }


   /* Name: canMoveUp
    * Purpose: The purpose of the method is to determine if the tiles on the
    *          board can move up.
    * Paramaters: No parameters
    * Return: The method returns a boolean. If it returns true that means the
    *         move is valid and false if the move cannot be executed.
    */ 
   private boolean canMoveUp()
   {  
      //Loops through the columns.
      for (int i = 0; i < this.GRID_SIZE; i++)
      { 
         ArrayList<Integer> checkArray = new ArrayList<Integer>();
         //Loops through all the rows.
         for (int j = 0; j< this.GRID_SIZE; j++)
         {
            int value = this.grid[j][i];
            checkArray.add(j,value);
         }
         //Looops through the arraylist
         for (int k = checkArray.size() - 1; k > 0; k--)
         {
            //Makes sure that the column contains a tile
            if (checkArray.get(k) != 0)
            {
               //Checks to see if their the tile above is the same or zero
               if (checkArray.get(k) == checkArray.get(k-1) ||
                     checkArray.get(k-1) == 0)
               {
                  return true;
               }
            }
         }
      }
      return false;
   }

   /* Name: canMoveDown
    * Purpose: The purpose of the method is to determine if the tiles on the
    *          board can move down.
    * Paramaters: No parameters
    * Return: The method returns a boolean. If it returns true that means the
    *         move is valid and false if the move cannot be executed.
    */ 
   private boolean canMoveDown()
   {  
      //Loops through the columns.
      for (int i = 0; i < this.GRID_SIZE; i++)
      { 
         ArrayList<Integer> checkArray = new ArrayList<Integer>();
         //Loops through all the rows.
         for (int j = 0; j< this.GRID_SIZE; j++)
         {
            int value = this.grid[j][i];
            checkArray.add(j,value);
         }
         //Loops through the arraylist
         for (int k = 0; k < checkArray.size() - 1; k++)
         {
            //Makes sure that the column contains a tile
            if (checkArray.get(k) != 0)
            {
               //Checks to see if the tile below is the same or zero
               if (checkArray.get(k) == checkArray.get(k+1) ||
                     checkArray.get(k+1) == 0)
               {
                  return true;
               }
            }
         }
      }
      return false;
   }

   /* EXTRA CREDIT "UNDO"
    * Name: copyBoard
    * Purpose: The purpose of this method is to copy the movement of the board.
    *          This method stores a move that has already been completed.
    * Parameters: The method takes in an int to determine which is the last move
    *             to have occured and in which 2d array to save that move into.
    * Return: Returns void.
    */ 
   public void copyBoard()
   {
      //Initializes the copy 2d array
      int [][] copy  = new int[this.GRID_SIZE][this.GRID_SIZE];
      this.previousScore = this.score;
      for (int i = 0; i < this.GRID_SIZE; i++)
      {
         for (int j = 0; j < this.GRID_SIZE; j++)
         {
            copy[i][j] = grid[i][j];
         }
      }
      //Stores the copy array an the previous score.
      if (this.lastMove%2 == 0)
      {
         this.previousScore = this.score;
         this.undo1 = copy;
         this.lastMove++;
      }
      else if (this.lastMove%2 == 1)
      {
         this.previousScore2 = this.score;
         this.undo2 = copy;
         this.lastMove++;
      }
   }

   /* EXTRA CREDIT "UNDO"
    * Name: undoTheBoard
    * Purpose: The purpose of this board is to undo the last action taken. This
    *          is done by loading a copied array that had stored last board's
    *          values.
    * Parameters: The method takes in an int to determine which board has the
    *             last move stored within it.
    * Return: Returns void.
    */ 

   public void Undo()
   {
      for (int i = 0; i < this.GRID_SIZE; i++)
      {
         for (int j = 0; j < this.GRID_SIZE; j++)
         { 
            //Copies the values of the last move into the board
            //Copies the previous score too.
            if (this.lastMove%2 == 0)
            {
               this.grid[i][j] = this.undo2[i][j];
               this.score = this.previousScore2;
               this.lastMove++;
            }
            else if (this.lastMove%2 == 1)
            {
               this.grid[i][j] = this.undo1[i][j];
               this.score = this.previousScore;
               this.lastMove++;
            }
         }
      }
   }

   // Return the reference to the 2048 Grid
   public int[][] getGrid()
   {
      return grid;
   }

   // Return the score
   public int getScore()
   {
      return score;
   }

   @Override
      public String toString()
      {
         StringBuilder outputString = new StringBuilder();
         outputString.append(String.format("Score: %d\n", score));
         for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
               outputString.append(grid[row][column] == 0 ? "    -" :
                     String.format("%5d", grid[row][column]));

            outputString.append("\n");
         }
         return outputString.toString();
      }
}
