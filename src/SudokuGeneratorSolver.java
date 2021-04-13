import java.util.*;

class Sudoku {
    private int solutionCounter;
    private Integer[][] puzzle;                 //holds puzzle for this class
    private Integer[][] solution;               //holds solution for generated puzzle only
    private List<Integer[][]> solutionList;     //holds list of possible solution for custom input puzzle

    //Defautl constructor generates new puzzle as soon as object is created
    public Sudoku(){
        puzzle = new Integer[9][9];
        solution = new Integer[9][9];
        solutionList = new ArrayList<>();
    }

    //Overloaded constructor takes in a custom puzzle
    //Puzzle is solved as soon as the object is created
    public Sudoku(Integer[][] puzzle){
        this.puzzle = puzzle;
        solution = new Integer[9][9];
        solutionList = new ArrayList<>();
    }

    //Solves input puzzle from overloaded constructor
    public boolean solve(){
        return solvePuzzle(this.puzzle, true);
    }

    //Generates new puzzle. Empty grid is initialized to 0.
    //Empty grid is filled with numbers that satisfies requirements of sudoku puzzle
    //Numbers are removed from filled grid while maintain requirements of sudoku puzzle
    public void generateNewPuzzle(){
        clearGrid(this.puzzle);
        generateFilledPuzzle(this.puzzle);
        solution = copyGrid(this.puzzle);
        removeNumbersFromFilledGrid(this.puzzle);
    }

    //Check if grid is completely filled and a solution is found
    private boolean isGridFilled(Integer[][] grid){
        for(int row = 0 ; row < 9 ; ++row){
            for(int col = 0 ; col < 9 ; ++col){
                if (grid[row][col] == 0)
                        return false;
            }
        }
        return true;
    }

    //Returns a copy of input 2D array
    private Integer[][] copyGrid(Integer[][] grid){
        Integer[][] copy = new Integer[9][9];
        for(int row = 0 ; row < 9 ; ++row){
            for(int col = 0 ; col < 9 ; ++col){
                copy[row][col] = grid[row][col];
            }
        }
        return copy;
    }

    //Clear grid to all 0's
    private void clearGrid(Integer[][] grid){
        for(int row = 0 ; row < 9 ; ++row){
            for(int col = 0 ; col < 9 ; ++col){
                grid[row][col] = 0;
            }
        }
    }

    //Filled an empty grid with numbers that satisfies requirements of sudoku puzzle
    private boolean generateFilledPuzzle(Integer[][] grid) {

        //Takes an array from 1-9, convert it into a lis to use the shuffle() in Collections and convert back to array
        Integer[] numList = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        List<Integer> intList = Arrays.asList(numList);
        Collections.shuffle(intList);
        intList.toArray(numList);

        for (int i = 0; i < 81; ++i) {
            int row = i / 9;
            int col = i % 9;

            //fill cell if not empty (0)
            if (grid[row][col] == 0) {
                //try all number from randomized number list
                for (int num : numList) {
                    //put in current number in the cell if it's valid, else reset to 0
                    if (checkValid(grid, row, col, num)) {
                        grid[row][col] = num;
                        if(isGridFilled(grid)) {
                            this.solutionList.add(copyGrid(grid));
                            return true;      //start generating puzzle if all cells are filled
                        } else if (generateFilledPuzzle(grid)) {
                            return true;     //start on next cell
                        } else{
                            grid[row][col] = 0;     //reset cell value to 0, backtrack and try another path
                        }
                    }
                }
                return false;   //not of the numbers will work for this cell, return false to backtrack
            }
        }
        return true;
    }

