package playground;

import javafx.util.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * TODO Tips for Graph question
 *  - List<Set<T>> or Map<Set<T>>, is usually a good way to implement Adjacency List to represent a directed/undirected graph
 *    If the nodes are continuous integer and we need to random access a node, List can be used, otherwise Map should be better
 *      Ex, graph[0], will have all the nodes which are connected (neighbour) to node 0 and so on.
 *      For undirected graph say node 0 and node are connected w/ an edge, we will have graph[0] = {1}, graph[1] = {0}
 *      For directed graph say node 0 and node are connected w/ an edge from 0 -> 1, we will have graph[0] = {1} ONLY
 *      https://www.geeksforgeeks.org/graph-and-its-representations/
 */
public class GraphQuestion {

    /**
     * Topological sort Kahn's Algorithm implementation
     * Each edge pair, [x, y], in the edges represents y --> x in the graph
     * <p>
     * V represents the number of vertices, and E represents the number of edges
     * Time complexity: O(V+E)
     * Initializing the adjacency list, i.e. graph, takes O(E) time as we go through all the edges. The in-degree array
     * take O(V) time.
     * Each queue operation takes O(1) time, and a single node will be pushed once, leading to O(V) operations for V
     * nodes. We iterate over the neighbors of each node that is popped out of the queue iterating over all the edges
     * once. Since there are total of E edges, it would take O(E) time to iterate over the edges.
     * Hence, O(E) + O(V+E) = O(V+E)
     * <p>
     * Space complexity: O(m+n)
     * The adjacency list takes O(E) space. The in-degree array takes O(V) space.
     * The queue can have no more than V elements in the worst-case scenario. It would take up O(V)
     * space in that case.
     */
    int[] FindTopologicalOrdering(int nodeTotal, int[][] edges) {
        int[] inDegree = new int[nodeTotal];
        // Graph is represented as an adjacency list.
        // For directed graph say node 0 and node are connected w/ an edge from 0 -> 1, we will have graph[0] = {1}
        // graph.get(i): Nodes having an inbound edge from the node i, e.g. i --> a, i --> b, graph[i] = {a, b}
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < nodeTotal; i++) {
            graph.add(new ArrayList<>());
        }
        // Construct the adjacent list and calculate the in-degree of each node.
        for (int[] edge : edges) {
            int from = edge[1];
            int to = edge[0];
            graph.get(from).add(to);
            ++inDegree[to];
        }
        // queue always contains the nodes with no incoming edges, i.e. zero in-degree
        Queue<Integer> zeroIngreeNodeQueue = new ArrayDeque<>();
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0)
                zeroIngreeNodeQueue.offer(i);
        }
        int[] order = new int[nodeTotal];
        int idx = 0;
        while (!zeroIngreeNodeQueue.isEmpty()) {
            // The node pop from the queue has no incoming edge, the goal of this algo is to visit such
            // kind of node, then remove it from the graph, then explore its connected neighbor nodes.
            Integer node = zeroIngreeNodeQueue.poll();
            order[idx++] = node;
            for (Integer toNode : graph.get(node)) {
                // To simulate that we remove this node from the graph, we decrement the in-degree count
                // of the node having edge from this node
                --inDegree[toNode];
                if (inDegree[toNode] == 0)
                    // If the toNode has no more incoming edge, add it to the queue
                    zeroIngreeNodeQueue.offer(toNode);
            }
        }
        if (idx != nodeTotal)
            // Some nodes in the graph are not reduced to zero in-degree, which means cycle is found
            return new int[0];
        return order;
    }

    /**
     * Find the Celebrity
     * Suppose you are at a party with n people labeled from 0 to n - 1 and among them, there may
     * exist one celebrity. The definition of a celebrity is that all the other n - 1 people know
     * the celebrity, but the celebrity does not know any of them.
     * <p>
     * Now you want to find out who the celebrity is or verify that there is not one. You are only
     * allowed to ask questions like: "Hi, A. Do you know B?" to get information about whether A
     * knows B. You need to find out the celebrity (or verify there is not one) by asking as few
     * questions as possible (in the asymptotic sense).
     * <p>
     * You are given a helper function bool knows(a, b) that tells you whether a knows b. Implement
     * a function int findCelebrity(n). There will be exactly one celebrity if they are at the party.
     * <p>
     * Return the celebrity's label if there is a celebrity at the party. If there is no celebrity, return -1.
     * <p>
     * Input: graph = [[1,1,0],[0,1,0],[1,1,1]]
     * Output: 1
     * Explanation: There are three persons labeled with 0, 1 and 2. graph[i][j] = 1 means person i knows person j,
     * otherwise graph[i][j] = 0 means person i does not know person j. The celebrity is the person labeled as 1
     * because both 0 and 2 know him but 1 does not know anybody.
     * <p>
     * This input can be represented as 2D array/grid below
     * <p>
     * -    0 1 2
     * - 0 [1,1,0]
     * - 1 [0,1,0]
     * - 2 [1,1,1]
     * <p>
     * graph[i][j] = 1 i knows j
     * <p>
     * Then when you look at that you can see the celebrity has the following properties:
     * <p>
     * 1. The column of the celebrity will be all ones
     * 2. The row of the celebrity will be all zeros except for where row == col
     * (because that's the celebrity and they know themselves).
     * <p>
     * https://leetcode.com/problems/find-the-celebrity/description/
     */
    @Test
    void testFindCelebrity() {
        Assertions.assertThat(findCelebrity(3)).isEqualTo(1);
    }

    /**
     * Iterate from 0 to n-1, make the first person as candidate, then call knows API w/ candidate and
     * the next person. If returns true, makes the next person as candidate. Otherwise, keep the same
     * candidate and continue the loop for the next person. After the loop, we need to use the candidate
     * w/ API to check if he indeed doesn't know everyone and everyone knows him. Otherwise, there is no
     * celebrity.
     * <p>
     * Observation
     * The brute force approach iterates every person and call the knows API iteratively on the rest of ppl
     * to determine if he is celebrity. Thus will take O(n^2).
     * To improve the alog, the idea is with each call to knows(...), we can conclusively determine that
     * exactly 1 of the people is not a celebrity!
     * Ex, A knows B? True -> A can't be a celebrity. False -> B can't be a celebrity.
     * <p>
     * Therefore, we can have the following algo to rule out n - 1 of the people in O(n) time.
     * We start by guessing that 0 might be a celebrityCandidate, and then we check if 0 knows 1
     * (within the loop). If true, then we know 0 isn't a celebrity (they know somebody), but 1 might be.
     * We update the celebrityCandidate variable to 1 to reflect this. Otherwise, we know 1 is not a
     * celebrity (somebody doesn't know them), but we haven't ruled out 0, yet, so keep them as the
     * celebrityCandidate. Whoever we kept is then asked if they know 2, and so forth.
     * <p>
     * celebrity_candidate = 0
     * for i in range(1, n):
     * -    if knows(celebrity_candidate, i):
     * -        celebrity_candidate = i
     * <p>
     * In the end, the only person we haven't ruled out is in the celebrityCandidate variable.
     * However, cuz it is possible that there is NO celebrity at all, we still need to use isCelebrity(...)
     * function on this person to check whether he is a celebrity, i.e. For every person i other than celebrityCandidate,
     * if (knows(celebrityCandidate, i) || !knows(i, celebrityCandidate)) is true, then no celebrity. Otherwise,
     * celebrityCandidate is celebrity
     * <p>
     * Time Complexity : O(n).
     * The first part finds a celebrity candidate. This requires doing n−1 calls to knows(...) API, and so is O(n).
     * The second part is the same as before—checking whether a given person is a celebrity. We determined that this is O(n).
     * Therefore, we have a total time complexity of O(n+n) = O(n).
     * <p>
     * Space Complexity : O(1).
     */
    int findCelebrity(int n) {
        int celebrityCandidate = 0;
        for (int i = 0; i < n; i++) {
            if (knows(celebrityCandidate, i))
                // Rule out this celebrityCandidate cuz celebrity shouldn't know anyone
                celebrityCandidate = i;
        }
        // Need to do final check on this candidate
        return isCelebrity(celebrityCandidate, n) ? celebrityCandidate : -1;
    }

    boolean isCelebrity(int i, int numOfPpl) {
        for (int j = 0; j < numOfPpl; j++) {
            if (i == j) // Don't ask if they know themselves.
                continue;
            if (knows(i, j) || !knows(j, i)) // Celebrity knows no one and everyone should know celebrity
                return false;
        }
        return true;
    }

    // This is the hardcoded to simulate the API call on the LeetCode using the input example
    boolean knows(int i, int j) {
        int[][] matrix = {
                {1, 1, 0},
                {0, 1, 0},
                {1, 1, 1}
        };
        return matrix[i][j] == 1;
    }


    /**
     * Number of Islands
     * Given an m x n 2D binary grid which represents a map of '1's (land) and '0's (water), return the
     * number of islands.
     * <p>
     * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
     * You may assume all four edges of the grid are all surrounded by water.
     * <p>
     * Input: grid = [
     * ["1","1","1","1","0"],
     * ["1","1","0","1","0"],
     * ["1","1","0","0","0"],
     * ["0","0","0","0","0"]
     * ]
     * Output: 1
     * <p>
     * https://leetcode.com/problems/number-of-islands/description/
     */
    @Test
    void testNumIslands() {
        char[][] grid = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}};
        Assertions.assertThat(numIslandsBFS(grid)).isEqualTo(1);
        Assertions.assertThat(numIslandsDFS(grid)).isEqualTo(1);
    }

    /**
     * BFS
     * Use a separate boolean 2D array to keep track of the visited cells.
     * Iterate the grid, if the cell contains '1' and not visited, use it as starting cell to perform BFS
     * traversal. In BFS, we move to 4 directions from one cell if they have '1', and then mark them
     * visited. We increment the island count after finishing one iteration of BFS from a cell
     * Time Complexity: O(M⋅N) where M is the number of rows and N is the number of columns.
     * Space Complexity: When we use the separate 2D array for keeping track of the visiting nodes, it will be O(M⋅N).
     * However, if we use the same approach as DFS, i.e. marking the visited cell directly in the source grid,
     * it will be O(min(M,N)) because in worst case where the grid is filled with lands,
     * the size of queue can grow up to min(M,N).
     */
    int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0)
            return 0;
        int count = 0;
        int rowNum = grid.length, colNum = grid[0].length;
        boolean[][] visited = new boolean[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    markIslandBFS(i, j, grid, visited);
                    ++count;
                }
            }
        }
        return count;
    }

    void markIslandBFS(int row, int col, char[][] grid, boolean[][] visited) {
        int[][] directions = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        Queue<int[]> cellQueue = new ArrayDeque<>(); // stores the index/coordinate(x,y) of the cell
        cellQueue.offer(new int[]{row, col});
        visited[row][col] = true;
        while (!cellQueue.isEmpty()) {
            int[] cell = cellQueue.poll();
            for (int[] dir : directions) {
                int x = cell[0] + dir[0];
                int y = cell[1] + dir[1];
                if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length // boundary check
                        && !visited[x][y] && grid[x][y] == '1') {
                    cellQueue.offer(new int[]{x, y});
                    visited[x][y] = true;
                }
            }
        }
    }

    /**
     * DFS
     * Use a separate boolean 2D array to keep track of the visited cells.
     * Iterate the grid, if the cell contains '1' and not visited, use it as starting cell to perform
     * DFS traversal. In DFS, if the rowIdx and colIdx is not out of boundary and not visited and
     * has '1', we mark the node visited and make recursive calls using 4 adjacent cells respectively.
     * We increment the island count after finishing one iteration of DFS from a cell
     * Time Complexity: O(M⋅N) where M is the number of rows and N is the number of columns.
     * Space Complexity: O(M⋅N) in case that the grid map is filled with lands where DFS goes by M⋅N deep.
     */
    int numIslandsDFS(char[][] grid) {
        if (grid == null || grid.length == 0)
            return 0;
        int count = 0;
        int rowNum = grid.length, colNum = grid[0].length;
        boolean[][] visited = new boolean[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if (grid[i][j] == '1' && !visited[i][j]) {
                    markIslandDFS(i, j, grid, visited);
                    count++;
                }
            }
        }
        return count;
    }


    void markIslandDFS(int row, int col, char[][] grid, boolean[][] visited) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length // boundary check
                || visited[row][col] || grid[row][col] == '0') // Only visit unvisited '1' cell
            return;
        visited[row][col] = true;
        markIslandDFS(row + 1, col, grid, visited);
        markIslandDFS(row - 1, col, grid, visited);
        markIslandDFS(row, col + 1, grid, visited);
        markIslandDFS(row, col - 1, grid, visited);
    }

    /**
     * Word Search
     * Given an m x n grid of characters board and a string word, return true if word exists in the grid.
     * The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or
     * vertically neighboring. The same letter cell may not be used more than once.
     * <p>
     * board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
     * Output: true
     * <p>
     * https://leetcode.com/problems/word-search/description/
     */
    @Test
    void testExist() {
        char[][] board = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
        Assertions.assertThat(exist(board, "ABCCED")).isTrue();
    }

    /**
     * Iterates each cell in the grid. For each cell, pass the (x, y), searchCharIdx(0) and a visited boolean
     * grid to a method. In the method, if (x, y) out of bound or cell was visited or current cell value !=
     * searchCharIdx value in the string, returns false. If searchCharIdx is the last index of the string
     * and cell value are equal, returns true. Otherwise, mark the cell visited and recursively explore four
     * adjacent cells w/ searchCharIdx + 1 to search for the next char in the string. If anyone returns true,
     * return true. Otherwise, mark the current cell not visited before we backtrack to the previous cell
     * and return false.
     * <p>
     * We would walk around the 2D grid, and at each step, we mark our choice before jumping into the next
     * step. And at the end of each step, we would also revert our mark so that we will have a clean slate
     * to try another direction.
     * In addition, the exploration is done via the DFS strategy, where we go as far as possible before
     * we try the next direction.
     * Key points:
     * 1. The recursive method should have an searchCharIdx var and get increment by 1 when it is called,
     * so we can use it as base case to determine if we find the last matched char in the word
     * 2. We need to check the row and col boundary besides the cell value against the char in the word
     * 3. When doing DFS recursive call, we need to call it for four adjacent cells, which means passing
     * different row and col four times.
     * 4. We need to mark the current visiting cell in the boolean grid, so it won't be visited again.
     * We also need to revert it back once recursive call returns.
     * <p>
     * Note: we can reduce the space complexity to O(L), where L is the length of the word to be matched
     * and also the max depth of the call stack, if we just modify the input grid using the special char
     * to mark the visited cell instead of a separate boolean grid.
     * <p>
     * Time Complexity:O(N⋅3^L), where N is the number of cells in the board and L is the length of the word to be matched.
     * 1. Besides the very first move, we can have at most 4 directions to move, the choices are reduced to 3. So it is
     * basically 3-ary tree, each of the branches represent a potential exploration in the corresponding direction.
     * Therefore, in the worst case, the total number of invocation would be the number of nodes in a full 3-nary tree,
     * which is about 3^L
     * 2. We iterate through the board for backtracking, i.e. there could be N times invocation for the backtracking
     * function in the worst case.
     * <p>
     * Space Complexity: O(L + M⋅N) where L is the length of the word to be matched.
     * The maximum length of the call stack would be the length of the word. M⋅N to maintain the boolean visited grid
     */
    boolean exist(char[][] board, String word) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boolean[][] visited = new boolean[board.length][board[0].length];
                if (searchWord(i, j, 0, board, word, visited))
                    return true;
            }
        }
        return false;
    }

    boolean searchWord(int r, int c, int charIdx, char[][] board, String word, boolean[][] visited) {
        // Check boundaries and if the current cell was visited and has the char we currently look for
        if (r < 0 || r == board.length || c < 0 || c == board[0].length
                || visited[r][c] || word.charAt(charIdx) != board[r][c]) {
            return false;
        }
        // charIdx is used for tracking the progress for searching each char in the word. It is incremented in each
        // recursive call, so we will search the next char of the word in the next adjacent cells.
        // When the recursive call reaches here w/ index equal to the word length, it means this cell has the last char
        // of the string, so we successfully found the whole word
        if (charIdx == word.length() - 1) {
            return true;
        }
        // The current cell has the char we need. Mark the cell visited, so recursive DFS later on won't go
        // back to visit the same cell twice in one path (call stack)
        visited[r][c] = true;

        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        boolean found = false;
        /// search the next char in the word in DFS manner toward four adjacent cells
        for (int[] dir : dirs) {
            int row = r + dir[0];
            int col = c + dir[1];
            if (searchWord(row, col, charIdx + 1, board, word, visited)) {
                return true;
            }
        }
        // When reaching here, it means all paths from 4 direction of the current cell didn't find the char(s) we need.
        // Before returning to the previous cell to explore other adjacent cells, we need to mark this cell unvisited,
        // so it can be visited again from different path. This is basically BACKTRACKING.
        visited[r][c] = false;
        return false;
    }

    private class Node {
        public int val;
        public List<Node> neighbors;

        public Node() {
            val = 0;
            neighbors = new ArrayList<>();
        }

        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<>();
        }

        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }

    /**
     * Clone Graph
     * Given a reference of a node in a connected undirected graph.
     * Return a deep copy (clone) of the graph.
     * Each node in the graph contains a value (int) and a list (List[Node]) of its neighbors.
     * <p>
     * class Node {
     * public int val;
     * public List<Node> neighbors;
     * }
     * Test case format:
     * <p>
     * For simplicity, each node's value is the same as the node's index (1-indexed). For example,
     * the first node with val == 1, the second node with val == 2, and so on. The graph is represented
     * in the test case using an adjacency list.
     * <p>
     * An adjacency list is a collection of unordered lists used to represent a finite graph. Each list
     * describes the set of neighbors of a node in the graph.
     * <p>
     * The given node will always be the first node with val = 1. You must return the copy of the given
     * node as a reference to the cloned graph.
     * <p>
     * Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
     * Output: [[2,4],[1,3],[2,4],[1,3]]
     * Explanation: There are 4 nodes in the graph.
     * 1st node (val = 1)'s neighbors are 2nd node (val = 2) and 4th node (val = 4).
     * 2nd node (val = 2)'s neighbors are 1st node (val = 1) and 3rd node (val = 3).
     * 3rd node (val = 3)'s neighbors are 2nd node (val = 2) and 4th node (val = 4).
     * 4th node (val = 4)'s neighbors are 1st node (val = 1) and 3rd node (val = 3).
     * <p>
     * https://leetcode.com/problems/clone-graph/description/
     */
    @Test
    void testCloneGraph() {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        node1.neighbors = List.of(node2, node4);
        node2.neighbors = List.of(node1, node3);
        node3.neighbors = List.of(node2, node4);
        node4.neighbors = List.of(node1, node3);
        Node clonedNode1 = cloneGraphDFS(node1);
        Assertions.assertThat(clonedNode1.neighbors).hasSize(2);
    }

    /**
     * DFS traversal and clone node in the graph while maintaining a visited node -> new node Map
     * 1. Traverse the graph from the given node in DFS manner.
     * 2. If we don't find the node in the visited hash map, we create a copy of it and put it in the hash map.
     * 3. Now make the recursive call for the neighbors of the node
     * <p>
     * Time Complexity: O(N+M), where N is a number of nodes (vertices) and M is a number of edges.
     * Space Complexity: O(N). This space is occupied by the visited hash map and in addition to that,
     * space would also be occupied by the recursion stack since we are adopting a recursive approach here.
     * The space occupied by the recursion stack would be equal to O(H) where H is the height of the graph.
     * Overall, the space complexity would be O(N).
     */
    Node cloneGraphDFS(Node node) {
        Map<Node, Node> visitedToClone = new HashMap<>();
        return cloneAdjacentNode(node, visitedToClone);
    }

    Node cloneAdjacentNode(Node node, Map<Node, Node> visitedToClone) {
        // base case
        // If the node was already visited before, return the clone from the map, so we won't be stuck in infinite recursion
        if (visitedToClone.containsKey(node)) {
            return visitedToClone.get(node);
        }
        if (node == null)
            return null;
        Node newNode = new Node(node.val);
        visitedToClone.put(node, newNode);
        // Iterate through the neighbors to generate their clones
        // and prepare a list of cloned neighbors to be added to the cloned node.
        for (Node neighbor : node.neighbors) {
            newNode.neighbors.add(cloneAdjacentNode(neighbor, visitedToClone));
        }
        return newNode;
    }

    Node cloneGraphBFS(Node node) {
        if (node == null) {
            return null;
        }

        // Hash map to save the visited node and it's respective clone
        // as key and value respectively. This helps to avoid cycles.
        Map<Node, Node> visited = new HashMap<>();

        // Put the first node in the queue
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(node);
        // Clone the node and put it in the visited dictionary.
        visited.put(node, new Node(node.val, new ArrayList<>()));

        // Start BFS traversal
        while (!queue.isEmpty()) {
            // Pop a node say "n" from the from the front of the queue.
            Node n = queue.remove();
            // Iterate through all the neighbors of the node "n"
            for (Node neighbor : n.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    // Clone the neighbor and put in the visited, if not present already
                    visited.put(neighbor, new Node(neighbor.val, new ArrayList<>()));
                    // Add the newly encountered node to the queue.
                    queue.add(neighbor);
                }
                // Add the clone of the neighbor to the neighbors of the clone node "n".
                visited.get(n).neighbors.add(visited.get(neighbor));
            }
        }

        // Return the clone of the node from visited.
        return visited.get(node);
    }


    /**
     * Flood Fill
     * An image is represented by an m x n integer grid image where image[i][j] represents the pixel
     * value of the image.
     * You are also given three integers sr, sc, and color. You should perform a flood fill on the
     * image starting from the pixel image[sr][sc].
     * <p>
     * To perform a flood fill, consider the starting pixel, plus any pixels connected 4-directionally
     * to the starting pixel of the same color as the starting pixel, plus any pixels connected
     * 4-directionally to those pixels (also with the same color), and so on. Replace the color
     * of all of the aforementioned pixels with color.
     * <p>
     * Return the modified image after performing the flood fill.
     * <p>
     * Input: image = [[1,1,1],[1,1,0],[1,0,1]], sr = 1, sc = 1, color = 2
     * Output: [[2,2,2],[2,2,0],[2,0,1]]
     * Explanation: From the center of the image with position (sr, sc) = (1, 1) (i.e., the red pixel),
     * all pixels connected by a path of the same color as the starting pixel (i.e., the blue pixels)
     * are colored with the new color.
     * Note the bottom corner is not colored 2, because it is not 4-directionally connected to the
     * starting pixel.
     * <p>
     * https://leetcode.com/problems/flood-fill/description/
     */
    @Test
    void testFloodFill() {
        int[][] grid = {
                {1, 1, 1},
                {1, 1, 0},
                {1, 0, 1}
        };
        int[][] answer = {
                {2, 2, 2},
                {2, 2, 0},
                {2, 0, 1}
        };
        int[][] output = floodFill(grid, 1, 1, 2);
        Assertions.assertThat(Arrays.deepEquals(answer, output)).isTrue();
    }

    /**
     * Traversal the 4 neighboring cells from the given cell in DFS manner. Update to the new color if it is
     * the color we are target.
     * <p>
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int oldColor = image[sr][sc];
        if (color != oldColor)
            updateCellColor(image, sr, sc, oldColor, color);
        return image;
    }

    void updateCellColor(int[][] grid, int row, int col, int oldColor, int newColor) {
        // base case
        if (row >= grid.length || row < 0 || col < 0 || col >= grid[row].length
                || grid[row][col] != oldColor || grid[row][col] == newColor)
            // Move the index boundary check before making recursive call to reduce unnecessary calls
            return;
        grid[row][col] = newColor;
        updateCellColor(grid, row + 1, col, oldColor, newColor);
        updateCellColor(grid, row - 1, col, oldColor, newColor);
        updateCellColor(grid, row, col + 1, oldColor, newColor);
        updateCellColor(grid, row, col - 1, oldColor, newColor);
    }

    /**
     * 01 Matrix
     * Given an m x n binary matrix mat, return the distance of the nearest 0 for each cell.
     * <p>
     * The distance between two adjacent cells is 1.
     * <p>
     * Input: mat = [[0,0,0],[0,1,0],[1,1,1]]
     * Output: [[0,0,0],[0,1,0],[1,2,1]]
     * <p>
     * https://leetcode.com/problems/01-matrix/description/
     */
    @Test
    void testUpdateMatrix() {
        int[][] grid = {
                {0, 0, 0},
                {0, 1, 0},
                {1, 1, 1}
        };
        int[][] answer = {
                {0, 0, 0},
                {0, 1, 0},
                {1, 2, 1}
        };
        int[][] output = updateMatrix(grid);
        Assertions.assertThat(Arrays.deepEquals(answer, output)).isTrue();
    }

    /**
     * Use multi-source BFS to start traversal from all cells w/ value 0. The distance to 0 is equal to the
     * BFS level from the source cell, i.e. "0" cell.
     * <p>
     * Essentially this is a shortest path graph problem, so BFS is usually the best solution. However,
     * If we start BFS from cell w/ value 1, we can only find the shortest distance for that 1. If we start BFS from 0,
     * we could find the shortest distance for many 1 at a time. So to make it more efficient, we should start from
     * all cell w/ 0.
     * <p>
     * Regarding how to compute the distance, from a source node, we first visit all nodes at a distance of 1.
     * Next, we visit all nodes at a distance of 2, then 3, and so on. We can say a node at a distance of x from
     * the source belongs to "level x". So the source is at level 0, the neighbors of the source are at level 1,
     * the neighbors of those nodes are at level 2, and so on. Since our sauce node is "0", the level is exactly the
     * answer we want.
     * <p>
     * Algo:
     * 1. Create an answer grid w/ the same size as input matrix.
     * 2. Use a boolean grid to mark nodes we have already visited and a queue for the BFS.
     * 3. Put all nodes with 0 into the queue. Mark these nodes in seen as well.
     * 4. Perform the BFS:
     * - While queue is not empty, get the current row, col, from the queue.
     * - Iterate over the 4 directions. For each nextRow, nextCol, check if it is in bounds and not already
     * visited in seen.
     * - If so, set answer[nextRow][nextCol] = answer[currRow][curCol] + 1 and push nextRow, nextCol onto the
     * queue. Also mark nextRow, nextCol in visited grid.
     * 5. Return matrix.
     * <p>
     * Time complexity: O(m⋅n)
     * The BFS never visits a node more than once due to seen. Each node has at most 4 neighbors,
     * so the work done at each node is O(1). This gives us a time complexity of O(m⋅n), the number of nodes.
     * <p>
     * Space complexity: O(m⋅n)
     */
    class Cell {
        int row;
        int col;
        int step; // Track the level of BFS when the cell is visited

        Cell(int row, int col, int step) {
            this.row = row;
            this.col = col;
            this.step = step;
        }
    }

    int[][] updateMatrix(int[][] mat) {
        int rowNum = mat.length, colNum = mat[0].length;
        int[][] answer = new int[rowNum][colNum]; // cell stores the distance of the nearest 0 in the original matrix
        int[][] direction = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        boolean[][] visited = new boolean[rowNum][colNum];
        Queue<Cell> queue = new ArrayDeque<>();

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                int val = mat[i][j];
                if (val == 0) {
                    // Insert all '0' cells to the queue and mark correspond cell visited
                    queue.offer(new Cell(i, j, 0));
                    visited[i][j] = true;
                }
            }
        }
        BiPredicate<Integer, Integer> validBound = (r, c) -> r >= 0 && r < rowNum && c >= 0 && c < colNum;

        while (!queue.isEmpty()) {
            Cell cell = queue.poll();
            for (int[] dir : direction) {
                // Iterate over 4 neighboring cells
                int nextRow = cell.row + dir[0], nextCol = cell.col + dir[1];
                if (validBound.test(nextRow, nextCol) && !visited[nextRow][nextCol]) { // Check inside the bound and not visited
                    // ** This is the alternative way to compute the distance w/o storing the step/level in the Cell object **
                    // answer grid is init to 0, when we perform BFS from the '0' cells removed from the queue, the distance of the
                    // neighbor cell is the distance of the source cell(stored in the answer grid) + 1. For example, the first
                    // level BFS makes all neighbors of '0' cell in the matrix have '1' in the answer grid. And the 2nd level BFS
                    // makes the neighbors of those cells have '2' in the answer grid, so on and so forth.
                    // Therefore, neighboring cell distance is the source cell distance + 1
                    // int distance = answer[cell.row][cell.col] + 1;
                    // answer[nextRow][nextCol] = distance;

                    // Every time when we traverse to 4 adjacent cells from a cell polled from the queue, we increment the BFS
                    // level/steps from the source cell. Cuz we start BFS from '0' cells, so the level/steps is the distance to
                    // the '0'.
                    // Note: Each cell only expands one level at one time, so the '1' cell first mark visited is guaranteed having
                    // the shortest distance to '0' cell
                    int level = cell.step + 1;
                    answer[nextRow][nextCol] = level;
                    visited[nextRow][nextCol] = true;
                    queue.offer(new Cell(nextRow, nextCol, level));
                }
            }
        }
        return answer;
    }

    /**
     * Rotting Oranges
     * You are given an m x n grid where each cell can have one of three values:
     * <p>
     * 0 representing an empty cell,
     * 1 representing a fresh orange, or
     * 2 representing a rotten orange.
     * Every minute, any fresh orange that is 4-directionally adjacent to a rotten orange becomes rotten.
     * <p>
     * Return the minimum number of minutes that must elapse until no cell has a fresh orange.
     * If this is impossible, return -1.
     * <p>
     * Input: grid = [[2,1,1],[1,1,0],[0,1,1]]
     * Output: 4
     * <p>
     * Input: grid = [[2,1,1],[0,1,1],[1,0,1]]
     * Output: -1
     * Explanation: The orange in the bottom left corner (row 2, column 0) is never rotten,
     * because rotting only happens 4-directionally.
     * <p>
     * https://leetcode.com/problems/rotting-oranges/description/
     */
    @Test
    void testOrangesRotting() {
        int[][] grid = {
                {2, 1, 1},
                {1, 1, 0},
                {0, 1, 1}
        };
        Assertions.assertThat(orangesRotting(grid)).isEqualTo(4);
    }

    /**
     * Use multi-source BFS to start traversal from all cells w/ rotted oranges. Only visit the fresh orange cell
     * and devrement its count and increment the time after every BFS level/iteration
     * <p>
     * Perform the BFS from all rotted orange cell as source. We mutate the input grid in place when a fresh orange
     * cell is visited so no need to maintain another "visited" data structure.
     * <p>
     * The key is for each BFS round/iteration, we only process the cells in the queue inserted from the last round.
     * Because we need to increment the time ONLY after every node finished one round of BFS.
     * <p>
     * Time Complexity: O(N⋅M), where N×M is the size of the grid.
     * First, we scan the grid to find the initial values for the queue, which would take O(N⋅M) time.
     * Then we run the BFS process on the queue, which in the worst case would enumerate all the cells
     * in the grid once and only once. Therefore, it takes O(N⋅M) time.
     * <p>
     * Space Complexity: O(N⋅M), where N×M is the size of the grid.
     * In the worst case, the grid is filled with rotten oranges. As a result, the queue would be initialized
     * with all the cells in the grid.
     */
    int orangesRotting(int[][] grid) {
        Queue<Pair<Integer, Integer>> queue = new ArrayDeque<>();
        int rowNum = grid.length, colNum = grid[0].length;
        int freshOrangeCount = 0;
        int timeElapsed = 0;

        // Count the fresh oranges and insert the cells w/ rotten orange to queue
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if (grid[i][j] == 2)
                    queue.offer(new Pair<>(i, j));
                else if (grid[i][j] == 1)
                    ++freshOrangeCount;
            }
        }

        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        while (!queue.isEmpty() && freshOrangeCount > 0) {
            // This number is the total number of cells inserted from the previous round of BFS. We need to take a snapshot first.
            int qSize = queue.size();
            for (int i = 1; i <= qSize; i++) { // Only visit the cells at the same level by limiting the number of iteration to poll the queue
                Pair<Integer, Integer> cell = queue.poll();
                int rowIdx = cell.getKey();
                int colIdx = cell.getValue();
                for (int[] direction : directions) {
                    // Start BFS for 4 neighboring cells
                    int nextRowIdx = direction[0] + rowIdx;
                    int nextColIdx = direction[1] + colIdx;
                    if (nextRowIdx >= 0 && nextRowIdx < rowNum && nextColIdx >= 0 && nextColIdx < colNum
                            && grid[nextRowIdx][nextColIdx] == 1) {
                        grid[nextRowIdx][nextColIdx] = 2;
                        queue.offer(new Pair<>(nextRowIdx, nextColIdx));
                        --freshOrangeCount;
                    }
                }
            }
            // Finished one round of BFS on all rotten oranges made from last round, so increment the time
            ++timeElapsed;
        }
        return freshOrangeCount == 0 ? timeElapsed : -1;
    }

    /**
     * Course Schedule
     * There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1.
     * You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must
     * take course bi first if you want to take course ai.
     * <p>
     * For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
     * Return true if you can finish all courses. Otherwise, return false.
     * <p>
     * Input: numCourses = 2, prerequisites = [[1,0]]
     * Output: true
     * Explanation: There are a total of 2 courses to take.
     * To take course 1 you should have finished course 0. So it is possible.
     * <p>
     * Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
     * Output: false
     * Explanation: There are a total of 2 courses to take.
     * To take course 1 you should have finished course 0, and to take course 0 you should also have
     * finished course 1. So it is impossible.
     * <p>
     * https://leetcode.com/problems/course-schedule/description/
     */
    @Test
    void testCanFinish() {
        int[][] grid = {
                {1, 0},
                {0, 1}
        };
        Assertions.assertThat(canFinish(2, grid)).isFalse();
        grid = new int[][]{
                {1, 4}, {2, 4}, {3, 1}, {3, 2}
        };
        Assertions.assertThat(canFinish(5, grid)).isTrue();
        grid = new int[][]{
                {1, 0},
                {2, 0},
                {2, 1}
        };
        Assertions.assertThat(canFinish(3, grid)).isTrue();
    }


    /**
     * Implement Kahn's Topological sorting algo to detect the cycle in the graph
     * <p>
     * We can think each course as a node and draw an edge from b to a for any prerequisite [a, b]
     * (to indicate that course b should be completed before taking course a), we get a directed graph.
     * <p>
     * If there is a cycle in this directed graph, it suggests that we will not be able to finish all
     * the courses. Otherwise, we can perform a topological sort of the graph to determine the order
     * in which all the courses can be finished.
     * We implement the Kahn's algorithm to get the topological ordering. Kahn’s algorithm works by
     * keeping track of the number of incoming edges into each node (indegree). It works by repeatedly
     * visiting the nodes with an indegree of zero and deleting all the edges associated with it leading
     * to a decrement of indegree for the nodes whose incoming edges are deleted. This process continues
     * until no elements with zero indegree can be found.
     * *The Kahn’s algo usually produces the result of topological ordering, but here we only care
     * if there is cycle
     * <p>
     * 1. Create an array indegree of length n where indegree[x] stores the number of edges entering node x.
     * 2. We create an adjacency list adj in which adj[x] contains all the nodes with an incoming edge from
     * node x, i.e., neighbors of node x. We create this adjacency list by iterating over prerequisites. For
     * every prerequisite in prerequisites, we add an edge from prerequisite[1] to prerequisite[0] and
     * increment the indegree of prerequisite[0] by 1.
     * 3. Initialize a queue of integers q and start a BFS algorithm moving from the leaf nodes to the
     * parent nodes.
     * 4. Begin the BFS traversal by pushing all the leaf nodes (indegree equal to 0) in the queue.
     * 5. Create an integer variable visitedNodeCount = 0 to count the number of visited nodes.
     * 6. While the queue is not empty;
     * -	Dequeue the first node from the queue.
     * -	Increment nodesVisited by 1.
     * -	For each neighbor of node (nodes that have an incoming edge from node), we decrement
     * -    indegree[neighbor]by 1 to delete the node -> neighbor edge.
     * -	If indegree[neighbor] == 0, we push this neighbor node to the queue.
     * If the number of nodes visited is less than the total number of nodes, i.e., visitedNodeCount < n we
     * return false as there must be a cycle. Otherwise, if visitedNodeCount == numCourses, we return true.
     * We can shorten it to just return visitedNodeCount == numCourses.
     * <p>
     * Time complexity: O(m+n), n be the number of courses and m be the size of prerequisites
     * Initializing the adj list takes O(m) time as we go through all the edges. The indegree array take O(n) time.
     * Each queue operation takes O(1) time, and a single node will be pushed once, leading to O(n) operations for n
     * nodes. We iterate over the neighbors of each node that is popped out of the queue iterating over all the edges
     * once. Since there are total of m edges, it would take O(m) time to iterate over the edges.
     * <p>
     * Space complexity: O(m+n)
     * The adj arrays takes O(m) space. The indegree array takes O(n) space.
     * The queue can have no more than nnn elements in the worst-case scenario. It would take up O(n)
     * space in that case.
     */
    boolean canFinish(int numCourses, int[][] prerequisites) {
        int[] inDegree = new int[numCourses]; // count of the incoming degree of each node
        List<List<Integer>> adjacencyList = new ArrayList<>(numCourses);
        for (int i = 0; i < numCourses; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        // Build the adjacency list and inDegree array
        // List[n]: neighbors of node n, i.e. nodes that has an incoming edge from node n. Ex, n --> a, n --> b, a and b are n's neighbors
        // Ex: prerequisites: [[1, 0], [2, 0], [2, 1]], we will have List {[1, 2], [1], []}
        for (int[] prerequisite : prerequisites) {
            // [1, 0] => 1 depends on 0, i.e. 0 --> 1 in the graph(directed edge from 0 to 1)
            adjacencyList.get(prerequisite[1]).add(prerequisite[0]);
            inDegree[prerequisite[0]]++; // increment the indegree of the node having the incoming edge
        }

        Queue<Integer> queue = new ArrayDeque<>(); // Stores the course number
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0)
                // Push all the nodes with indegree zero in the queue.
                queue.offer(i);
        }
        int visitedNodeCount = 0;
        while (!queue.isEmpty()) {
            Integer node = queue.poll();
            visitedNodeCount++;
            for (Integer neighbor : adjacencyList.get(node)) {
                // Delete the edge "node -> neighbor".
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0)
                    // Push the node having no incoming edges to the queue
                    queue.offer(neighbor);
            }
        }
        // If there is no cycle in the graph, all nodes should be visited.
        // Nodes forming the cycle will still have inDegree > 0 after we remove other edges, so they were never push to the queue
        return visitedNodeCount == numCourses;
    }

    /**
     * Course Schedule II
     * There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1.
     * You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that
     * you must take course bi first if you want to take course ai.
     * <p>
     * For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
     * Return the ordering of courses you should take to finish all courses. If there are many valid
     * answers, return any of them. If it is impossible to finish all courses, return an empty array.
     * <p>
     * Input: numCourses = 2, prerequisites = [[1,0]]
     * Output: [0,1]
     * Explanation: There are a total of 2 courses to take. To take course 1 you should have finished
     * course 0. So the correct course order is [0,1].
     * <p>
     * <p>
     * Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
     * Output: [0,2,1,3]
     * Explanation: There are a total of 4 courses to take. To take course 3 you should have finished
     * both courses 1 and 2. Both courses 1 and 2 should be taken after you finished course 0.
     * So one correct course order is [0,1,2,3]. Another correct ordering is [0,2,1,3].
     * <p>
     * Input: numCourses = 1, prerequisites = []
     * Output: [0]
     * <p>
     * https://leetcode.com/problems/course-schedule-ii/description/
     */
    @Test
    void testFindOrder() {
        int[][] grid = {
                {1, 0}
        };
        Assertions.assertThat(findOrder(2, grid)).containsExactly(0, 1);
        grid = new int[][]{
                {1, 0},
                {2, 0},
                {3, 1},
                {3, 2}
        };
        Assertions.assertThat(findOrder(4, grid)).containsExactly(0, 1, 2, 3);
    }

    /**
     * Implement Kahn's Topological sorting algo to detect the cycle in the graph and generate topological ordering.
     * <p>
     * This problem is almost the same as Course Schedule 1. But it also asks to return the ordering.
     * All we need is when removing the node from the queue, we also add the course node to the order array
     * and return it if there is no cycle.
     * <p>
     * Note: Code is 95% the same as last problem, but some naming is different and more generic so we can consider
     * this is more like a generic implementation of Kahn's algo
     * <p>
     * Time complexity: O(m+n)
     * Initializing the adj list takes O(m) time as we go through all the edges. The indegree array take O(n) time.
     * Each queue operation takes O(1) time, and a single node will be pushed once, leading to O(n) operations for n
     * nodes. We iterate over the neighbors of each node that is popped out of the queue iterating over all the edges
     * once. Since there are total of m edges, it would take O(m) time to iterate over the edges.
     * <p>
     * Space complexity: O(m+n)
     * The adj arrays takes O(m) space. The indegree array takes O(n) space.
     * The queue can have no more than nnn elements in the worst-case scenario. It would take up O(n)
     * space in that case.
     */
    int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] inDegree = new int[numCourses];
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] prerequisite : prerequisites) {
            int from = prerequisite[1];
            int to = prerequisite[0];
            graph.get(from).add(to);
            ++inDegree[to];
        }
        Queue<Integer> zeroIngreeNodeQueue = new ArrayDeque<>();
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0)
                zeroIngreeNodeQueue.offer(i);
        }
        int[] order = new int[numCourses];
        int idx = 0;
        while (!zeroIngreeNodeQueue.isEmpty()) {
            Integer node = zeroIngreeNodeQueue.poll();
            order[idx++] = node; // This is the only new thing we need to add besides the solution of Course I problem
            for (Integer toNode : graph.get(node)) {
                --inDegree[toNode];
                if (inDegree[toNode] == 0)
                    zeroIngreeNodeQueue.offer(toNode);
            }
        }
        if (idx != numCourses)
            return new int[0];
        return order;
    }

    /**
     * Minimum Height Trees
     * A tree is an undirected graph in which any two vertices are connected by exactly one path.
     * In other words, any connected graph without simple cycles is a tree.
     * <p>
     * Given a tree of n nodes labelled from 0 to n - 1, and an array of n - 1 edges where
     * edges[i] = [ai, bi] indicates that there is an undirected edge between the two nodes ai and
     * bi in the tree, you can choose any node of the tree as the root. When you select a node x as
     * the root, the result tree has height h. Among all possible rooted trees, those with minimum
     * height (i.e. min(h))  are called minimum height trees (MHTs).
     * <p>
     * Return a list of all MHTs' root labels. You can return the answer in any order.
     * <p>
     * The height of a rooted tree is the number of edges on the longest downward path between the
     * root and a leaf.
     * <p>
     * Input: n = 4, edges = [[1,0],[1,2],[1,3]]
     * Output: [1]
     * Explanation: As shown, the height of the tree is 1 when the root is the node with label 1
     * which is the only MHT.
     * <p>
     * Input: n = 6, edges = [[3,0],[3,1],[3,2],[3,4],[5,4]]
     * Output: [3,4]
     * <p>
     * https://leetcode.com/problems/minimum-height-trees/description/
     */
    @Test
    void testFindMinHeightTrees() {
        int[][] grid = {
                {1, 0},
                {1, 2},
                {1, 3}
        };
        Assertions.assertThat(findMinHeightTrees(4, grid)).containsExactly(1);
        grid = new int[][]{
                {3, 0},
                {3, 1},
                {3, 2},
                {3, 4},
                {5, 4}
        };
        Assertions.assertThat(findMinHeightTrees(6, grid)).containsExactly(3, 4);
    }

    /**
     * Use the variant Topological sorting algo to iteratively remove the "leaf" node in the graph, then update
     * the degree of other nodes and remove the new leaves until 1 or 2 nodes left, which is the answer.
     * <p>
     * First, a leaf is a node of degree 1, and the height of a tree can be defined as the maximum distance
     * between the root and all its leaf nodes.We can rephrase the problem as finding out the nodes that are
     * overall close to all other nodes, especially the leaf nodes,
     * <p>
     * If we view the graph as an area of circle, and the leaf nodes as the peripheral of the circle, then
     * what we are looking for are actually the centroids of the circle, i.e. nodes that is close to all the
     * peripheral nodes (leaf nodes).
     * <p>
     * For the tree-alike graph, the number of centroids is no more than 2.
     * https://stackoverflow.com/questions/63237671/how-many-minimum-height-trees-mhts-can-a-graph-have-at-most
     * <p>
     * To find the centroids, the idea is that we trim out the leaf nodes layer by layer, until we reach the
     * core of the graph, which are the centroids nodes.
     * The trimming process continues until there are only one or two nodes left in the graph, which are the
     * centroids that we are looking for.
     * <p>
     * In our case, we trim out the leaf nodes first, which are the farther away from the centroids.
     * At each step, the nodes we trim out are closer to the centroids than the nodes in the previous step.
     * At the end, the trimming process terminates at the centroids nodes.
     * <p>
     * Algo: (It is similar to BFS topological sorting in that we keep removing nodes in the graph)
     * 1. Initially, we would build a graph with the adjacency list from the input.
     * 2. We then create a queue which would be used to hold the leaf nodes.
     * 3. At the beginning, we put all the current leaf nodes into the queue.
     * 4. We then run a loop until there is only two nodes left in the graph.
     * 5. At each iteration, we remove the current leaf nodes from the queue. While removing the nodes,
     * -  we also remove the edges that are linked to the nodes. As a consequence, some of the non-leaf
     * -  nodes would become leaf nodes. And these are the nodes that would be trimmed out in the next iteration.
     * 6. The iteration terminates when there are no more than two nodes left in the graph, which are the desired
     * -  centroids nodes.
     * <p>
     * Time Complexity: O(V), V be the number of nodes in the graph, then the number of edges would be V−1
     * First, it takes V−1 iterations for us to construct a graph, given the edges.
     * With the constructed graph, we retrieve the initial leaf nodes, which takes V steps.
     * <p>
     * During the BFS trimming process, we will trim out almost all the nodes V and edges V-1 from the edges.
     * Therefore, it would take us around V+V−1 operations to reach the centroids.
     * To sum up, the overall time complexity of the algorithm is O(V).
     * <p>
     * Space Complexity: O(V)
     * <p>
     * We construct the graph with adjacency list, which has V nodes and V−1 edges.
     * Therefore, we would need V+V-1 space for the representation of the graph.
     * <p>
     * In addition, we use a queue to keep track of the leaf nodes.
     * In the worst case, the nodes form a star shape, with one centroid and the rest of the nodes as
     * leaf nodes. In this case, we would need V−1 space for the queue.
     * <p>
     * To sum up, the overall space complexity of the algorithm is also O(V)
     *
     * @param n     total number of nodes in a tree
     * @param edges an array of n - 1 edges where edges[i] = [ai, bi] indicates that there is an undirected edge between the two nodes ai and bi in the tree
     * @return a list of all MHTs' root labels.
     */
    List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (n == 1)
            return List.of(0);
        // Build adjacency list to represent the UNDIRECTED graph
        List<Set<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
            graph.add(new HashSet<>());
        for (int[] edge : edges) {
            int start = edge[0];
            int end = edge[1];
            graph.get(start).add(end);
            graph.get(end).add(start);
        }
        // A leaf node is a node of degree 1, which means it only has one neighbor
        Queue<Integer> leafNodeQueue = new ArrayDeque<>();
        // Push all leaf nodes to the queue
        for (int i = 0; i < graph.size(); i++) {
            if (graph.get(i).size() == 1)
                leafNodeQueue.offer(i);
        }
        int remainingNodeCount = n; // Keep track of how many nodes still not visited after removing the leaf node iteratively
        while (remainingNodeCount > 2) {
            // Use BFS level traversal style, cuz we may add new node to the queue when removing each node at the same time, we need to
            // process the nodes inserted in the queue from last iteration.
            // At each iteration, we remove the current leaf nodes from the queue. While removing the nodes, we also remove
            // the edges that are linked to the nodes. As a consequence, some of the non-leaf nodes would become leaf nodes.
            // And these are the nodes that would be trimmed out in the next iteration. We continue this until there are 2 or 1 nodes left.
            int leafNodeCount = leafNodeQueue.size();
            for (int i = 0; i < leafNodeCount; i++) {
                Integer leafNode = leafNodeQueue.poll();
                --remainingNodeCount;
                Integer neighborNode = graph.get(leafNode).iterator().next(); // Leaf node only has one neighbor node in the set, use iterator to get it
                graph.get(neighborNode).remove(leafNode); // Remove the leaf node from neighbor node's neighbors set
                if (graph.get(neighborNode).size() == 1)
                    // If any node becomes "leaf" node, insert it into the queue
                    leafNodeQueue.offer(neighborNode);
            }
        }
        return new ArrayList<>(leafNodeQueue);
    }

    /**
     * Accounts Merge
     * Given a list of accounts where each element accounts[i] is a list of strings, where the first element
     * accounts[i][0] is a name, and the rest of the elements are emails representing emails of the account.
     * <p>
     * Now, we would like to merge these accounts. Two accounts definitely belong to the same person if there
     * is some common email to both accounts. Note that even if two accounts have the same name, they may
     * belong to different people as people could have the same name. A person can have any number of accounts
     * initially, but all of their accounts definitely have the same name.
     * <p>
     * After merging the accounts, return the accounts in the following format: the first element of each
     * account is the name, and the rest of the elements are emails in sorted order. The accounts themselves
     * can be returned in any order.
     * <p>
     * Input: accounts = [["John","johnsmith@mail.com","john_newyork@mail.com"],["John","johnsmith@mail.com",
     * "john00@mail.com"],["Mary","mary@mail.com"],["John","johnnybravo@mail.com"]]
     * Output: [["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"],["Mary","mary@mail.com"],
     * ["John","johnnybravo@mail.com"]]
     * Explanation:
     * The first and second John's are the same person as they have the common email "johnsmith@mail.com".
     * The third John and Mary are different people as none of their email addresses are used by other accounts.
     * We could return these lists in any order, for example the answer [['Mary', 'mary@mail.com'],
     * ['John', 'johnnybravo@mail.com'], ['John', 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com']]
     * would still be accepted.
     * <p>
     * Input: accounts = [["Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"],["Kevin","Kevin3@m.co","Kevin5@m.co",
     * "Kevin0@m.co"],["Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"],["Hanzo","Hanzo3@m.co","Hanzo1@m.co",
     * "Hanzo0@m.co"],["Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"]]
     * Output: [["Ethan","Ethan0@m.co","Ethan4@m.co","Ethan5@m.co"],["Gabe","Gabe0@m.co","Gabe1@m.co","Gabe3@m.co"],
     * ["Hanzo","Hanzo0@m.co","Hanzo1@m.co","Hanzo3@m.co"],["Kevin","Kevin0@m.co","Kevin3@m.co","Kevin5@m.co"],
     * ["Fern","Fern0@m.co","Fern1@m.co","Fern5@m.co"]]
     * <p>
     * https://leetcode.com/problems/accounts-merge/description/
     */
    @Test
    void testAccountsMerge() {
        List<String> acct1 = List.of("John", "johnsmith@mail.com", "john_newyork@mail.com");
        List<String> acct2 = List.of("John", "johnsmith@mail.com", "john00@mail.com");
        List<String> acct3 = List.of("Mary", "mary@mail.com");
        List<String> acct4 = List.of("John", "johnnybravo@mail.com");
        List<List<String>> input = List.of(acct1, acct2, acct3, acct4);
        Assertions.assertThat(accountsMergeDFS(input)).containsOnly(List.of("John", "john00@mail.com", "john_newyork@mail.com", "johnsmith@mail.com"), List.of("Mary", "mary@mail.com"), List.of("John", "johnnybravo@mail.com"));
        Assertions.assertThat(accountsMergeDSU(input)).containsOnly(List.of("John", "john00@mail.com", "john_newyork@mail.com", "johnsmith@mail.com"), List.of("Mary", "mary@mail.com"), List.of("John", "johnnybravo@mail.com"));
    }

    HashSet<String> visited = new HashSet<>();
    // undirected graph (email -> emails) The connected email nodes means they belong to the same person
    Map<String, List<String>> graph = new HashMap<>();

    List<List<String>> accountsMergeDFS(List<List<String>> accountList) {
        for (List<String> account : accountList) {
            int accountSize = account.size();

            // Building adjacency list for the graph: virtually merging the account
            // Adding edge between first email to all other emails in the account. We treat it as undirected graph, so edge will be added on both emails
            // If account only has one email, it will NOT be added to the graph. It will always be included in the final output if there is no
            // common email in other account. If other account has common email and two or more emails, that will be added to the graph, so when
            // we do DFS, other emails will be found when we use the common email to look up in the graph(map)
            String accountFirstEmail = account.get(1);
            for (int j = 2; j < accountSize; j++) {
                // we connect emails in an account in a STAR manner with the first email as the internal node of the star and all
                // other emails as the leaves. All emails in the original account are connected for sure. After connecting an email
                // to a second account, that email will have one edge going to an email in the first account and one edge going to
                // an email in the second account. Thereby automatically merging the two accounts.
                String accountEmail = account.get(j);
                // Add the edge on the first email side
                graph.computeIfAbsent(accountFirstEmail, k -> new ArrayList<>()).add(accountEmail);
                // Add the edge on the other email side, so now two email nodes are connected
                graph.computeIfAbsent(accountEmail, k -> new ArrayList<>()).add(accountFirstEmail);
            }
        }

        List<List<String>> mergedAccounts = new ArrayList<>();
        for (List<String> account : accountList) {
            String accountName = account.get(0);
            String accountFirstEmail = account.get(1); //This is our internal node in the star graph

            // We iterate the original account list here, so we need to first check the visited set,
            // cuz the account may have been merged in the graph and visited already earlier
            if (!visited.contains(accountFirstEmail)) {
                List<String> mergedAccountEmails = new ArrayList<>();
                // Adding account name at the 0th index
                mergedAccountEmails.add(accountName);
                // Start traversal from this email node in the graph and visit other connected email nodes
                traversalConnectedEmailNode(mergedAccountEmails, accountFirstEmail);
                Collections.sort(mergedAccountEmails.subList(1, mergedAccountEmails.size()));
                mergedAccounts.add(mergedAccountEmails);
            }
        }
        return mergedAccounts;
    }

    private void traversalConnectedEmailNode(List<String> accountEmails, String email) {
        visited.add(email);
        // Add the email to account email list
        accountEmails.add(email);

        if (!graph.containsKey(email)) {
            // This condition is for the account having only one email amd also no common email found in other accounts
            // This kind of account email was NOT added to the graph at the first place by design
            return;
        }
        // Traversal all connected email nodes in this account in DFS way
        for (String neighbor : graph.get(email)) {
            if (!visited.contains(neighbor)) {
                traversalConnectedEmailNode(accountEmails, neighbor);
            }
        }
    }


    /**
     * Use DSU to union the account w/ common email then generate the output from the componenets in DSU
     * <p>
     * Any problem that involves merging connected components (accounts) is a natural fit for the
     * Disjoint Set Union (DSU) data structure. Since most implementations of DSU use an array to record the
     * root (representative) of each component, we will use integers to represent each component for ease of
     * operation. Therefore, we will give each account a unique ID, and we will map all the emails in the
     * account to the account's ID.
     * <p>
     * Algo:
     * 1. Build the emailToAccountId map and DSU
     * Traverse over each account, and for each account, traverse over all of its emails. If we see an email
     * for the first time, create an entry (email -> index of the current account as the account ID)
     * Otherwise, if the email has already been seen in another account, then we will union the current account
     * and the other account.
     * <p>
     * 2. Build the accountRepIdToEmailSet map
     * Traverse over every email once more. Each email will be added to accountRepIdToEmailSet map where the
     * key is the email's representative node in DSU, and the value is a list of emails with that representative.
     * <p>
     * 3. Traverse over the accountRepIdToEmailSet map we just built. Since the emails must be "in sorted order"
     * we will sort the list of emails for each account. Lastly, we can get the account name using the
     * accountList[group][0]. In accordance with the instructions, we will insert this name at the beginning
     * of the email list.
     * <p>
     * 4. Store the list created in step 4 in our final result (mergedAccount).
     * <p>
     * Time complexity: O(NK⋅logNK), N is the number of accounts and K is the maximum length of an account.
     * While merging we consider the size of each connected component and we always choose the representative
     * of the larger component to be the new representative of the smaller component, also we have included
     * the path compression so the time complexity for find/union operation is α(N) (Here, α(N) is the
     * inverse Ackermann function that grows so slowly, that it doesn't exceed 4 for all reasonable N
     * (approximately N<10^600 N).
     * <p>
     * We find the representative of all the emails, hence it will take O(NKα(N)) time. We are also sorting the
     * components and the worst case will be when all emails end up belonging to the same component this will
     * cost O(NK(logNK)).
     * <p>
     * Hence the total time complexity is O(NK⋅logNK+NK⋅α(N)).
     * <p>
     * Space complexity: O(NK)
     * <p>
     * List representative, size in DSU store information corresponding to each account so will take O(N) space.
     * All emails get stored in emailToAccountId and accountRepIdToEmailSet hence space used is O(NK).
     * <p>
     * The space complexity of the sorting algorithm depends on the implementation of each programming language.
     * For instance, in Java, Collections.sort() dumps the specified list into an array this will take O(NK) space
     */
    static class AccountDSU {
        int[] representative;
        int[] size;

        AccountDSU(int sz) {
            representative = new int[sz];
            size = new int[sz];

            for (int i = 0; i < sz; ++i) {
                // Initially each group is its own representative
                representative[i] = i;
                // Initialize the size of all groups to 1
                size[i] = 1;
            }
        }

        // Finds the representative of group x
        public int findRepresentative(int x) {
            if (x == representative[x]) {
                return x;
            }

            // This is path compression
            return representative[x] = findRepresentative(representative[x]);
        }

        // Unite the group that contains "a" with the group that contains "b"
        public void unionBySize(int a, int b) {
            int representativeA = findRepresentative(a);
            int representativeB = findRepresentative(b);

            // Union by size: point the representative of the smaller
            // group to the representative of the larger group.
            if (representativeA != representativeB) {
                if (size[representativeA] >= size[representativeB]) {
                    size[representativeA] += size[representativeB];
                    representative[representativeB] = representativeA;
                } else {
                    size[representativeB] += size[representativeA];
                    representative[representativeA] = representativeB;
                }
            }
        }
    }

    public List<List<String>> accountsMergeDSU(List<List<String>> accountList) {
        int accountListSize = accountList.size();
        AccountDSU dsu = new AccountDSU(accountListSize); // init the component size as the number of accounts, every account is a component

        // 1. Build the association of email and account/component id
        // 2. Construct the DSU and union the components w/ the same email.
        Map<String, Integer> emailToAccountId = new HashMap<>();
        for (int i = 0; i < accountListSize; i++) {
            int accountSize = accountList.get(i).size();
            for (int j = 1; j < accountSize; j++) {
                String email = accountList.get(i).get(j);

                // If this is the first time seeing this email then use the current index of accountList as the accountId(component ID in DSU)
                if (!emailToAccountId.containsKey(email)) {
                    emailToAccountId.put(email, i);
                } else {
                    // If we have seen this email before then union this account with the previous account of the email
                    // This effectively merge two components/accounts w/ the common email
                    dsu.unionBySize(i, emailToAccountId.get(email));
                }
            }
        }

        // Now iterate every email and query the DSU to get the representative/root id, then build the email list for each component/account
        Map<Integer, List<String>> accountRepIdToEmailSet = new HashMap<>();
        for (String email : emailToAccountId.keySet()) {
            int id = emailToAccountId.get(email);
            int repId = dsu.findRepresentative(id);
            accountRepIdToEmailSet.computeIfAbsent(repId, k -> new ArrayList<>()).add(email);
        }

        // Sort the accountRepIdToEmailSet and add the account name for the final output
        List<List<String>> mergedAccounts = new ArrayList<>();
        accountRepIdToEmailSet.forEach((aId, emails) -> {
                    Collections.sort(emails);
                    emails.add(0, accountList.get(aId).get(0));// Prepend account name
                    mergedAccounts.add(emails);
                }
        );
        return mergedAccounts;
    }

    /**
     * Redundant Connection
     * In this problem, a tree is an undirected graph that is connected and has no cycles.
     * <p>
     * You are given a graph that started as a tree with n nodes labeled from 1 to n, with
     * one additional edge added. The added edge has two different vertices chosen from 1
     * to n, and was not an edge that already existed. The graph is represented as an array
     * edges of length n where edges[i] = [ai, bi] indicates that there is an edge between
     * nodes ai and bi in the graph.
     * <p>
     * Return an edge that can be removed so that the resulting graph is a tree of n nodes.
     * If there are multiple answers, return the answer that occurs last in the input.
     * <p>
     * Input: edges = [[1,2],[1,3],[2,3]]
     * Output: [2,3]
     * <p>
     * Input: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
     * Output: [1,4]
     * <p>
     * https://leetcode.com/problems/redundant-connection/description/
     */
    @Test
    void testFindRedundantConnection() {
        int[][] edges = {
                {1, 2},
                {1, 3},
                {2, 3}
        };
        Assertions.assertThat(findRedundantConnection(edges)).containsExactly(2, 3);

    }

    private class RedundantConnectionDSU {
        private final int[] root;
        private final int[] rank;

        public RedundantConnectionDSU(int size) {
            root = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                root[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            int rootNode = x;
            while (rootNode != root[rootNode]) {
                rootNode = root[rootNode];
            }
            while (x != rootNode) {
                int next = root[x];
                root[x] = rootNode;
                x = next;
            }
            return rootNode;
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    root[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    root[rootX] = rootY;
                } else {
                    root[rootY] = rootX;
                    rank[rootX] += 1;
                }
            }
        }

        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }
    }

    /**
     * First create the DSU class implementing the find (w/ path compression), union (by rank),
     * and connected methods. Then iterate the edges array, if the two vertices at the edge is
     * not connected, union them using dsu. Otherwise, they are already connected, return this
     * edge.
     * <p>
     * Time Complexity: O(Nα(N))≈O(N), where N is the number of vertices
     * We make up to N queries of dsu.union, which takes (amortized) O(α(N)) time
     * Space Complexity: O(N).
     */
    public int[] findRedundantConnection(int[][] edges) {
        // use the max node number from constraint
        RedundantConnectionDSU dsu = new RedundantConnectionDSU(1001);
        for (int[] edge : edges) {
            if (!dsu.connected(edge[0], edge[1])) {
                dsu.union(edge[0], edge[1]);
            } else {
                return edge;
            }
        }
        return new int[0];
    }

    /**
     * Evaluate Division
     * You are given an array of variable pairs equations and an array of real numbers values, where
     * equations[i] = [Ai, Bi] and values[i] represent the equation Ai / Bi = values[i]. Each Ai or Bi
     * is a string that represents a single variable.
     * <p>
     * You are also given some queries, where queries[j] = [Cj, Dj] represents the jth query where you
     * must find the answer for Cj / Dj = ?.
     * <p>
     * Return the answers to all queries. If a single answer cannot be determined, return -1.0.
     * <p>
     * Note: The input is always valid. You may assume that evaluating the queries will not result in
     * division by zero and that there is no contradiction.
     * <p>
     * Note: The variables that do not occur in the list of equations are undefined, so the answer
     * cannot be determined for them.
     * <p>
     * Input: equations = [["a","b"],["b","c"]], values = [2.0,3.0], queries =
     * [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
     * Output: [6.00000,0.50000,-1.00000,1.00000,-1.00000]
     * <p>
     * Given: a / b = 2.0, b / c = 3.0
     * queries are: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
     * return: [6.0, 0.5, -1.0, 1.0, -1.0 ]
     * note: x is undefined => -1.0
     * <p>
     * Input: equations = [["a","b"],["b","c"],["bc","cd"]], values = [1.5,2.5,5.0], queries = [["a","c"],["c","b"],["bc","cd"],["cd","bc"]]
     * Output: [3.75000,0.40000,5.00000,0.20000]
     * <p>
     * https://leetcode.com/problems/evaluate-division/description/
     */
    @Test
    void testCalcEquation() {
        List<List<String>> eq = List.of(List.of("a", "b"), List.of("b", "c"), List.of("bc", "cd"));
        double[] values = {1.5, 2.5, 5.0};
        List<List<String>> queries = List.of(List.of("a", "c"), List.of("c", "b"), List.of("bc", "cd"), List.of("cd", "bc"), List.of("a", "a"), List.of("x", "x"));
        Assertions.assertThat(calcEquation(eq, values, queries)).containsExactly(3.75000, 0.40000, 5.00000, 0.20000, 1.00000, -1.00000);
    }

    /**
     * Treat variables as nodes in the directed graph, and the quotient is the weight of the directed edge
     * from one to the other. For each pair of nodes in the query, perform BFS/DFS traversal from one to
     * the other in the graph and accumulate the product of the weight of all edges in the path. If the
     * node or path doesn't exist, it will be -1 as result.
     * <p>
     * We could reformulate the equations with the graph data structure, where each variable can be
     * represented as a node in the graph, and the division relationship between variables can be modeled
     * as edge with direction and weight. The direction of edge indicates the order of division, and the
     * weight of edge indicates the result of division.
     * Ex, given a/b = 2, b/c = 3, we can have the graph
     * <p>
     * -    a/b=2       b/c=3
     * - a --------> b --------> c
     * -   <--------   -------->
     * -    b/a=1/2     c/b=1/3
     * <p>
     * We can reinterpret the problem as "given two nodes, we are asked to check if there exists a path
     * between them. If so, we should return the cumulative products along the path as the result.
     * Therefore, we can perform the BFS or DFS to search for the path from two given nodes. BFS is prob
     * better cuz it is better for finding shortest path.
     * <p>
     * Algo:
     * 1. Build the directed graph w/ edge weight from the list of equations
     * 2. Iterate the queries,
     * - First need to evaluate the special case such as either node isn't in the graph, and origin and
     * destination node are the same. and perform BFS
     * - Otherwise, perform BFS from origin to search over the connected nodes, and keep accumulating
     * the edge weight as we traversal.
     * <p>
     * Note: Another solution uses customized DSU/Union-Find implementation, check the LeetCode
     * <p>
     * Time Complexity: O(M⋅N), N be the number of input equations and M be the number of queries.
     * <p>
     * First of all, we iterate through the equations to build a graph. Each equation takes O(1) time
     * to process. Therefore, this step will take O(N) time in total.
     * <p>
     * For each query, we need to traverse the graph. In the worst case, we might need to traverse the
     * entire graph, which could take O(N).
     * Hence, in total, the evaluation of queries could take M⋅O(N)=O(M⋅N).
     * <p>
     * To sum up, the overall time complexity of the algorithm is O(N)+O(M⋅N)=O(M⋅N)
     * <p>
     * Space Complexity: O(N)
     */
    double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // Build the directed graph w/ edge weight
        // From equation node -> [To equation node -> weight]
        Map<String, Map<String, Double>> graph = new HashMap<>();
        for (int i = 0; i < equations.size(); i++) {
            List<String> equation = equations.get(i);
            String dividend = equation.get(0);
            String divisor = equation.get(1);
            double quotient = values[i];
            // Add the dividend --> divisor w/ edge weight, i.e. quotient
            graph.computeIfAbsent(dividend, k -> new HashMap<>()).put(divisor, quotient);
            // Add the divisor --> dividend w/ 1/edge weight, i.e. 1/quotient
            graph.computeIfAbsent(divisor, k -> new HashMap<>()).put(dividend, 1.0 / quotient);
        }
        double[] result = new double[queries.size()];
        Arrays.fill(result, -1.0); // Prefill array w/ -1. We need this cuz both nodes can be in the graph but not connected.
        for (int i = 0; i < queries.size(); i++) {
            List<String> query = queries.get(i);
            String dividend = query.get(0);
            String divisor = query.get(1);
            // Handle the special edge cases
            if (!graph.containsKey(dividend) || !graph.containsKey(divisor))
                result[i] = -1.0;
            else if (dividend.equals(divisor))
                result[i] = 1.0;
            else {
                // Perform BFS traversal from "dividend node" to search for "divisor node"
                // Each entry in the queue is the connected node and the accumulated product of the edge weight
                Queue<Pair<String, Double>> queue = new ArrayDeque<>();
                Set<String> visited = new HashSet<>();
                queue.offer(new Pair<>(dividend, 1.0)); // init the product to 1
                while (!queue.isEmpty()) {
                    Pair<String, Double> pair = queue.poll();
                    String node = pair.getKey();
                    Double accProduct = pair.getValue();
                    visited.add(node);
                    if (node.equals(divisor)) {
                        // Found the target, the accumulated product is the answer from the source to here
                        result[i] = accProduct;
                        break;
                    }
                    graph.get(node).forEach((neighbor, edgeWeight) -> {
                        if (!visited.contains(neighbor)) {
                            // Put the neighbor node with the accumulated product of the edge weight to the queue
                            queue.offer(new Pair<>(neighbor, accProduct * edgeWeight));
                        }
                    });
                }
            }
        }
        return result;
    }

    /**
     * Word Ladder
     * A transformation sequence from word beginWord to word endWord using a dictionary wordList is a sequence
     * of words beginWord -> s1 -> s2 -> ... -> sk such that:
     * <p>
     * Every adjacent pair of words differs by a single letter.
     * Every si for 1 <= i <= k is in wordList. Note that beginWord does not need to be in wordList.
     * sk == endWord
     * Given two words, beginWord and endWord, and a dictionary wordList, return the number of words in the
     * shortest transformation sequence from beginWord to endWord, or 0 if no such sequence exists.
     * <p>
     * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
     * Output: 5
     * Explanation: One shortest transformation sequence is "hit" -> "hot" -> "dot" -> "dog" -> cog",
     * which is 5 words long.
     * <p>
     * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
     * Output: 0
     * Explanation: The endWord "cog" is not in wordList, therefore there is no valid transformation sequence.
     * <p>
     * https://leetcode.com/problems/word-ladder/description/
     */
    @Test
    void testLadderLength() {
        List<String> wordList = List.of("hot", "dot", "dog", "lot", "log", "cog");
        Assertions.assertThat(ladderLength("hit", "cog", wordList)).isEqualTo(5);
        Assertions.assertThat(ladderLength2("hit", "cog", wordList)).isEqualTo(5);
    }

    /**
     * Make each word as a node in an undirected graph and edges between words which differ by just one letter,
     * then perform BFS from start word to the end word by level.
     * <p>
     * The most complex part is the logic to find the words in the wordList that has only one char difference
     * from the given word.
     * When we perform BFS, this is basically to find the neighboring nodes to visit at next level of BFS.
     * <p>
     * The helper method, getNeighborsFromWordSet, it takes the current word and the unvisited word set,
     * then iterate each char in the current word and create a new string by iteratively replacing this char
     * with char in [a-z] to see if such string exists in the unvisited word set. If so, put it in the list
     * and return them in the end.
     * <p>
     * There is another implementation to build a cutomized adjacent list up front, so we can find the neighboring
     * word node differ by one letter by look up the map, but this is slower and consume more memory. Check
     * the source code and LeetCode
     * <p>
     * Time Complexity: O(M^2⋅N), where M is the length of each word, and N is the length word list.
     * getNeighborsFromWordSet takes O(M^2) for each word and it is called by N times.
     * <p>
     * Space Complexity: O(M*N) for the set
     */
    int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Queue<String> queue = new ArrayDeque<>();
        // Instead of visited set, we create the set from wordList to track remaining available words, and pass it
        // to the getNeighborsFromWordSet method to determine the neighbor nodes, i.e. differ 1 char from the current node.
        Set<String> unvisitedWords = new HashSet<>(wordList);
        int level = 0;
        queue.offer(beginWord);
        while (!queue.isEmpty()) {
            int qSize = queue.size(); // Doing BFS level by level
            level++;
            for (int i = 0; i < qSize; i++) {
                String word = queue.poll();
                if (endWord.equals(word)) // Found the target word
                    return level;
                List<String> neighbors = getNeighborsFromWordSet(word, unvisitedWords);
                for (String neighbor : neighbors) {
                    unvisitedWords.remove(neighbor); // Remove the word, so it won't be picked up and visited again
                    queue.offer(neighbor);
                }
            }
        }
        return 0;
    }

    List<String> getNeighborsFromWordSet(String word, Set<String> words) {
        char[] chars = word.toCharArray();
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            // Iterate each char and create a new string by iteratively replacing this char with [a-z]
            // and take the one also existing in the words set
            char orig = chars[i]; // copy of the original char
            for (int c = 'a'; c <= 'z'; c++) {
                chars[i] = (char) c;
                String neighbor = new String(chars);
                if (words.contains(neighbor))
                    result.add(neighbor);
            }
            chars[i] = orig; // restore to original char
        }
        return result;
    }

    /**
     * Slower and consume more memory than the above implementation(It creates lots of wordPattern entry in the map)
     * Time Complexity: O(M^2⋅N), where M is the length of each word, and N is the length word list.
     * Space Complexity: O(M^2⋅N)
     */
    int ladderLength2(String beginWord, String endWord, List<String> wordList) {
        // Build the graph(adjacent list)
        // Word pattern w/ wildcard on one char --> words matching the pattern
        // Ex, for the hot in the wordList at test case, we generate (ho* --> [hot]), (h*t --> [hot]), (*ot --> [hot, dot, lot]) in the map
        Map<String, List<String>> wordPatternToWords = new HashMap<>();
        BiFunction<String, Integer, String> toWordPattern = (w, i) -> w.substring(0, i) + "*" + w.substring(i + 1); // Tricky!
        for (String word : wordList) {
            for (int i = 0; i < word.length(); i++) {
                String wordPattern = toWordPattern.apply(word, i);
                // computeIfAbsent is more efficient than putIfAbsent cuz new List is not always created up front
                wordPatternToWords.computeIfAbsent(wordPattern, k -> new ArrayList<>()).add(word);
            }
        }
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        int level = 0;
        queue.offer(beginWord);
        visited.add(beginWord);
        while (!queue.isEmpty()) {
            int qSize = queue.size();
            level++;
            for (int i = 0; i < qSize; i++) {
                String word = queue.poll();
                visited.add(word);
                if (endWord.equals(word)) // Found the target word
                    return level;
                for (int j = 0; j < word.length(); j++) {
                    String wordPattern = toWordPattern.apply(word, j);
                    if (wordPatternToWords.containsKey(wordPattern)) {
                        for (String neighborWord : wordPatternToWords.get(wordPattern)) {
                            if (!visited.contains(neighborWord)) {
                                queue.offer(neighborWord);
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Possible Bipartition
     * We want to split a group of n people (labeled from 1 to n) into two groups of any size.
     * Each person may dislike some other people, and they should not go into the same group.
     * <p>
     * Given the integer n and the array dislikes where dislikes[i] = [ai, bi] indicates that
     * the person labeled ai does not like the person labeled bi, return true if it is possible
     * to split everyone into two groups in this way.
     * <p>
     * Input: n = 4, dislikes = [[1,2],[1,3],[2,4]]
     * Output: true
     * Explanation: The first group has [1,4], and the second group has [2,3].
     * <p>
     * Input: n = 3, dislikes = [[1,2],[1,3],[2,3]]
     * Output: false
     * Explanation: We need at least 3 groups to divide them. We cannot put them in two groups.
     * <p>
     * https://leetcode.com/problems/possible-bipartition/description/
     */
    @Test
    void testPossibleBipartition() {
        int[][] dislikes = {
                {1, 2},
                {1, 3},
                {2, 4}
        };
        Assertions.assertThat(possibleBipartition(4, dislikes)).isTrue();
        dislikes = new int[][]{
                {1, 2},
                {1, 3},
                {2, 3}
        };
        Assertions.assertThat(possibleBipartition(3, dislikes)).isFalse();
    }

    static final int NONE = -1, RED = 0, BLUE = 1;

    /**
     * First build the adjacent list Map for the graph to store all neighbors of each node.
     * Then create an int (n+1)-length array (init: -1) to store the color for all nodes.
     * {-1, RED(0), BLUE(1)}. Start to iterate the every node having color == -1. For each
     * node, we mark it RED in the array first and add it to the queue, then start BFS.
     * While the queue is not empty, we poll the queue and get the color of the current node.
     * Then we iterate its neighbor nodes, if any neighbor has the same color as the current
     * node, it means we have a cycle. Return false.
     * Otherwise, if the neighbor is not colored yet, i.e. -1, we mark it using the opposite
     * color and add it to the queue.
     * <p>
     * Observation:
     * 1. The problem asks us to divide the given people into two groups such that no two
     * people in the same group dislike each other. We can represent this problem in the
     * form of a graph, with people being the nodes and disliked pairs being the edges.
     * Our task is to figure out whether we can divide the nodes into two sets such that
     * there aren't any edges between nodes of the same set.
     * <p>
     * 2. This is the classic bipartite graph algorithm. If a graph is bipartite, then it
     * can't contain a odd-length cycle.
     * Ex: A -> B, A -> C, B -> C.
     * A, B, C form an odd-length cycle, so the graph can't be bipartite.
     * <p>
     * 3. We use two colors and start BFS traversal from every un-colored node in the graph.
     * For each level, we use the opposite color of the current node to mark its neighboring
     * nodes then put them in the queue. If at any point we see any neighbor node has the
     * same color as current node, it means we have a cycle, which means these two nodes has
     * the same parent, so they were marked the same color at the previous BFS level.
     * And there must be an edge between these two nodes, so the other node was added to the
     * queue earlier. Therefore, the graph can't be bipartite
     * <p>
     * <p>
     * Time complexity: O(V+E)
     * Let E be the size of dislikes and V be the number of people.
     * Each node is only queued once, which takes O(1) time for each node.
     * We also iterate over the edges of every node once (since we only visit each node once,
     * we won't iterate over a node's edges multiple times), which adds O(E) time.
     * We also need O(E) to initialize the adjacency list and O(V) to initialize the color array.
     * <p>
     * Space complexity: O(V+E)
     */
    boolean possibleBipartition(int n, int[][] dislikes) {
        // Build the adjacent list, cuz this is undirected graph, we need to add edges(neighbor node) on both end
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] dislike : dislikes) {
            int node1 = dislike[0];
            int node2 = dislike[1];
            graph.computeIfAbsent(node1, k -> new ArrayList<>()).add(node2);
            graph.computeIfAbsent(node2, k -> new ArrayList<>()).add(node1);
        }
        // Use nodeColor array to store the color of each node
        int[] nodeColor = new int[n + 1];
        Arrays.fill(nodeColor, NONE);
        // Check every unmarked node in the graph
        for (Integer node : graph.keySet()) {
            if (nodeColor[node] == NONE) {
                if (!colorNodesInGraph(node, graph, nodeColor))
                    return false;
            }
        }
        return true;
    }

    private boolean colorNodesInGraph(int startNode, Map<Integer, List<Integer>> graph, int[] nodeColor) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(startNode);
        nodeColor[startNode] = RED;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            int currentNodeColor = nodeColor[node];
            for (Integer neighbor : graph.get(node)) {
                if (nodeColor[neighbor] == currentNodeColor) {
                    // If the neighbor has the same color as the current node, it means we have a cycle, which means
                    // these two nodes has the same parent, so they were marked the same color at the previous BFS level.
                    // And there must be an edge between these two nodes, so the other node was added to the queue.
                    // Ex: A -> B, A -> C, B -> C.
                    // A, B, C form an odd-length cycle, so the graph can't be bipartite.
                    return false;
                }
                // If the neighbor node is not colored yet, color it and add it to the queue
                int nextColor = currentNodeColor == RED ? BLUE : RED;
                if (nodeColor[neighbor] == NONE) {
                    nodeColor[neighbor] = nextColor;
                    queue.offer(neighbor);
                }
            }
        }
        return true;
    }

    /**
     * Is Graph Bipartite?
     * There is an undirected graph with n nodes, where each node is numbered between 0 and n - 1.
     * You are given a 2D array graph, where graph[u] is an array of nodes that node u is adjacent to.
     * More formally, for each v in graph[u], there is an undirected edge between node u and node v.
     * The graph has the following properties:
     * <p>
     * There are no self-edges (graph[u] does not contain u).
     * There are no parallel edges (graph[u] does not contain duplicate values).
     * If v is in graph[u], then u is in graph[v] (the graph is undirected).
     * The graph may not be connected, meaning there may be two nodes u and v such that there is
     * no path between them.
     * A graph is bipartite if the nodes can be partitioned into two independent sets A and B
     * such that every edge in the graph connects a node in set A and a node in set B.
     * <p>
     * Return true if and only if it is bipartite.
     * <p>
     * Input: graph = [[1,2,3],[0,2],[0,1,3],[0,2]]
     * Output: false
     * Explanation: There is no way to partition the nodes into two independent sets such that every
     * edge connects a node in one and a node in the other.
     * <p>
     * Input: graph = [[1,3],[0,2],[1,3],[0,2]]
     * Output: true
     * Explanation: We can partition the nodes into two sets: {0, 2} and {1, 3}.
     * https://leetcode.com/problems/is-graph-bipartite/description/
     */
    @Test
    void testIsBipartite() {
        int[][] graph = {
                {1, 2, 3},
                {0, 2},
                {0, 1, 3},
                {0, 2}
        };
        Assertions.assertThat(isBipartite(graph)).isFalse();
        graph = new int[][]{
                {1, 3},
                {0, 2},
                {1, 3},
                {0, 2}
        };
        Assertions.assertThat(isBipartite(graph)).isTrue();
    }

    /**
     * Same implementation as the problem "Possible Bipartition", but this one the adjacent
     * list of the graph is already given.
     */
    boolean isBipartite(int[][] graph) {
        int[] nodeColor = new int[graph.length];
        Arrays.fill(nodeColor, NONE);
        for (int i = 0; i < nodeColor.length; i++) {
            if (nodeColor[i] == NONE) {
                if (!colorNode(i, nodeColor, graph)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean colorNode(int startNode, int[] nodeColor, int[][] graph) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(startNode);
        nodeColor[startNode] = RED;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            int currentNodeColor = nodeColor[node];
            int nextColor = currentNodeColor == RED ? BLUE : RED;
            for (int i = 0; i < graph[node].length; i++) {
                int neighborNode = graph[node][i];
                if (currentNodeColor == nodeColor[neighborNode]) {
                    return false;
                }
                if (nodeColor[neighborNode] == NONE) {
                    queue.offer(neighborNode);
                    nodeColor[neighborNode] = nextColor;
                }
            }
        }
        return true;
    }

    /**
     * Cheapest Flights Within K Stops
     * There are n cities connected by some number of flights. You are given an array flights where
     * flights[i] = [fromi, toi, pricei] indicates that there is a flight from city fromi to city
     * toi with cost pricei.
     * <p>
     * You are also given three integers src, dst, and k, return the cheapest price from src to dst
     * with at most k stops. If there is no such route, return -1.
     * <p>
     * Input: n = 4, flights = [[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]], src = 0,
     * dst = 3, k = 1
     * Output: 700
     * Explanation:
     * The graph is shown above.
     * The optimal path with at most 1 stop from city 0 to 3 is marked in red and has cost 100 +
     * 600 = 700.
     * Note that the path through cities [0,1,2,3] is cheaper but is invalid because it uses 2 stops.
     * <p>
     * Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 1
     * Output: 200
     * Explanation:
     * The graph is shown above.
     * The optimal path with at most 1 stop from city 0 to 2 is marked in red and has cost 100
     * + 100 = 200.
     * <p>
     * Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 0
     * Output: 500
     * Explanation:
     * The graph is shown above.
     * The optimal path with no stops from city 0 to 2 is marked in red and has cost 500.
     * <p>
     * https://leetcode.com/problems/cheapest-flights-within-k-stops/description/
     */
    @Test
    void testFindCheapestPrice() {
        int[][] flights = {
                {0, 1, 100},
                {1, 2, 100},
                {2, 0, 100},
                {1, 3, 600},
                {2, 3, 200}
        };
        Assertions.assertThat(findCheapestPrice(4, flights, 0, 3, 1)).isEqualTo(700);
        Assertions.assertThat(findCheapestPriceBellmanFord(4, flights, 0, 3, 1)).isEqualTo(700);
    }

    /**
     * First build the Map<Integer, List<int[]>>for the graph, cityToCityFlightFare.
     * We use an int[n] array to track the minCostFromSourceToCity, prefilled w/ INT_MAX
     * Start the BFS by level ops, we stop the loop when we explored more than k layers.
     * The queue contains the Pair(city, minCostToCity). For each pair polled from the
     * queue, we iterate its flightToNextCity from the map. if the cost of the current
     * city + the flight cost to the next city >= minCostFromSourceToCity[nextCity],
     * we skip it. Otherwise, we update minCostFromSourceToCity[nextCity] to this cost
     * and add this pair to the queue. Finally, return minCostFromSourceToCity[dst]
     * or -1.
     * <p>
     * Time complexity: O(N+E⋅K)
     * Let E be the number of flights and N be the number of cities.
     * O(N) to initialize the array
     * O(E) to initialize the map
     * Max number of times an edge can be processed is limited by K cuz that is the
     * number of levels. In the worst case, O(E⋅K)
     * <p>
     * Space complexity: O(N+E⋅K)
     */
    int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        Map<Integer, List<int[]>> cityToCityFlightFare = new HashMap<>();
        for (int[] i : flights)
            cityToCityFlightFare.computeIfAbsent(i[0], value -> new ArrayList<>()).add(new int[]{i[1], i[2]});

        int[] minCostFromSourceToCity = new int[n];
        Arrays.fill(minCostFromSourceToCity, Integer.MAX_VALUE);
        Queue<Pair<Integer, Integer>> queue = new ArrayDeque<>(); // Pair(city, minCostToCity)
        queue.offer(new Pair<>(src, 0));
        int stops = 0;
        // We only do BFS for k-time layer
        while (stops <= k && !queue.isEmpty()) {
            int qSize = queue.size();
            // Iterate on current level.
            for (int i = 0; i < qSize; i++) {
                Pair<Integer, Integer> pair = queue.poll();
                Integer city = pair.getKey();
                Integer cost = pair.getValue(); // The current min cost from source to this city
                if (!cityToCityFlightFare.containsKey(city))
                    continue;
                for (int[] flight : cityToCityFlightFare.get(city)) {
                    int nextCity = flight[0];
                    int price = flight[1];
                    if (cost + price >= minCostFromSourceToCity[nextCity])
                        // If the cost of taking this flight to the next city is greater than other route to it,
                        // skip this flight. W/O this part, we will get TLE on LeetCode
                        continue;
                    // update the min cost to the next city
                    minCostFromSourceToCity[nextCity] = cost + price;
                    queue.offer(new Pair<>(nextCity, minCostFromSourceToCity[nextCity]));
                }
            }
            stops++;
        }
        return minCostFromSourceToCity[dst] == Integer.MAX_VALUE ? -1 : minCostFromSourceToCity[dst];
    }

    /**
     * Bellman Ford's algorithm
     * First build an int[n] array minCostFromSourceToCity, init to INT_MAX. We set
     * minCostFromSourceToCity[src] = 0. Then we start the loop for k+1 times.
     * In each iteration, make a copy of minCostFromSourceToCity named current and loop
     * over all the flights(edges).
     * For each flight, if minCostFromSourceToCity[fromCity] != Integer.MAX_VALUE,
     * We compute the min cost of this path to the city by taking the min of current min
     * cost to it and previous min cost to fromCity + this flight cost. Then we update
     * current[toCity] to it.
     * We update the minCostFromSourceToCity to the current array at the end of each
     * iteration.
     * Finally, eturn minCostFromSourceToCity[dst] or -1.
     * <p>
     * Bellman Ford's algorithm is used to find the shortest paths from the source node
     * to all other vertices in a weighted graph. It depends on the idea that the
     * shortest path contains at most N - 1 edges (where N is the number of nodes in
     * the graph) because the shortest path cannot have a cycle.
     * <p>
     * This algorithm takes as input a directed weighted graph and a starting node.
     * It produces all the shortest paths from the starting node to all other vertices.
     * It initially sets the distance from the starting node to all other vertices to
     * infinity. The distance of the starting node is set to 0. The algorithm loops
     * through each edge N - 1 times. If it finds an edge through which the distance of
     * a node is smaller than the previously stored value, it uses this edge and stores
     * the new value. This is called relaxing an edge.
     * <p>
     * Time complexity: O((N+E)⋅K)
     * We are iterating over all the edges K+1 times which takes O(E⋅K). At the start
     * and end of each iteration, we also swap minCostFromSourceToCity arrays, which
     * take O(N⋅K) time for all the iterations. This gives us a time complexity of
     * O((N+E)⋅K)
     * <p>
     * Space complexity: O(N)
     */
    int findCheapestPriceBellmanFord(int n, int[][] flights, int src, int dst, int k) {
        // Min cost/distance from source to all other nodes.
        int[] minCostFromSourceToCity = new int[n];
        Arrays.fill(minCostFromSourceToCity, Integer.MAX_VALUE);
        minCostFromSourceToCity[src] = 0;

        // Run only K+1 times since we want the shortest distance in K hops, i.e. K+1 edges
        for (int i = 0; i <= k; i++) {
            // Create a copy of minCostFromSourceToCity array.
            int[] current = Arrays.copyOf(minCostFromSourceToCity, n);
            for (int[] flight : flights) { // relaxation process on each edge
                int fromCity = flight[0];
                int toCity = flight[1];
                int cost = flight[2];
                // When minCostFromSourceToCity[fromCity] == Integer.MAX_VALUE, it means we haven't visited any edges
                // that go thru this node yet. So we can't determine the cost. Therefore, skip it for this iteration
                if (minCostFromSourceToCity[fromCity] != Integer.MAX_VALUE) {
                    // There is a visited edge thru fromCity node, so we can compute the min cost of this path to
                    // the city by taking the min of current min cost to it and previous min cost to from city + this
                    // flight cost
                    current[toCity] = Math.min(current[toCity], minCostFromSourceToCity[fromCity] + cost);
                }
            }
            // Update the minCostFromSourceToCity to current array
            minCostFromSourceToCity = current;
        }
        return minCostFromSourceToCity[dst] == Integer.MAX_VALUE ? -1 : minCostFromSourceToCity[dst];
    }

    /**
     * Number of Connected Components in an Undirected
     * You have a graph of n nodes. You are given an integer n and an array edges where
     * edges[i] = [ai, bi] indicates that there is an edge between ai and bi in the graph.
     * <p>
     * Return the number of connected components in the graph.
     * <p>
     * Input: n = 5, edges = [[0,1],[1,2],[3,4]]
     * Output: 2
     * <p>
     * Input: n = 5, edges = [[0,1],[1,2],[2,3],[3,4]]
     * Output: 1
     * <p>
     * https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/description/
     */
    @Test
    void testCountComponents() {
        int[][] edges = {
                {0, 1},
                {1, 2},
                {3, 4}
        };
        Assertions.assertThat(countComponents(5, edges)).isEqualTo(2);
        edges = new int[][]{
                {3, 1},
                {1, 2},
                {3, 2}
        };
        Assertions.assertThat(countComponents(4, edges)).isEqualTo(2);
        edges = new int[][]{
                {0, 1},
                {1, 2},
                {3, 4},
                {3, 2}
        };
        Assertions.assertThat(countComponents(5, edges)).isEqualTo(1);
    }

    /**
     * Create the adjacent list map for n nodes and given edges. Maintain a visited set then
     * iterate the node from 0...n-1. For each node, if not visited, put it into the queue
     * and start BFS to mark traversed node visited. After each BFS traversal is done,
     * increment the component count.
     * <p>
     * * Can also be implemented in DFS
     * <p>
     * Time complexity: O(E+V)
     * <p>
     * Space complexity: O(E+V)
     */
    int countComponents(int n, int[][] edges) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        // Not every node is connected with edge, so we need to create all nodes in the graph
        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        Set<Integer> visited = new HashSet<>();
        int count = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            // iterate every unvisited node and start BFS to mark nodes visited
            if (!visited.contains(i)) {
                queue.offer(i);
                visited.add(i);
                while (!queue.isEmpty()) {
                    Integer currentNode = queue.poll();
                    for (Integer neighbor : graph.get(currentNode)) {
                        if (!visited.contains(neighbor)) {
                            queue.offer(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }
                // Increment the count after finish one BFS traversal from one node
                count++;
            }
        }
        return count;
    }

    /**
     * Pacific Atlantic Water Flow
     * There is an m x n rectangular island that borders both the Pacific Ocean and Atlantic Ocean.
     * The Pacific Ocean touches the island's left and top edges, and the Atlantic Ocean touches
     * the island's right and bottom edges.
     * <p>
     * The island is partitioned into a grid of square cells. You are given an m x n integer matrix
     * heights where heights[r][c] represents the height above sea level of the cell at coordinate
     * (r, c).
     * <p>
     * The island receives a lot of rain, and the rain water can flow to neighboring cells directly
     * north, south, east, and west if the neighboring cell's height is less than or equal to the
     * current cell's height. Water can flow from any cell adjacent to an ocean into the ocean.
     * <p>
     * Return a 2D list of grid coordinates result where result[i] = [ri, ci] denotes that rain
     * water can flow from cell (ri, ci) to both the Pacific and Atlantic oceans.
     * <p>
     * https://leetcode.com/problems/pacific-atlantic-water-flow/description/
     */
    @Test
    void testPacificAtlantic() {
        int[][] grid = {
                {1, 2, 2, 3, 5},
                {3, 2, 3, 4, 4},
                {2, 4, 5, 3, 1},
                {6, 7, 1, 4, 5},
                {5, 1, 1, 2, 4}
        };
        Assertions.assertThat(pacificAtlanticBFS(grid))
                .containsExactlyInAnyOrder(List.of(0, 4), List.of(1, 3), List.of(1, 4), List.of(2, 2), List.of(3, 0), List.of(3, 1), List.of(4, 0));
        Assertions.assertThat(pacificAtlanticDFS(grid))
                .containsExactlyInAnyOrder(List.of(0, 4), List.of(1, 3), List.of(1, 4), List.of(2, 2), List.of(3, 0), List.of(3, 1), List.of(4, 0));
    }

    /**
     * Use multi-source BFS traversal to start from all cells next to Pacific Ocean
     * (grid[0..m-1][0], grid[0][0..n-1]) and Atlantic Ocean (grid[0..m-1][n-1],
     * grid[m-1][0..n-1]). We need separate queues and visited boolean grids.
     * We put the aforementioned cells in the corresponding queues and mark them
     * visited. Then start two separate BFS using two queues separately to mark
     * the cells, however, only the neighbor cells greater or equal to the current
     * node should be added to the queue. Finally, iterate all cells in the grid
     * and if it is marked true on both visited grid, add to the result list.
     * <p>
     * * Can also be implemented in DFS
     */
    List<List<Integer>> pacificAtlanticBFS(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        int rowNum = heights.length;
        int colNum = heights[0].length;
        Queue<Pair<Integer, Integer>> pacificQ = new ArrayDeque<>();
        Queue<Pair<Integer, Integer>> atlanticQ = new ArrayDeque<>();
        boolean[][] pacificVisited = new boolean[rowNum][colNum];
        boolean[][] atlanticVisited = new boolean[rowNum][colNum];
        // Put all cells next to the pacific ocean in the queue and mark visited
        for (int i = 0; i < rowNum; i++) {
            pacificQ.offer(new Pair<>(i, 0));
            pacificVisited[i][0] = true;
            atlanticQ.offer(new Pair<>(i, colNum - 1));
            atlanticVisited[i][colNum - 1] = true;
        }
        // Put all cells next to the atlantic ocean in the queue and mark visited
        for (int i = 0; i < colNum; i++) {
            pacificQ.offer(new Pair<>(0, i));
            pacificVisited[0][i] = true;
            atlanticQ.offer(new Pair<>(rowNum - 1, i));
            atlanticVisited[rowNum - 1][i] = true;
        }
        // Start multi-source BFS to mark cells on both queues
        markCellsBFS(pacificQ, pacificVisited, rowNum, colNum, heights);
        markCellsBFS(atlanticQ, atlanticVisited, rowNum, colNum, heights);
        // Find the cells marked by both BFS
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if (pacificVisited[i][j] && atlanticVisited[i][j])
                    result.add(List.of(i, j));
            }
        }
        return result;
    }

    private void markCellsBFS(Queue<Pair<Integer, Integer>> queue, boolean[][] visited, int rowNum, int colNum, int[][] heights) {
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> cell = queue.poll();
            for (int[] dir : dirs) {
                int r = cell.getKey() + dir[0];
                int c = cell.getValue() + dir[1];
                if (r >= 0 && r < rowNum && c >= 0 && c < colNum && !visited[r][c]
                        && heights[r][c] >= heights[cell.getKey()][cell.getValue()]) {
                    queue.offer(new Pair<>(r, c));
                    visited[r][c] = true;
                }
            }
        }
    }

    List<List<Integer>> pacificAtlanticDFS(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        int rowNum = heights.length;
        int colNum = heights[0].length;
        boolean[][] pacificVisited = new boolean[rowNum][colNum];
        boolean[][] atlanticVisited = new boolean[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            markCellsDFS(i, 0, pacificVisited, rowNum, colNum, heights);
            markCellsDFS(i, colNum - 1, atlanticVisited, rowNum, colNum, heights);
        }
        // Put all cells next to the atlantic ocean in the queue and mark visited
        for (int i = 0; i < colNum; i++) {
            markCellsDFS(0, i, pacificVisited, rowNum, colNum, heights);
            markCellsDFS(rowNum - 1, i, atlanticVisited, rowNum, colNum, heights);
        }
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < colNum; j++) {
                if (pacificVisited[i][j] && atlanticVisited[i][j])
                    result.add(List.of(i, j));
            }
        }
        return result;
    }

    private void markCellsDFS(int row, int col, boolean[][] visited, int rowNum, int colNum, int[][] heights) {
        visited[row][col] = true;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] dir : dirs) {
            int r = row + dir[0];
            int c = col + dir[1];
            if (r >= 0 && r < rowNum && c >= 0 && c < colNum && !visited[r][c]
                    && heights[r][c] >= heights[row][col]) {
                markCellsDFS(r, c, visited, rowNum, colNum, heights);
            }
        }
    }
}
