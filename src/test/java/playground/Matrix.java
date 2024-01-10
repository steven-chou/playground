package playground;

import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO Matrix problem essential tips:
 *   - Matrix Transpose: Flips a matrix over its diagonal; that is, it switches the row and column indices of the matrix
 *     Check the transpose method in problem "Rotate Image". The inner loop pointer init state is the key
 */
public class Matrix {

    /**
     * Valid Sudoku
     * Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated
     * according to the following rules:
     * <p>
     * Each row must contain the digits 1-9 without repetition.
     * Each column must contain the digits 1-9 without repetition.
     * Each of the nine 3 x 3 sub-boxes of the grid must contain the digits 1-9 without repetition.
     * <p>
     * Note:
     * <p>
     * A Sudoku board (partially filled) could be valid but is not necessarily solvable.
     * Only the filled cells need to be validated according to the mentioned rules.
     * https://leetcode.com/problems/valid-sudoku/solution/
     * EPI 5.17
     */
    @Test
    void testSudoku() {
        char[][] board = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };

        Assertions.assertTrue(isValidSudoku(board));
        char[][] boardTwo = {
                {'8', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };
        Assertions.assertFalse(isValidSudoku(boardTwo));
    }

    /**
     * Iterate 2D board array and use 3 Map with Set, rowIdxToCharSet, colIdxToCharSet and boxIdxToCharSet
     * to record visited number and check if the current cell value is duplicate.
     * <p>
     * Observation:
     * 1. A valid sudoku board should satisfy three conditions: (1) each row, (2) each column, and (3)
     * each box has no duplicate numbers.
     * <p>
     * 2. We can use three sets to check duplicate char for each row, column and 3x3 boxes.
     * <p>
     * Algo:
     * 1. Create 3 maps. rowIdxToCharSet, colIdxToCharSet, and boxIdxToCharSet
     * For each 3x3 box. Each cell in the board is assigned a box ID, which is Pair(rowIndex/3, columnIndex/3)
     * So all cell in the same 3x3 box have the same box ID
     * <p>
     * 2. Iterate the 2D board array,
     * -  Check if the cell value exists in these 3 sets. If so, the board is invalid, return false.
     * -  Add the cell value to all 3 sets.
     * <p>
     * Time Complexity: O(n^2)
     * Space Complexity: O(n^2): 3 x n (max entries of Map) x n (max size of each set)
     */
    boolean isValidSudoku(char[][] board) {
        Map<Integer, Set<Character>> rowIdxToCharSet = new HashMap<>(); // Set for each row
        Map<Integer, Set<Character>> colIdxToCharSet = new HashMap<>(); // Set for each column
        // Set for each 3x3 box. Each cell in the board is assigned a box ID, which is Pair(rowIndex/3, columnIndex/3).
        // So all cell in the same 3x3 box have the same box ID
        Map<Pair<Integer, Integer>, Set<Character>> boxIdxToCharSet = new HashMap<>();
        // Init the Set in the map
        for (int i = 0; i < 9; i++) {
            rowIdxToCharSet.put(i, new HashSet<>());
            colIdxToCharSet.put(i, new HashSet<>());
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                boxIdxToCharSet.put(new Pair<>(i, j), new HashSet<>());
        }
        // Iterate over the board
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                char val = board[r][c];
                if (val == '.') // Skip empty cell, i.e. '.'
                    continue;
                if (rowIdxToCharSet.get(r).contains(val)
                        || colIdxToCharSet.get(c).contains(val)
                        || boxIdxToCharSet.get(new Pair<>(r / 3, c / 3)).contains(val))
                    // board is invalid if the cell value is found in any one of the three sets
                    return false;
                // add current cell value to three sets
                rowIdxToCharSet.get(r).add(val);
                colIdxToCharSet.get(c).add(val);
                boxIdxToCharSet.get(new Pair<>(r / 3, c / 3)).add(val);
            }
        }
        return true;
    }


    /**
     * Rotate Image/Matrix
     * You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees
     * (clockwise).
     * <p>
     * You have to rotate the image in-place, which means you have to modify the input 2D matrix
     * directly. DO NOT allocate another 2D matrix and do the rotation.
     * <p>
     * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
     * Output: [[7,4,1],[8,5,2],[9,6,3]]
     * https://leetcode.com/problems/rotate-image/solution/
     * EPI 5.19
     */
    @Test
    void testRotateMatrix() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        int[][] answer = {
                {7, 4, 1},
                {8, 5, 2},
                {9, 6, 3}
        };
        rotate(matrix);
        assertThat(Arrays.deepEquals(answer, matrix)).isTrue();
    }

    /**
     * First flip the matrix around the main diagonal(Matrix transpose), then reverse each row
     * from left to right.
     * These operations are called transpose and reflect in linear algebra.
     * Time Complexity: O(N). Space Complexity: O(1)
     */
    void rotate(int[][] matrix) {
        transpose(matrix);
        reflect(matrix);
    }

    // Reverse the matrix around the main diagonal
    void transpose(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            //TODO: IMPORTANT! j=i+1 cuz we want to swap w/ the other element cross the diagonal. Ptr j must start at
            // i+1, so we won't do the same swap twice.
            for (int j = i + 1; j < matrix.length; j++) {
                var tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }
    }

    // Reverse each row in the matrix from left to right
    void reflect(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length / 2; j++) {
                var tmp = matrix[i][j];
                matrix[i][j] = matrix[i][matrix.length - 1 - j];
                matrix[i][matrix.length - 1 - j] = tmp;
            }
        }
    }

    /**
     * Set Matrix Zeroes
     * Given an m x n integer matrix matrix, if an element is 0, set its entire row and column to 0's.
     * You must do it in place.
     * <p>
     * Input: matrix = [[1,1,1],[1,0,1],[1,1,1]]
     * Output: [[1,0,1],[0,0,0],[1,0,1]]
     * <p>
     * Input: matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
     * Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
     * <p>
     * https://leetcode.com/problems/set-matrix-zeroes/description/
     */
    @Test
    void testSetZeroes() {
        int[][] matrix = {
                {1, 1, 1},
                {1, 0, 1},
                {1, 1, 1}
        };
        int[][] answer = {
                {1, 0, 1},
                {0, 0, 0},
                {1, 0, 1}
        };
        setZeroes(matrix);
        assertThat(matrix).isEqualTo(answer);
        matrix = new int[][]{
                {0, 1, 2, 0},
                {3, 4, 5, 2},
                {1, 3, 1, 5}
        };
        answer = new int[][]{
                {0, 0, 0, 0},
                {0, 4, 5, 0},
                {0, 3, 1, 0}
        };
        setZeroes(matrix);
        assertThat(matrix).isEqualTo(answer);
    }


    /**
     * Iterate all cells in matrix and if the cell is zero, set [row][0] and [0][col] to 0. Set boolean
     * flag, setFirstColZero, to true when any cell at the first column is 0. Then we use the info from
     * the first row & column to iterate from [1][1] to set the cell to zero accordingly. Then set the
     * cells at the first row to 0 if [0][0] is 0. Set cells at the first column to 0 if setFirstColZero
     * is true.
     * <p>
     * We don't want to use any additional memory so we can use the first cell of every row and column
     * as a flag. This flag would determine whether a row or column has been set to zero.
     * -if cell[i][j] == 0 {
     * -    cell[i][0] = 0
     * -    cell[0][j] = 0
     * -}
     * These flags are used later to update the matrix. If the first cell of a row is set to zero this
     * means the row should be marked zero. If the first cell of a column is set to zero this means the
     * column should be marked zero. However, we use [0][0] for the info of the first row. Thus we need
     * another boolean var to indicate if the first column should be mark zero.
     * <p>
     * Algorithm
     * 1. We iterate over the matrix and we mark the first cell of a row i and first cell of a column j,
     * if the condition in the pseudo code above is satisfied. i.e. if cell[i][j] == 0.
     * <p>
     * 2. The first cell of row and column for the first row and first column is the same i.e.
     * cell[0][0]. Hence, we use an additional variable to tell us if the first column had been marked or
     * not and the cell[0][0] would be used for the first row.
     * <p>
     * 3. Now, we iterate over the matrix starting from second row and second column i.e. matrix[1][1]
     * onwards. For each cell we check if matrix[i][0] or matrix[0][j] is zero. If so, set it to zero.
     * <p>
     * 4.Check if cell[0][0] == 0, if so, we mark all cells at the first row to zero.
     * <p>
     * 5.Check if boolean flag, setFirstColZero is true, if so, make all cells at first column to zero.
     * <p>
     * Time Complexity : O(M×N)
     * Space Complexity : O(1)
     */
    void setZeroes(int[][] matrix) {
        boolean setFirstColZero = false;
        int rowNum = matrix.length, colNum = matrix[0].length;
        for (int i = 0; i < rowNum; i++) {
            // Since the first cell, i.e. matrix[0][0], for both first row and first column is the same
            // We need an additional variable for either the first row/column.
            // Here we are use setFirstColZero for the first column and matrix[0][0] for the first row.
            for (int j = 0; j < colNum; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0; // Set the first cell of the same row to zero
                    if (j == 0) // If we see zero at any cell in the 1st column, set the flag
                        setFirstColZero = true;
                    else
                        matrix[0][j] = 0; // Set the first cell of the same column to zero
                }
            }
        }
        // Iterate over the array once again(except for the first row and column) and use the info from the
        // first row and first column to update the elements.
        for (int i = 1; i < rowNum; i++) {
            for (int j = 1; j < colNum; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        // See if the first row needs to be set to zero as well
        if (matrix[0][0] == 0) {
            // First row should be set to zero
            for (int i = 0; i < colNum; i++)
                matrix[0][i] = 0;
        }
        // See if the first column needs to be set to zero as well
        if (setFirstColZero) {
            for (int i = 0; i < rowNum; i++)
                matrix[i][0] = 0;
        }
    }
}