    //Numbers are removed from filled grid while maintain requirements of sudoku puzzle
    private void removeNumbersFromFilledGrid(Integer[][] grid){
        LinkedList<Integer> nonEmptySquares = getNonEmptySquares(grid);
        int nonEmptySquaresCount = nonEmptySquares.size();
        int clues = 18;

        while(!nonEmptySquares.isEmpty() && nonEmptySquaresCount >= clues){
           // System.out.println(nonEmptySquares.size());
            //System.out.println(attempts);
            //get a random square row and col
            int index = nonEmptySquares.poll();
            int row = index / 9;
            int col = index % 9;

            //System.out.println("ROW: " + row + " COL: " + col);

            nonEmptySquaresCount--;

            //backup current square value before making it empty
            int removedSquareValue = grid[row][col];
            grid[row][col] = 0;

            //make backup copy of the new grid to solve and verify
            Integer[][] copy = copyGrid(grid);

            //reset number of unique solutions and solve the puzzle
            solutionCounter = 0;
            solvePuzzle(copy,false);

            //System.out.println("Number of solutions: " + solutionCounter);

            //if more than 1 solution, put the last removed number back into the grid
            if(solutionCounter != 1){
                grid[row][col] = removedSquareValue;
                nonEmptySquaresCount++;
            }
        }
    }

    //Solves the puzzle and counts the number of possible solutions.
    //If saveSolutions is true, all solutions found are saved into an ArrayList
    private boolean solvePuzzle(Integer[][] grid, boolean saveSolutions){
        for (int i = 0; i < 81; ++i) {
            int row = i / 9;
            int col = i % 9;

            if (grid[row][col] == 0) {
                for (int num = 1; num < 10; ++num) {
                    if (checkValid(grid, row, col, num)) {
                        grid[row][col] = num;
                        if (isGridFilled(grid)) {
                            this.solutionCounter++;
                            if(saveSolutions) this.solutionList.add(copyGrid(grid));
                            break;
                        } else if (solvePuzzle(grid, saveSolutions)) {
                            return true;     //start on next cell
                        }
                    }
                }
                grid[row][col] = 0;     //reset cell value to 0, backtrack and try another path
                return false;
            }
        }
        return true;
    }

    //Check if a input number in the a cell of a grid satisfies requirements of sudoku puzzle
    //Each row and column should not already contain the input number
    //Current 3X3 subsection of the grid can't already contain the input number
    private boolean checkValid(Integer[][] grid, int row, int col, int num){
        boolean valid = true;

        for(int i = 0 ; i < 9 ; ++i){
            if(grid[i][col] == num)
                valid = false;
        }

        for(int i = 0 ; i < 9 ; ++i){
            if(grid[row][i] == num)
                valid = false;
        }

        int rowSection = row / 3;
        int colSection = col / 3;

        for(int i = 0 ; i < 3 ; ++i){
            for(int j = 0 ; j < 3 ; ++j){
                if(grid[rowSection*3 + i][colSection*3 + j] == num)
                    valid = false;
            }
        }
        return valid;
    }

    //Print all possible solution(s) from the solution list
    public void printSolutions(){
        for(Integer[][] solution : solutionList)
            printPuzzle(solution);
    }

    //Print puzzle of this class
    public void printCurrentPuzzle(){
        printPuzzle(this.puzzle);
    }

    //Print input 9X9 sudoku puzzle
    private void printPuzzle(Integer[][] grid){
        String gridSepX = "|--------------------------------|";
        String gridSepY = "|----------+----------+----------|";

        System.out.println(gridSepX);
        for(int row = 0 ; row < grid[0].length ; ++row){
            for(int col = 0 ; col < grid.length ; ++col){
                if(col == 0 && (row == 3 || row ==6))
                    System.out.println(gridSepY);
                if(col == 0 || col == 3 || col == 6)
                    System.out.print("| ");
                System.out.print(" " + grid[row][col] + " ");
                if(col == 8)
                    System.out.println("|");
            }
        }
        System.out.println(gridSepX);
    }

    //Return number of possible solutions for this puzzle
    public int getSolutionCounter(){
        return this.solutionCounter;
    }

    //Get all non-empty squares and shuffle
    //For completely filled grid, this will shuffled list from 0 -> 81
    private LinkedList<Integer> getNonEmptySquares(Integer[][] grid){
        LinkedList<Integer> nonEmptySquares = new LinkedList<>();   //use LinkedList for poll()

        for (int i = 0; i < 81; ++i) {
            int row = i / 9;
            int col = i % 9;
            if (grid[row][col] != 0)
                nonEmptySquares.add(i);
        }

        Collections.shuffle(nonEmptySquares);
        return nonEmptySquares;
    }
}

