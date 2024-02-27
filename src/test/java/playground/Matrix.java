package playground;

import javafx.util.Pair;
import org.assertj.core.data.Index;
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

    /**
     * Spiral Matrix
     * Given an m x n matrix, return all elements of the matrix in spiral order.
     * <p>
     * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
     * Output: [1,2,3,6,9,8,7,4,5]
     * <p>
     * Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
     * Output: [1,2,3,4,8,12,11,10,9,5,6,7]
     * <p>
     * https://leetcode.com/problems/spiral-matrix/description/
     */
    @Test
    void testSpiralOrder() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        assertThat(spiralOrder(matrix)).containsExactly(1, 2, 3, 6, 9, 8, 7, 4, 5);
    }

    /**
     * Use two pointers(rowPtr & colPtr) to iterate one row and column at one time. We use rowNums and colNums to keep track of
     * the number of rows/cols not visited yet, and use them to decide how far each ptr can move. Also use an offset var to
     * control the two ptr direction of right/down or left/up and next position. We decrement the rowNums or colNums after
     * finish visiting one row or column, so we reach the end when either of them becomes 0.
     * <p>
     * Time complexity: O(M⋅N)
     * Space complexity: O(1)
     */
    List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        // The following two var denotes how many rows/columns not visited yet. This controls the max steps the ptr can
        // move along the column or row
        int rowNums = matrix.length;
        int colNums = matrix[0].length;
        int offset = 1; // added to the ptr to determine the next position in the matrix. Start at right/down direction
        int rowPtr = 0;
        int colPtr = -1; // init to -1 so the total number of steps colPtr can move will be equal to colNums(cuz it iterates the first row first)
        while (rowNums * colNums > 0) {
            for (int j = 1; j <= colNums; j++) {// iterate over one row. The loop only control how many steps ptr will go
                colPtr += offset; // Move the column ptr to next cell
                result.add(matrix[rowPtr][colPtr]);
            }
            --rowNums; // Decrement remaining number of rows
            for (int i = 1; i <= rowNums; i++) {// iterate over one column.
                rowPtr += offset; // Move the row ptr to next cell
                result.add(matrix[rowPtr][colPtr]);
            }
            --colNums; // Decrement remaining number of columns
            offset *= -1; // Flip the ptr move direction i.e. right/down <==> left/up
        }
        return result;
    }

    /**
     * Diagonal Traverse
     * Given an m x n matrix mat, return an array of all the elements of the array in
     * a diagonal order.
     * <p>
     * Input: mat = [[1,2,3],[4,5,6],[7,8,9]]
     * Output: [1,2,4,7,5,3,6,8,9]
     * Example 2:
     * <p>
     * Input: mat = [[1,2],[3,4]]
     * Output: [1,2,3,4]
     * <p>
     * https://leetcode.com/problems/diagonal-traverse/description/
     */
    @Test
    void testFindDiagonalOrderI() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        assertThat(findDiagonalOrder(matrix)).containsExactly(1, 2, 4, 7, 5, 3, 6, 8, 9);
    }

    /**
     * Put Pair(0, 0) in the queue and traversal matrix in level BFS. For each level, first remove
     * and add the cell value to levelList from the queue. If col == 0 and row+1 isn't out bound,
     * put (row+1, col) in the queue. If col+1 isn't out bound, put (row, col+1) in the queue.
     * After each level loop ends, if it is even level, we need to reverse the levelList and add
     * to the final ans list
     * <p>
     * * This is actually the extension of the Diagonal Traverse II problem, do that first before
     * this
     * <p>
     * Observation:
     * Consider the 2D list is a graph and each cell is [i, j]
     * -          [0,0]
     * -      [1,0]  [0,1]
     * -   [2,0]  [1,1]  [0,2]
     * -[3,0] [2,1] [1,2]  [0,3]
     * <p>
     * We can see except for the first column, i.e. j:0, at each row, we can generate the cell
     * from every cell [i,j] at its previous row as [i, j+1]. As for the cell at the first column,
     * it will be [i+1, j].
     * <p>
     * Therefore, we just need to use this rule to put each correspond [i, j] from left to right
     * in the queue and perform BFS to visit each of them.
     * <p>
     * However, for the even-level, we want to visit them from left to right, so we need to do
     * BFS level traversal and reverse the level visited list before adding them to the result
     * <p>
     * Time complexity: O(n) + O(n) = O(n), n is the total cells in the matrix
     * For all even level, we have additional 2 + 4 + ... + sqrt(n), for reversing list ops
     * which will sum up as sqrt(n)/2 * (2 + sqrt(n))/2 = O(n)
     * Space complexity: O(sqrt(n))
     */
    int[] findDiagonalOrder(int[][] mat) {
        List<Integer> ans = new ArrayList<>();
        Deque<Pair<Integer, Integer>> queue = new ArrayDeque<>();
        queue.offer(new Pair<>(0, 0));
        int level = 0;
        while (!queue.isEmpty()) {
            int qSize = queue.size();
            level++;
            List<Integer> levelNums = new ArrayList<>();
            for (int i = 0; i < qSize; i++) {
                Pair<Integer, Integer> cell = queue.poll();
                Integer row = cell.getKey();
                Integer col = cell.getValue();
                levelNums.add(mat[row][col]);
                // Only for the first column, we put the next level cell at the left (row+1, col)
                if (col == 0 && row + 1 < mat.length) {
                    queue.offer(new Pair<>(row + 1, col));
                }
                if (col + 1 < mat[0].length) {
                    queue.offer(new Pair<>(row, col + 1));
                }
            }
            if (level % 2 == 0) {
                // even level, we want to visit from right to left, hence, reverse the level list
                Collections.reverse(levelNums);
            }
            ans.addAll(levelNums);
        }
        return ans.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Diagonal Traverse II
     * Given a 2D integer array nums, return all elements of nums in diagonal order
     * as shown in the below images.
     * <p>
     * Input: nums = [[1,2,3],[4,5,6],[7,8,9]]
     * Output: [1,4,2,7,5,3,8,6,9]
     * <p>
     * Input: nums = [[1,2,3,4,5],[6,7],[8],[9,10,11],[12,13,14,15,16]]
     * Output: [1,6,2,8,7,3,9,4,12,10,5,13,11,14,15,16]
     * https://leetcode.com/problems/diagonal-traverse-ii/description/
     */
    @Test
    void testFindDiagonalOrderII() {
        List<List<Integer>> matrix = List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7, 8, 9));
        assertThat(findDiagonalOrder(matrix)).containsExactly(1, 4, 2, 7, 5, 3, 8, 6, 9);
    }

    /**
     * Put Pair(0, 0) in the queue and traversal 2D list in BFS. Remove and visit the pair from
     * the queue. If col == 0 and row+1 isn't out bound, put (row+1, col) in the queue. If col+1
     * isn't out bound, put (row, col+1) in the queue.
     * <p>
     * Observation:
     * Consider the 2D list as a graph and each cell is [i, j]
     * -          [0,0]
     * -      [1,0]  [0,1]
     * -   [2,0]  [1,1]  [0,2]
     * -[3,0] [2,1] [1,2]  [0,3]
     * <p>
     * We can see except for the first column, i.e. j:0, at each row, we can generate the cell
     * from every cell [i,j] at its previous row as [i, j+1]. As for the cell at the first column,
     * it will be [i+1, j].
     * <p>
     * Therefore, we just need to use this rule to put each correspond [i, j] from left to right
     * in the queue and perform BFS to visit each of them.
     * <p>
     * Time complexity: O(n)
     * Space complexity: O(sqrt(n)), the max number of the diagonal. Or the height of the tree, log n
     */
    int[] findDiagonalOrder(List<List<Integer>> nums) {
        List<Integer> ans = new ArrayList<>();
        Deque<Pair<Integer, Integer>> queue = new ArrayDeque<>();
        queue.offer(new Pair<>(0, 0));
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> cell = queue.poll();
            Integer row = cell.getKey();
            Integer col = cell.getValue();
            ans.add(nums.get(row).get(col));
            // Only for the first column, we put the next level cell at the left (row+1, col)
            if (col == 0 && row + 1 < nums.size()) {
                queue.offer(new Pair<>(row + 1, col));
            }
            // each row may have different size. It is not a complete grid, so column bound depend on current row
            if (col + 1 < nums.get(row).size()) {
                queue.offer(new Pair<>(row, col + 1));
            }
        }
        return ans.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Shortest Path in a Grid with Obstacles Elimination
     * You are given an m x n integer matrix grid where each cell is either 0 (empty) or 1 (obstacle).
     * You can move up, down, left, or right from and to an empty cell in one step.
     * <p>
     * Return the minimum number of steps to walk from the upper left corner (0, 0) to the lower right
     * corner (m - 1, n - 1) given that you can eliminate at most k obstacles. If it is not possible to
     * find such walk return -1.
     * <p>
     * Input: grid = [[0,0,0],[1,1,0],[0,0,0],[0,1,1],[0,0,0]], k = 1
     * Output: 6
     * Explanation:
     * The shortest path without eliminating any obstacle is 10.
     * The shortest path with one obstacle elimination at position (3,2) is 6. Such path is (0,0) ->
     * (0,1) -> (0,2) -> (1,2) -> (2,2) -> (3,2) -> (4,2).
     * <p>
     * Input: grid = [[0,1,1],[1,1,1],[1,0,0]], k = 1
     * Output: -1
     * Explanation: We need to eliminate at least two obstacles to find such a walk.
     * <p>
     * https://leetcode.com/problems/shortest-path-in-a-grid-with-obstacles-elimination/description/
     */
    @Test
    void testShortestPath() {
        int[][] matrix = {
                {0, 0, 0},
                {1, 1, 0},
                {0, 0, 0},
                {0, 1, 1},
                {0, 0, 0},
        };
        assertThat(shortestPath(matrix, 1)).isEqualTo(6);
    }

    /**
     * Define a StepState class to store the row, col, steps, and obstaclesRemoved. And a
     * visited int[][] grid to store the obstaclesRemoved value from previous visit of each cell.
     * (init to INT_MAX). Then start to create StepState from each cell and perform BFS.
     * If we reach the target cell, return the currentStep's step value. Otherwise, explore
     * 4 adjacent cells, if it is in-bound and the obstaclesRemoved of the new step <= k
     * and < visited[x][y], add this step to the queue.
     * <p>
     * Observation:
     * Two key differences from the normal BFS in the grid
     * 1. A cell [x][y] may need to be visited again cuz coming from a different path may
     * yield a better path to the target
     * 2. We should only consider a new step if it can make a better path, i.e. encounter
     * no or fewer obstacles
     * <p>
     * Algo:
     * 1. StepState contains the row and col of the visited cell and the steps accumulated from
     * the first step originating this path, and this is also the answer we will return when
     * we reach the step reaches the target cell. Finally, we also need to track the obstacles
     * we have encountered/removed, so we can know if the next step is valid.
     * <p>
     * 2. From the observation described above, we need a visited int grid for two purpose
     * -	1 Indicate if the given cell was visited.
     * -	2 We store the obstaclesRemoved value of the previous step when it was visited,
     * -	  so when we have chance to visit the same cell again, we can compare the
     * -	  obstaclesRemoved of the current step with it to decide if we should consider
     * -	  this step (Greedy strategy)
     * <p>
     * 3. We start to perform BFS, when we reach the target cell, return the step value of
     * the current step. Otherwise, explore 4 adjacent cells.
     * However, we consider the new step ONLY if
     * -	1. The number of obstacles it removed so far is less than or equal to k
     * -   2. The step to this cell has more optimal result, i.e. less obstacle removed, or
     * -	   this cell is not visited yet, i.e. visited[nextX][nextY] == Integer.MAX_VALUE
     * <p>
     * <p>
     * Let N be the number of cells in the grid, and K be the quota to eliminate obstacles.
     * <p>
     * Time Complexity: O(N⋅K)
     * We conduct a BFS traversal in the grid. In the worst case, we will visit each cell
     * in the grid. And for each cell, at most, it will be visited K times, with different
     * quotas of obstacle elimination.
     * Thus, the overall time complexity of the algorithm is O(N⋅K).
     * <p>
     * Space Complexity: O(N⋅K)
     * Queue and the visited grid
     */
    int shortestPath(int[][] grid, int k) {
        class StepState {
            final int row;
            final int col;
            // Keep track of the accumulated steps along this path so far. A new StepState's steps should
            // be the previous steps + 1
            final int steps;
            // Keep track of the total number of obstacles it encountered/removed along this path so far
            final int obstaclesRemoved;

            public StepState(int row, int col, int steps, int obstaclesRemoved) {
                this.row = row;
                this.col = col;
                this.steps = steps;
                this.obstaclesRemoved = obstaclesRemoved;
            }
        }
        int m = grid.length;
        int n = grid[0].length;
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        // The visited grid serves two purposes
        // 1. Indicate if the given cell was visited.
        // 2. We store the obstaclesRemoved value of the previous step when it was visited, so when
        // we have chance to visit the same cell again, we can compare the obstaclesRemoved of the current step with it
        // to decide if we should consider this step (Greedy strategy)
        int[][] visited = new int[m][n];
        for (int[] i : visited) {
            Arrays.fill(i, Integer.MAX_VALUE); // Default to INT_MAX, so unvisited cell will always be considered
        }
        Deque<StepState> queue = new ArrayDeque<>();
        queue.offer(new StepState(0, 0, 0, 0));
        visited[0][0] = 0;

        while (!queue.isEmpty()) {
            StepState currentStep = queue.poll();
            if (currentStep.row == m - 1 && currentStep.col == n - 1) {
                // we reach the target. Cuz we perform BFS, it guarantees the shorted path will be found first.
                return currentStep.steps;
            }
            for (int[] dir : dirs) {
                int nextX = dir[0] + currentStep.row;
                int nextY = dir[1] + currentStep.col;
                if (nextX < 0 || nextX == m || nextY < 0 || nextY == n)
                    continue; // out of bound, so skip it
                int nextObstacleRemoved = currentStep.obstaclesRemoved + grid[nextX][nextY]; // obstacle in the grid[][] is 1
                if (nextObstacleRemoved <= k && nextObstacleRemoved < visited[nextX][nextY]) {
                    // Consider this step ONLY if
                    // 1. The number of obstacles it removed so far is less than or equal to k
                    // 2. The step to this cell has more optimal result, i.e. less obstacle removed, or this cell is not
                    // visited yet, i.e. visited[nextX][nextY] == Integer.MAX_VALUE
                    visited[nextX][nextY] = nextObstacleRemoved;
                    queue.offer(new StepState(nextX, nextY, currentStep.steps + 1, nextObstacleRemoved));
                }
            }
        }
        return -1;
    }

    /**
     * Sparse Matrix Multiplication
     * Given two sparse matrices mat1 of size m x k and mat2 of size k x n, return the result of
     * mat1 x mat2. You may assume that multiplication is always possible.
     * <p>
     * Matrix multiplication formula:
     * Matrix A: [a b c]
     * [d e f]
     * <p>
     * Matrix B: [1 2]
     * [3 4]
     * [5 6]
     * <p>
     * Matrix C = A x B
     * [a+3b+5c  2a+4b+6c]
     * [d+3e+5f  2d+4e+6f]
     * <p>
     * Product of matrix A and B has the number of rows from A
     * and the number of columns from B, i.e. rowA x colB.
     * <p>
     * Input: mat1 = [[1,0,0],[-1,0,3]], mat2 = [[7,0,0],[0,0,0],[0,0,1]]
     * Output: [[7,0,0],[-7,0,3]]
     * <p>
     * Input: mat1 = [[0]], mat2 = [[0]]
     * Output: [[0]]
     * <p>
     * https://leetcode.com/problems/sparse-matrix-multiplication/description/
     */
    @Test
    void testMultiply() {
        int[][] matrix1 = {
                {1, 0, 0},
                {-1, 0, 3},
                {0, 0, 0}
        };
        int[][] matrix2 = {
                {7, 0, 0},
                {0, 0, 0},
                {0, 0, 1}
        };
        assertThat(multiply(matrix1, matrix2)).contains(new int[]{7, 0, 0}, Index.atIndex(0));
        assertThat(multiply(matrix1, matrix2)).contains(new int[]{-7, 0, 3}, Index.atIndex(1));
        assertThat(multiplyBruteForce(matrix1, matrix2)).contains(new int[]{7, 0, 0}, Index.atIndex(0));
        assertThat(multiplyBruteForce(matrix1, matrix2)).contains(new int[]{-7, 0, 3}, Index.atIndex(1));
        assertThat(multiplyOpt(matrix1, matrix2)).contains(new int[]{7, 0, 0}, Index.atIndex(0));
        assertThat(multiplyOpt(matrix1, matrix2)).contains(new int[]{-7, 0, 3}, Index.atIndex(1));
    }

    /**
     * Implement the matrix multiplication rule directly.
     * Iterate each cell in the final product matrix, and apply the rule to compute the sum of
     * product of from the cells at matrix1 and matrix2.
     * <p>
     * Time complexity: O(m⋅k⋅n).
     * m, number of rows in mat1
     * n, number of columns in mat2
     * k, number of columns in mat1
     * <p>
     * Space complexity: O(1).
     */
    int[][] multiplyBruteForce(int[][] mat1, int[][] mat2) {
        // mat1 is size m × k, mat2 is size k × n, product of mat1⋅mat2 is size m × n.
        int m = mat1.length, k = mat1[0].length; // == mat2.length
        int n = mat2[0].length;
        // Product matrix has the number of rows from A and the number of columns from B, i.e. rowA x colB.
        int[][] prod = new int[m][n];
        for (int r1 = 0; r1 < m; r1++) { // iterate rows of m1
            for (int c2 = 0; c2 < n; c2++) { // iterate cols of m2
                int val = 0;
                for (int c1 = 0; c1 < k; c1++) { // iterate cols of m1, i.e. rows of m2
                    // loop each number on the col c1 at matrix 1, and times the corresponding row at col c2 at matrix 2
                    val += mat1[r1][c1] * mat2[c1][c2];
                }
                prod[r1][c2] = val;
            }
        }
        return prod;
    }

    /**
     * The key difference of this implementation is instead of loop each number on the col at matrix 1
     * for a given row, and times the corresponding row at the col at matrix 2, for this new version,
     * the idea is for a given cell [r][c] at matrix 1, we first iterate the cell [c][0...n-1] at
     * matrix 2, and compute the product and store at the product matrix. However, we only have partial
     * at this moment. We still need to iterate the rest of cell at the same row in matrix 1, so the
     * their computed will be added to the existing product to be complete. The advantage of doing this
     * is if the cell at matrix 1 is 0, there is no need to continue to iterate the matrix 2, cuz the
     * computed partial product will be 0 and won't change the final product value even if added.
     * <p>
     * For example:
     * Matrix A: [a b c]
     * [d e f]
     * <p>
     * Matrix B: [1 2]
     * [3 4]
     * [5 6]
     * <p>
     * The first row of the product matrix will be updated as the following sequence (a, b, c > 0)
     * [a⋅1  a⋅2]
     * [a⋅1 + b⋅3   a⋅2 + b⋅4]
     * [a⋅1 + b⋅3 + c⋅5   a⋅2 + b⋅4 + c⋅6]
     * <p>
     * In the case of b = 0, then the algo will skip it and won't generate b⋅3 and b⋅4
     * Time complexity: O(m⋅k⋅n).
     * m, number of rows in mat1
     * n, number of columns in mat2
     * k, number of columns in mat1
     * <p>
     * Space complexity: O(1).
     */
    int[][] multiply(int[][] mat1, int[][] mat2) {
        // mat1 is size m × k, mat2 is size k × n, product of mat1⋅mat2 is size m × n.
        int m = mat1.length, k = mat1[0].length; // == mat2.length
        int n = mat2[0].length;
        // Product matrix has the number of rows from A and the number of columns from B, i.e. rowA x colB.
        int[][] prod = new int[m][n];

        for (int r1 = 0; r1 < m; r1++) { // iterate rows of m1
            for (int c1 = 0; c1 < k; c1++) { // iterate cols of m1
                if (mat1[r1][c1] != 0) {
                    for (int c2 = 0; c2 < n; c2++) { // iterate cols of m2
                        if (mat2[c1][c2] != 0)
                            prod[r1][c2] += mat1[r1][c1] * mat2[c1][c2];
                    }
                }
            }
        }
        return prod;
    }

    /**
     * This approach is for the follow-up question:
     * what if the matrix is too big to store in the memory, but there are only a few non-zero elements.
     * Use List of Lists to store the non-zero cell data for the matrix. The outer list is rows of
     * the matrix, and the inner list is the pairs of (non-zero cell value, columnIndex) at the
     * given row in the matrix. We use the same algo as the Approach 1, but two inner loop is over
     * the matrix 1 and matrix 2 map.
     * For each row in mat1, iterate over all elements from the map1 from that row. These represent
     * the non-zero elements. Then iterate the map2 for the given row(the col val of the map1 element)
     * and compute the product and add to the ans[mat1Row][mat2Col]
     * <p>
     * Time complexity: O(m⋅k⋅n).
     * m, number of rows in mat1
     * n, number of columns in mat2
     * k, number of columns in mat1
     * <p>
     * Space complexity: O(m⋅k+k⋅n).
     */
    int[][] multiplyOpt(int[][] mat1, int[][] mat2) {
        int m = mat1.length;
        int k = mat1[0].length;
        int n = mat2[0].length;

        // Store the non-zero values of each matrix.
        var A = compressMatrix(mat1);
        var B = compressMatrix(mat2);

        int[][] ans = new int[m][n];

        for (int mat1Row = 0; mat1Row < m; ++mat1Row) {
            // Iterate on all current 'row' non-zero elements of mat1.
            for (Pair<Integer, Integer> mat1Element : A.get(mat1Row)) {
                int element1 = mat1Element.getKey();
                int mat1Col = mat1Element.getValue();

                // Multiply and add all non-zero elements of mat2
                // where the row is equal to col of current element of mat1.
                for (Pair<Integer, Integer> mat2Element : B.get(mat1Col)) {
                    int element2 = mat2Element.getKey();
                    int mat2Col = mat2Element.getValue();
                    ans[mat1Row][mat2Col] += element1 * element2;
                }
            }
        }

        return ans;
    }

    /**
     * Turn a matrix into a List of list
     * The outer list is rows of the matrix.
     * The inner list is the pairs of (non-zero cell value, columnIndex) at the given row in the matrix
     */
    List<List<Pair<Integer, Integer>>> compressMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        List<List<Pair<Integer, Integer>>> compressedMatrix = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            List<Pair<Integer, Integer>> currRow = new ArrayList<>();
            for (int col = 0; col < cols; ++col) {
                if (matrix[row][col] != 0) {
                    // (value, columnIdx) in the list
                    currRow.add(new Pair<>(matrix[row][col], col));
                }
            }
            compressedMatrix.add(currRow);
        }
        return compressedMatrix;
    }

}
