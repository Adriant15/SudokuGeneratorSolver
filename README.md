# Sudoku Generator and Solver

Java program can generate new sudoku puzzle or solve inputted ones.

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Sudoku Generator](#Sudoku-Generator)
* [Sudoku Solver](#Sudoku-Solver)
* [Example](#Example)

## General info
This is Java program that uses the backtracking algorithm to generate or solve a puzzle by testing each cell for a valid solution. It tries all possible solutions. The algorithm can store multiple solutions if it exists.

The three constraints/goal of a Sudoku puzzle are:

1. Each row of the 9x9 cell grid will have only one number of each kind.
2. Each column of the 9x9 cell grid will have only one number of each kind.
3. Every 3x3 subsection of the grid can only contain numbers from 1 to 9 with no duplicates.
	
## Technologies
Project is created with:

* IntelliJ IDEA 
	
## Setup
Open and run examples files in src folder. 

## Sudoku Generator

There are two steps to generating new sudoku puzzle:

* Generate a filled grid with numbers that satisfy the three constraints of Sudoku.

  * A new instance of a Sudoku class object created with default constructor will have an empty puzzle 	represented by a 9x9 Integer array. Calling the generateNewPuzzle() method will first empty the puzzle grid by filling the 9x9 array with 0's and then fill each cell with numbers using the backtrack algorithm. 

  * Backtracking algorithm tries to solve the puzzle by testing each cell for a valid solution. If 	there's no violation of all of the 3 constraints of sudoku, the algorithm moves to the next cell, fills in all potential solutions and repeats all checks. If there's a violation, then it increments the cell value. Once, the value of the cell reaches 9, and there is still violation then the algorithm moves back to the previous cell and increases the value of that cell.


  * The backtracking algorithm is a recursive algorithm. During it recursion, if a number fits in the 	current cell, it will check if the entire grid is filled. If the grid is not filled, we continue to the next cell and try to find a number between 1 to 9 that can be placed. If the grid is filled, the solution is saved. 

* Take the filled grid empty random cells while attempting to generate a puzzle with a unique solution.

  * The removeNumbersFromFilledGrid() method will attempt to randomly empty one of the 81 cells in the 9x9 array. 
  * We will run the solvePuzzle() method after each time a cell is emptied to find the number of solution the resulting sudoku puzzle will have. If the number of solution is greater than 1, it resulting puzzle will not have an unique solution so the previous values is restored. The removeNumbersFromFilledGrid() will iterate will the next randomized number in the list of 81 integers.
  * When the list of 81 integers is emptied, the while loop will stop trying to empty more cells. 
  * The maximum number of non-zero cells allowed and still have the puzzle to be solvable is 17.

## Sudoku Solver

* The Sudoku class has an overloaded constructor that takes in a 2D array.

  * A instance of Sudoku class created with a 9x9 array input will set it to the 9x9 array of the Sudoku class. If the inputted array is not 9x9, the solve() method will return false and "Invalid sudoku puzzle" will be printed to the console.
 
 * The solve() method is called to solve the inputted sudoku puzzle. In contrast to when generating the a new sudoku puzzle, the boolean parameter for solve() method will be true to save all possible solutions to an array list. 
 
  * Again, the solve() method uses backtracking algorithm mentioned above. The difference is that every time a path reaches a solution, that solution is saved to an array list. 

  * In the output, the number of possible solutions for the inputted puzzle will be printed. All the possible solutions will also be printed.

## Example

The example provided will demonstrate:

 * Generating a new sudoku puzzle
 * Solving a puzzle that a unique solution
 * Solving a puzzle that has two solutions
 * Handling a puzzle that is a 8x9 array
 