public class SudokuGeneratorSolver {

    //Test puzzle with only 1 solution
    public static Integer[][] puzzle1 = {
            { 8, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 3, 6, 0, 0, 0, 0, 0 },
            { 0, 7, 0, 0, 9, 0, 2, 0, 0 },
            { 0, 5, 0, 0, 0, 7, 0, 0, 0 },
            { 0, 0, 0, 0, 4, 5, 7, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 3, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 6, 8 },
            { 0, 0, 8, 5, 0, 0, 0, 1, 0 },
            { 0, 9, 0, 0, 0, 0, 4, 0, 0 }
    };

    //Test puzzle with 2 solutions
    private static Integer[][] puzzle2 = {
            { 2, 9, 5, 7, 4, 3, 8, 6, 1 },
            { 4, 3, 1, 8, 6, 5, 9, 0, 0 },
            { 8, 7, 6, 1, 9, 2, 5, 4, 3 },
            { 3, 8, 7, 4, 5, 9, 2, 1, 6 },
            { 6, 1, 2, 3, 8, 7, 4, 9, 5 },
            { 5, 4, 9, 2, 1, 6, 7, 3, 8 },
            { 7, 6, 3, 5, 3, 4, 1, 8, 9 },
            { 9, 2, 8, 6, 7, 1, 3, 5, 4 },
            { 1, 5, 4, 9, 3, 8, 6, 0, 0 }
    };

    //Test puzzle that is not a valid puzzle
    private static Integer[][] puzzle3 = {
            { 2, 9, 5, 7, 4, 3, 8, 6, 1 },
            { 4, 3, 1, 8, 6, 5, 9, 7, 7 },
            { 8, 7, 6, 1, 9, 2, 5, 4, 3 },
            { 3, 8, 7, 4, 5, 9, 2, 1, 6 },
            { 6, 1, 2, 3, 8, 7, 4, 9, 5 },
            { 5, 4, 9, 2, 1, 6, 7, 3, 8 },
            { 7, 6, 3, 5, 3, 4, 1, 8, 9 },
            { 9, 2, 8, 6, 7, 1, 3, 5, 4 },
            { 1, 5, 4, 9, 3, 8, 6, 0, 0 }
    };

    public static void main(String[] args){
        System.out.println("Generating new sudoku puzzle. Please wait.");
        Sudoku newPuzzle = new Sudoku();
        newPuzzle.generateNewPuzzle();
        newPuzzle.printCurrentPuzzle();

        System.out.println("Solution for new puzzle: ");
        newPuzzle.printSolutions();

        System.out.println("Input custom sudoku puzzle #1: ");
        Sudoku customPuzzle1 = new Sudoku(puzzle1);
        customPuzzle1.solve();
        customPuzzle1.printCurrentPuzzle();

        System.out.println("There are " + customPuzzle1.getSolutionCounter() + " Solution(s) for this puzzle: ");
        customPuzzle1.printSolutions();

        System.out.println("Input custom sudoku puzzle #2: ");
        Sudoku customPuzzle2 = new Sudoku(puzzle2);
        customPuzzle2.solve();
        customPuzzle2.printCurrentPuzzle();

        System.out.println("There are " + customPuzzle2.getSolutionCounter() + " Solution(s) for this puzzle: ");
        customPuzzle2.printSolutions();

        System.out.println("Input custom sudoku puzzle #3: ");
        Sudoku customPuzzle3 = new Sudoku(puzzle3);
        customPuzzle3.printCurrentPuzzle();

        if(customPuzzle3.solve()) {
            System.out.println("There are " + customPuzzle3.getSolutionCounter() + " Solution(s) for this puzzle: ");
            customPuzzle3.printSolutions();
        }else{
            System.out.println("Invalid sudoku puzzle.");
        }

    }
}

