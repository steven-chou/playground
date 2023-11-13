package playground;

import javafx.util.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Design {
    /**
     * Shuffle an Array
     * Design a class to provide methods
     * Solution(int[] nums) Initializes the object with the integer array nums.
     * int[] reset() Resets the array to its original configuration and returns it.
     * int[] shuffle() Returns a random shuffling of the array.
     * https://leetcode.com/problems/shuffle-an-array/editorial/
     */
    class Solution {
        private int[] array, original;
        Random rand = new Random();

        /**
         * Initializes the object with the integer array nums
         *
         * @param nums
         */
        Solution(int[] nums) {
            array = nums;
            original = nums.clone();
        }

        /**
         * Resets the array to its original configuration and returns it.
         *
         * @return
         */
        int[] reset() {
            array = original;
            original = original.clone();
            return original;
        }

        int randRange(int min, int max) {
            return rand.nextInt(max - min) + min;
        }

        void swapAt(int i, int j) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        /**
         * Returns a random shuffling of the array.
         * Implementation of the Fisher-Yates Algorithm.
         * On each iteration of the algorithm, we generate a random integer between the
         * current index and the last index of the array. Then, we swap the elements at the
         * current index and the chosen index - this simulates drawing (and removing) the element from the hat,
         * as the next range from which we select a random index will not include the most recently processed one
         * Time Complexity: O(n). Space Complexity: O(1)
         *
         * @return
         */
        int[] shuffle() {
            for (int i = 0; i < array.length; i++) {
                swapAt(i, randRange(i, array.length));
            }
            return array;
        }
    }

    /**
     * Min Stack
     * https://leetcode.com/problems/min-stack/description/
     * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
     * <p>
     * Implement the MinStack class:
     * <p>
     * MinStack() initializes the stack object.
     * void push(int val) pushes the element val onto the stack.
     * void pop() removes the element on the top of the stack.
     * int top() gets the top element of the stack.
     * int getMin() retrieves the minimum element in the stack.
     * You must implement a solution with O(1) time complexity for each function.
     * Space Complexity: O(n)
     */
    class MinStack {

        // Each entry in the stack is {value, currentMin}, so we can easily get the min in O(1)
        private final Stack<int[]> stack;

        public MinStack() {
            stack = new Stack<>();
        }

        public void push(int val) {
            if (stack.isEmpty()) {
                stack.push(new int[]{val, val});
                return;
            }
            int currentMin = stack.peek()[1];
            // Trick: All the minimum values are equal to either the minimum value immediately before, or the actual
            // stack value alongside, i.e. the value which is going to be push.
            stack.push(new int[]{val, Math.min(currentMin, val)});
        }

        public void pop() {
            stack.pop();
        }

        public int top() {
            return stack.peek()[0];
        }

        public int getMin() {
            return stack.peek()[1];
        }
    }

    @Test
    void testKthLargest() {
        KthLargest test = new KthLargest(1, new int[]{});
        test.add(-3);
        test.add(-2);
        test.add(-4);
        test.add(0);
        test.add(4);
    }

    /**
     * Kth Largest Element in a Stream
     * <p>
     * Design a class to find the kth largest element in a stream. Note that it is the kth largest
     * element in the sorted order, not the kth distinct element.
     * <p>
     * Implement KthLargest class:
     * <p>
     * KthLargest(int k, int[] nums)
     * - Initializes the object with the integer k and the stream of integers nums.
     * <p>
     * int add(int val)
     * - Appends the integer val to the stream and returns the element representing
     * the kth largest element in the stream.
     * <p>
     * https://leetcode.com/problems/kth-largest-element-in-a-stream/description/
     * <p>
     * Time complexity: O(N⋅log(N)+M⋅log(k))
     * <p>
     * The time complexity is split into two parts. First, the constructor needs to turn nums into
     * a heap of size k. In Python, heapq.heapify() can turn nums into a heap in O(N) time.
     * Then, we need to remove from the heap until there are only k elements in it, which means removing
     * N - k elements. Since k can be, say 1, in terms of big OOO this is N operations, with each operation
     * costing log(N). Therefore, the constructor costs O(N+N⋅log(N))=O(N⋅log(N)).
     * <p>
     * Next, every call to add() involves adding an element to heap and potentially removing an element
     * from heap. Since our heap is of size k, every call to add() at worst costs O(2∗log⁡(k))=O(log⁡(k)).
     * That means M calls to add() costs O(M⋅log(k)).
     * <p>
     * Space complexity: O(N)
     * <p>
     * The only extra space we use is the heap. While during add() calls we limit the size of the heap to k,
     * in the constructor we start by converting nums into a heap, which means the heap will initially be of size N.
     */
    class KthLargest {
        private PriorityQueue<Integer> minHeap;
        private int k;

        public KthLargest(int k, int[] nums) {
            // PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
            minHeap = new PriorityQueue<>(); // default is ascending and min is always on top, which means it is Min heap
            this.k = k;
            for (int num : nums) {
                minHeap.offer(num);
            }
            while (minHeap.size() > k)
                minHeap.poll();
        }

        public int add(int val) {
            minHeap.offer(val);
            if (minHeap.size() > k)
                // We don't want to poll if heap size is less than k, otherwise the peek in the end may cause NPE,
                // if there is only one item in the heap)
                minHeap.poll();
            return minHeap.peek();
        }
    }


    /**
     * This is the implementation using hand-made BST
     * By using a BST, the time complexity for insertion and search are both O(h).
     * The time complexity of performing all the operations will be O(N*h). That is, O(N^2) in the worst case and O(NlogN) ideally.
     * https://leetcode.com/explore/learn/card/introduction-to-data-structure-binary-search-tree/142/conclusion/1026/
     */
    class KthLargestBST {
        // insert a node into the BST
        private Node insertNode(Node root, int num) {
            if (root == null) {
                return new Node(num, 1);
            }
            if (root.val < num) {
                root.right = insertNode(root.right, num);
            } else {
                root.left = insertNode(root.left, num);
            }
            root.cnt++;
            return root;
        }

        private int searchKth(Node root, int k) {
            // m = the size of right subtree
            int m = root.right != null ? root.right.cnt : 0;
            // root is the m+1 largest node in the BST
            if (k == m + 1) {
                return root.val;
            }
            if (k <= m) {
                // find kth largest in the right subtree
                return searchKth(root.right, k);
            } else {
                // find (k-m-1)th largest in the left subtree
                return searchKth(root.left, k - m - 1);
            }
        }

        private Node root;
        private int m_k;

        public KthLargestBST(int k, int[] nums) {
            root = null;
            for (int i = 0; i < nums.length; ++i) {
                root = insertNode(root, nums[i]);
            }
            m_k = k;
        }

        public int add(int val) {
            root = insertNode(root, val);
            return searchKth(root, m_k);
        }
    }

    class Node {    // the structure for the tree node
        Node left;
        Node right;
        int val;
        int cnt;    // the size of the subtree rooted at the node

        public Node(int v, int c) {
            left = null;
            right = null;
            val = v;
            cnt = c;
        }
    }

    @Test
    void testRandomizedSet() {
        RandomizedSet rSet = new RandomizedSet();
        rSet.insert(1);
        rSet.remove(2);
        rSet.insert(2);
        rSet.getRandom();
        rSet.remove(1);
        rSet.insert(2);
        rSet.getRandom();
    }

    /**
     * Insert Delete GetRandom O(1)
     * Implement the RandomizedSet class:
     * <p>
     * RandomizedSet() Initializes the RandomizedSet object.
     * bool insert(int val) Inserts an item val into the set if not present. Returns true if the item was not present, false otherwise.
     * bool remove(int val) Removes an item val from the set if present. Returns true if the item was present, false otherwise.
     * int getRandom() Returns a random element from the current set of elements (it's guaranteed that at least one element exists
     * when this method is called). Each element must have the same probability of being returned.
     * You must implement the functions of the class such that each function works in average O(1) time complexity.
     * <p>
     * https://leetcode.com/problems/insert-delete-getrandom-o1/description/
     * <p>
     * Use the combination of HashMap and ArrayList
     * <p>
     * HashMap provides Insert and Delete in
     * average constant time
     * <p>
     * ArrayList has indexes and could provide Insert and GetRandom in
     * average constant time, though has problems with Delete.
     * <p>
     * To delete a value at arbitrary index takes linear time.The solution here is to
     * always delete the last value:
     * - Swap the element to delete with the last one.
     * - Pop the last element out.
     * <p>
     * For that, one has to compute an index of each element in constant time, and hence
     * needs a hashmap which stores element -> its index dictionary.
     */
    class RandomizedSet {
        private final Map<Integer, Integer> valToListIdx;
        private final List<Integer> list;

        private final Random rand = new Random();

        public RandomizedSet() {
            valToListIdx = new HashMap<>();
            list = new ArrayList<>();
        }

        public boolean insert(int val) {
            if (valToListIdx.containsKey(val))
                return false;
            list.add(val);
            valToListIdx.put(val, list.size() - 1);
            return true;
        }

        public boolean remove(int val) {
            if (!valToListIdx.containsKey(val))
                return false;
            // Copy the last element val to the deleting element index position and update the map as well
            Integer deletingIdx = valToListIdx.get(val);
            Integer lastItemVal = list.get(list.size() - 1);
            list.set(deletingIdx, lastItemVal);
            valToListIdx.put(lastItemVal, deletingIdx);
            // delete the last element
            valToListIdx.remove(val);
            list.remove(list.size() - 1);
            return true;
        }

        public int getRandom() {
            return list.get(rand.nextInt(list.size()));
        }
    }

    /**
     * Design Tic-Tac-Toe
     * Assume the following rules are for the tic-tac-toe game on an n x n board between two players:
     * <p>
     * A move is guaranteed to be valid and is placed on an empty block.
     * Once a winning condition is reached, no more moves are allowed.
     * A player who succeeds in placing n of their marks in a horizontal, vertical, or diagonal row
     * wins the game.
     * <p>
     * Implement the TicTacToe class:
     * <p>
     * TicTacToe(int n) Initializes the object the size of the board n.
     * int move(int row, int col, int player) Indicates that the player with id player plays at the cell
     * (row, col) of the board. The move is guaranteed to be a valid move, and the two players alternate
     * in making moves. Return
     * <p>
     * 0 if there is no winner after the move,
     * 1 if player 1 is the winner after the move, or
     * 2 if player 2 is the winner after the move.
     * <p>
     * Input
     * ["TicTacToe", "move", "move", "move", "move", "move", "move", "move"]
     * [[3], [0, 0, 1], [0, 2, 2], [2, 2, 1], [1, 1, 2], [2, 0, 1], [1, 0, 2], [2, 1, 1]]
     * Output
     * [null, 0, 0, 0, 0, 0, 0, 1]
     * <p>
     * https://leetcode.com/problems/design-tic-tac-toe/description/
     * <p>
     * Key observation
     * We can simplify the way to determine if any player win by not maintaining complete N x N grid.
     * 1. On every move, we must determine whether a player has marked all of the cells in a row or column.
     * In other words, we could say that, if there are n rows and n columns on a board, the player must have
     * marked a certain row or column n times.
     * 2. On every move, we must determine whether a player has marked all of the cells on the main diagonal or
     * anti-diagonal. Irrespective of the size of the board, there can only be one diagonal and one anti-diagonal.
     * Also, there are always n cells on the diagonal or anti-diagonal. Thus, to win by either of these, a player
     * must have marked the cells on the diagonal or anti-diagonal n times.
     * <p>
     * Design key points
     * 1. Use two arrays(n-size), rows and cols to store the number of times a player has marked a cell on the ith
     * row/column. Cuz there are only two players, we assign 1 and -1 to them respectively. When one player marks on
     * one cell, we add his number to the row and col index at both arrays.
     * The player will win if either rows[i] or cols[j] is equal to n after the player has marked the cell on the
     * ith row and jth column.
     * 2. Use addition two integer variables, diagonal and antiDiagonal, to store how many times a cell has been
     * marked on each of the diagonals. We also add the player's value to them if his move is on any cells on the
     * diagonal or antiDiagonal.
     * After a player has marked a cell on a diagonal row, we check if the value of variable diagonal for that
     * player is equal to n. Similarly, after a player has marked a cell on an anti-diagonal row, we check if
     * the value of variable antiDiagonal for that player is equal to n.
     * <p>
     * Note: To identify whether cells lie on the diagonal or anti-diagonal
     * Diagonal: row = col
     * Anti-diagonal: col = n - row - 1
     * <p>
     * <p>
     * Time Complexity: O(1) because for every move, we mark a particular row, column, diagonal,
     * and anti-diagonal in constant time.
     * <p>
     * Space Complexity: O(n) because we use arrays rows and cols of size n. The variables diagonal
     * and antiDiagonal use constant extra space.
     */
    class TicTacToe {
        int[] rows;
        int[] cols;
        int diagonal;
        int antiDiagonal;

        public TicTacToe(int n) {
            rows = new int[n];
            cols = new int[n];
        }

        public int move(int row, int col, int player) {
            int currentPlayerVal = player == 1 ? 1 : -1;
            // update currentPlayer in rows and cols arrays
            rows[row] += currentPlayerVal;
            cols[col] += currentPlayerVal;
            // update diagonal
            if (row == col)
                diagonal += currentPlayerVal;

            //update anti diagonal
            if (col == rows.length - row - 1)
                antiDiagonal += currentPlayerVal;

            int n = rows.length;
            // check if the current player wins
            if (Math.abs(rows[row]) == n || Math.abs(cols[col]) == n
                    || Math.abs(diagonal) == n || Math.abs(antiDiagonal) == n)
                return player;
            // No one wins
            return 0;
        }
    }

    @Test
    void testVector2D() {
        Vector2D vector2D = new Vector2D(new int[][]{{1, 2}, {3}, {}, {}, {5}, {}, {6, 7}});
        Assertions.assertThat(vector2D.hasNext()).isTrue();
        Assertions.assertThat(vector2D.next()).isEqualTo(1);
        Assertions.assertThat(vector2D.next()).isEqualTo(2);
        Assertions.assertThat(vector2D.next()).isEqualTo(3);
        Assertions.assertThat(vector2D.next()).isEqualTo(5);
        Assertions.assertThat(vector2D.hasNext()).isTrue();
        Assertions.assertThat(vector2D.next()).isEqualTo(6);
        Assertions.assertThat(vector2D.next()).isEqualTo(7);
        Assertions.assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(vector2D::next);
    }

    /**
     * Flatten 2D Vector
     * Design an iterator to flatten a 2D vector. It should support the next and hasNext operations.
     * <p>
     * Implement the Vector2D class:
     * <p>
     * Vector2D(int[][] vec) initializes the object with the 2D vector vec.
     * next() returns the next element from the 2D vector and moves the pointer one step forward.
     * You may assume that all the calls to next are valid.
     * hasNext() returns true if there are still some elements in the vector, and false otherwise.
     * <p>
     * Input
     * ["Vector2D", "next", "next", "next", "hasNext", "hasNext", "next", "hasNext"]
     * [[[[1, 2], [3], [4]]], [], [], [], [], [], [], []]
     * Output
     * [null, 1, 2, 3, true, true, 4, false]
     * <p>
     * Explanation
     * Vector2D vector2D = new Vector2D([[1, 2], [3], [4]]);
     * vector2D.next();    // return 1
     * vector2D.next();    // return 2
     * vector2D.next();    // return 3
     * vector2D.hasNext(); // return True
     * vector2D.hasNext(); // return True
     * vector2D.next();    // return 4
     * vector2D.hasNext(); // return False
     * <p>
     * https://leetcode.com/problems/flatten-2d-vector/description/
     * <p>
     * Use rowIdx and colIdx to track the current position in the 2D-array
     * Instead of copying all elements of 2D-array into a list and iterate over it(This is the easiest implementation),
     * we just use two pointers to iterate over the exisitng 2D-array directly.
     * The trick is cuz the input 2D-array may have empty "row", we need a helper method, advanceToNext(),
     * checks if the current colIdx and rowIdx values point to an integer, and if they don't, then it moves them forward
     * until they point to an integer, i.e. the head of the next non-empty row array.
     * This method needs to be called in the beginning of hasNext() first, and next() needs to call the hasNext() first
     * to determine if it can proceed.
     */
    class Vector2D {
        private final int[][] vector;
        private int colIdx = 0;
        private int rowIdx = 0;

        public Vector2D(int[][] vec) {
            vector = vec;
        }

        // If the current rowIdx and colIdx point to an integer, this method does nothing.
        // Otherwise, colIdx and rowIdx are advanced until they point to an integer.
        // If there are no more integers, then rowIdx will be equal to vector.length
        // when this method terminates.
        private void advanceToNext() {
            // While rowIdx is still within the bound of vector[][], but colIdx is out of bound of the array pointed to by
            // rowIdx, i.e. vector[rowIdx], after we advance colIdx from last next() call, i.e. colIdx == vector[rowIdx].length,
            // We want to move both ptr to the start of the head of the next non-empty row
            while (rowIdx < vector.length && colIdx == vector[rowIdx].length) {
                // Loop terminates after we move to the row having element, i.e. vector[rowIdx].length != 0
                // or rowIdx is moved out of bound of vector[][]
                ++rowIdx;
                colIdx = 0;
            }
        }

        public int next() {
            if (!hasNext())
                // the rowIdx is moved out of bound of the vector[][]
                throw new NoSuchElementException();
            // Per requirement, it needs to move the pointer one step forward
            return vector[rowIdx][colIdx++];
        }

        public boolean hasNext() {
            // Ensure the position pointers are moved such they point to an integer,
            // or put the rowIdx out of bound
            advanceToNext();
            // After the advanceToNext call, the rowIdx may be moved out of bound of the vector[][], i.e. rowIdx = vector.length,
            // then there are no integers left, otherwise we've stopped at an integer and so there's an integer left.
            return rowIdx < vector.length;
        }
    }


    /**
     * Design Circular Queue
     * Design your implementation of the circular queue. The circular queue is a linear data structure in
     * which the operations are performed based on FIFO (First In First Out) principle, and the last
     * position is connected back to the first position to make a circle. It is also called "Ring Buffer".
     * <p>
     * One of the benefits of the circular queue is that we can make use of the spaces in front of the
     * queue. In a normal queue, once the queue becomes full, we cannot insert the next element even if
     * there is a space in front of the queue. But using the circular queue, we can use the space to
     * store new values.
     * <p>
     * Implement the MyCircularQueue class:
     * <p>
     * MyCircularQueue(k) Initializes the object with the size of the queue to be k.
     * int Front() Gets the front item from the queue. If the queue is empty, return -1.
     * int Rear() Gets the last item from the queue. If the queue is empty, return -1.
     * boolean enQueue(int value) Inserts an element into the circular queue. Return true if the operation is successful.
     * boolean deQueue() Deletes an element from the circular queue. Return true if the operation is successful.
     * boolean isEmpty() Checks whether the circular queue is empty or not.
     * boolean isFull() Checks whether the circular queue is full or not.
     * You must solve the problem without using the built-in queue data structure in your programming language.
     * <p>
     * https://leetcode.com/problems/design-circular-queue/description/
     * <p>
     * Use a fixed-sized array and maintain the headIndex and the count of elements in array to support all ops.
     * The key are the following formula:
     * 1. insertedIdx = (headIdx + count) % capacity(array size)
     * 2. tailIdx = (headIdx + count - 1) % capacity(array size)
     * <p>
     * Time complexity: O(1). All of the methods in our circular data structure is of constant time complexity.
     * <p>
     * Space Complexity: O(N). The overall space complexity of the data structure is linear, where
     * N is the pre-assigned capacity of the queue. However, it is worth mentioning that the memory consumption
     * of the data structure remains as its pre-assigned capacity during its entire life cycle.
     */
    class MyCircularQueue {
        private final int[] queue;
        private int headIdx;
        private int count;
        private final int capacity;

        // private ReentrantLock queueLock = new ReentrantLock();
        // we can use the lock for the enQueue/deQueue method for the thread safety

        public MyCircularQueue(int k) {
            queue = new int[k];
            headIdx = 0;
            count = 0;
            capacity = k;
        }

        public boolean enQueue(int value) {
            if (count == capacity)
                return false;
            int insertedIdx = (headIdx + count) % capacity; // This is the KEY!
            queue[insertedIdx] = value;
            ++count;
            return true;
        }

        public boolean deQueue() {
            if (count == 0)
                return false;
            headIdx = (headIdx + 1) % capacity; // This is the KEY!
            --count;
            return true;
        }

        public int Front() {
            if (count == 0)
                return -1;
            return queue[headIdx];
        }

        public int Rear() {
            if (count == 0)
                return -1;
            int tailIdx = (headIdx + count - 1) % capacity; // This is the KEY!
            return queue[tailIdx];
        }

        public boolean isEmpty() {
            return count == 0;
        }

        public boolean isFull() {
            return count == capacity;
        }
    }

    /**
     * Implement Queue using Stacks
     * Implement a first in first out (FIFO) queue using only two stacks. The implemented queue should
     * support all the functions of a normal queue (push, peek, pop, and empty).
     * <p>
     * Implement the MyQueue class:
     * <p>
     * void push(int x) Pushes element x to the back of the queue.
     * int pop() Removes the element from the front of the queue and returns it.
     * int peek() Returns the element at the front of the queue.
     * boolean empty() Returns true if the queue is empty, false otherwise.
     * Notes:
     * <p>
     * You must use only standard operations of a stack, which means only push to top, peek/pop from top,
     * size, and is empty operations are valid.
     * Depending on your language, the stack may not be supported natively. You may simulate a stack
     * using a list or deque (double-ended queue) as long as you use only a stack's standard operations.
     * <p>
     * https://leetcode.com/problems/implement-queue-using-stacks/description/
     */
    class MyQueue {
        private Deque<Integer> stackOne;
        private Deque<Integer> stackTwo;

        // This is necessary cuz before we transfer all data to the stackTwo, we need to keep track of
        // the front value for the peek function
        private int front = 0;

        public MyQueue() {
            stackOne = new ArrayDeque<>();
            stackTwo = new ArrayDeque<>();
        }

        public void push(int x) {
            if (stackOne.isEmpty())
                front = x;
            stackOne.push(x);
        }

        // To remove the bottom element from s1, we have to pop all elements from s1 and to push them on to an
        // additional stack s2, which helps us to store the elements of s1 in reversed order. This way the bottom
        // element of s1 will be positioned on top of s2 and we can simply pop it from stack s2. Once s2 is empty,
        // the algorithm transfer data from s1 to s2 again.
        public int pop() {
            if (stackTwo.isEmpty()) {
                while (!stackOne.isEmpty())
                    stackTwo.push(stackOne.pop());
            }
            return stackTwo.pop();
        }

        public int peek() {
            if (!stackTwo.isEmpty())
                return stackTwo.peek();
            return front;
        }

        public boolean empty() {
            return stackOne.isEmpty() && stackTwo.isEmpty();
        }
    }

    /**
     * Find Median from Data Stream
     * The median is the middle value in an ordered integer list. If the size of the list is even,
     * there is no middle value, and the median is the mean of the two middle values.
     * <p>
     * For example, for arr = [2,3,4], the median is 3.
     * For example, for arr = [2,3], the median is (2 + 3) / 2 = 2.5.
     * Implement the MedianFinder class:
     * <p>
     * MedianFinder() initializes the MedianFinder object.
     * void addNum(int num) adds the integer num from the data stream to the data structure.
     * double findMedian() returns the median of all elements so far. Answers within 10-5 of the
     * actual answer will be accepted.
     * <p>
     * Input
     * ["MedianFinder", "addNum", "addNum", "findMedian", "addNum", "findMedian"]
     * [[], [1], [2], [], [3], []]
     * Output
     * [null, null, null, 1.5, null, 2.0]
     * <p>
     * Explanation
     * MedianFinder medianFinder = new MedianFinder();
     * medianFinder.addNum(1);    // arr = [1]
     * medianFinder.addNum(2);    // arr = [1, 2]
     * medianFinder.findMedian(); // return 1.5 (i.e., (1 + 2) / 2)
     * medianFinder.addNum(3);    // arr[1, 2, 3]
     * medianFinder.findMedian(); // return 2.0
     * <p>
     * https://leetcode.com/problems/find-median-from-data-stream/description/
     * <p>
     * Use a MaxHeap to store the smaller half of the numbers and a MinHeap to store the larger half of the numbers.
     * The median is either the top of one heap or the average of the two top of both heaps.
     * <p>
     * Maintain one MaxHeap and MinHeap to store half of the input numbers respectively. When adding a number,
     * We need to ensure
     * 1. Both the heaps are balanced (or nearly balanced). The difference of the both sizes can't be more than 1
     * 2. The max-heap contains all the smaller numbers while the min-heap contains all the larger numbers,
     * i.e. MinHeap.top <= MaxHeap.top or MinHeap.top >= MaxHeap.top
     * <p>
     * Time complexity: O(log n)
     * Space complexity: O(n)
     */
    class MedianFinder {
        Queue<Integer> upperMinHeap;
        Queue<Integer> lowerMaxHeap;

        public MedianFinder() {
            upperMinHeap = new PriorityQueue<>();
            lowerMaxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        }

        public void addNum(int num) {
            // Insert in lowerHeap if it's empty or if number being inserted is less than the peek of lowerHeap
            // otherwise insert in upperHeap
            if (lowerMaxHeap.isEmpty() || num <= lowerMaxHeap.peek())
                lowerMaxHeap.offer(num);
            else
                upperMinHeap.offer(num);

            // Ensure that the halves are balanced, i.e. there is no more than a difference of 1 in size of both halves
            // Let lowerHeap be the one to hold one extra element if the size of the total data stream is odd otherwise be equal to upperHeap
            if (lowerMaxHeap.size() < upperMinHeap.size())
                // If an element added above made upperHeap have one more element than lowerHeap then we poll it and put it into lowerHeap
                lowerMaxHeap.offer(upperMinHeap.poll());
            else if (lowerMaxHeap.size() > upperMinHeap.size() + 1)
                // If an element added above, made lowerHeap have 2 more elements than upperHeap then we put one into upperHeap from lowerHeap
                upperMinHeap.offer(lowerMaxHeap.poll());
        }

        public double findMedian() {
            if (lowerMaxHeap.size() == upperMinHeap.size())
                return (lowerMaxHeap.peek() + upperMinHeap.peek()) / 2.0;
            else
                return (double) lowerMaxHeap.peek();
        }
    }

    /**
     * LRU Cache
     * Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.
     * <p>
     * Implement the LRUCache class:
     * <p>
     * LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
     * int get(int key) Return the value of the key if the key exists, otherwise return -1.
     * void put(int key, int value) Update the value of the key if the key exists. Otherwise,
     * add the key-value pair to the cache. If the number of keys exceeds the capacity
     * from this operation, evict the least recently used key. The functions get and put
     * must each run in O(1) average time complexity.
     * <p>
     * Input
     * ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
     * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
     * Output
     * [null, null, null, 1, null, -1, null, -1, 3, 4]
     * <p>
     * Explanation
     * LRUCache lRUCache = new LRUCache(2);
     * lRUCache.put(1, 1); // cache is {1=1}
     * lRUCache.put(2, 2); // cache is {1=1, 2=2}
     * lRUCache.get(1);    // return 1
     * lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
     * lRUCache.get(2);    // returns -1 (not found)
     * lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
     * lRUCache.get(1);    // return -1 (not found)
     * lRUCache.get(3);    // return 3
     * lRUCache.get(4);    // return 4
     * https://leetcode.com/problems/lru-cache/description/
     * <p>
     * Use HashMap w/ value point to the node in Doubly Linked List w/ dummy head and tail node.
     * The most recently used node(called by put/get) will be moved to the end of linked list(before the tail node)
     * Therefore nodes toward the head are least recently used, so when the put call exceeds the capacity,
     * we delete the node after the head node.
     * <p>
     * Time complexity: O(1) for both get and put
     * Space complexity: O(capacity)
     */
    class LRUCache {

        private class ListNode {
            int key;
            int val;
            ListNode prev;
            ListNode next;

            public ListNode(int key, int val) {
                this.key = key;
                this.val = val;
            }
        }

        private int capacity;
        private Map<Integer, ListNode> keyToNode;
        // The dummy head and tail nodes will help us to easily add/remove nodes on both ends
        private ListNode head; // Least recently used
        private ListNode tail; // Most recently used

        public LRUCache(int capacity) {
            keyToNode = new HashMap<>(capacity);
            head = new ListNode(-1, -1);
            head.next = tail;
            tail = new ListNode(-1, -1);
            tail.prev = head;
            this.capacity = capacity;
        }

        // Remove a node from the linked list
        private void removeNode(ListNode node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        // Add node to the end of the linked list (before tail node)
        private void addNode(ListNode node) {
            tail.prev.next = node;
            node.prev = tail.prev;
            node.next = tail;
            tail.prev = node;
        }

        public int get(int key) {
            if (!keyToNode.containsKey(key))
                return -1;
            ListNode node = keyToNode.get(key);
            // remove + add = Move to the end of linked list, so it becomes most recently used
            removeNode(node);
            addNode(node);
            return node.val;
        }

        public void put(int key, int value) {
            if (keyToNode.containsKey(key)) {
                removeNode(keyToNode.get(key));
            }
            ListNode node = new ListNode(key, value);
            addNode(node);
            keyToNode.put(key, node);
            if (keyToNode.size() > capacity) {
                // Exceeds the capacity, delete the least recently used node, i.e. node after the head node
                keyToNode.remove(head.next.key);
                removeNode(head.next);
            }
        }
    }

    /**
     * Time Based Key-Value Store
     * Design a time-based key-value data structure that can store multiple values for the same key at different
     * time stamps and retrieve the key's value at a certain timestamp.
     * <p>
     * Implement the TimeMap class:
     * <p>
     * TimeMap() Initializes the object of the data structure.
     * <p>
     * void set(String key, String value, int timestamp) Stores the key with the value at the
     * given time timestamp.
     * <p>
     * String get(String key, int timestamp) Returns a value such that set was called previously, with
     * timestamp_prev <= timestamp. If there are multiple such values, it returns the value associated with
     * the largest timestamp_prev. If there are no values, it returns "".
     * <p>
     * Input
     * ["TimeMap", "set", "get", "get", "set", "get", "get"]
     * [[], ["foo", "bar", 1], ["foo", 1], ["foo", 3], ["foo", "bar2", 4], ["foo", 4], ["foo", 5]]
     * Output
     * [null, null, "bar", "bar", null, "bar2", "bar2"]
     * <p>
     * Explanation
     * TimeMap timeMap = new TimeMap();
     * timeMap.set("foo", "bar", 1);  // store the key "foo" and value "bar" along with timestamp = 1.
     * timeMap.get("foo", 1);         // return "bar"
     * timeMap.get("foo", 3);         // return "bar", since there is no value corresponding to foo at timestamp 3 and timestamp 2, then the only value is at timestamp 1 is "bar".
     * timeMap.set("foo", "bar2", 4); // store the key "foo" and value "bar2" along with timestamp = 4.
     * timeMap.get("foo", 4);         // return "bar2"
     * timeMap.get("foo", 5);         // return "bar2"
     * <p>
     * https://leetcode.com/problems/time-based-key-value-store/description/
     * <p>
     * Use the HashMap to List of Pair of timestamp and value to store the (key, value, timestamp) set by set method.
     * Cuz the requiremnt says the set methods are ONLY called w/ timestamp increasing, we can assume the list is always
     * sorted. So we perfomr binary search to find the value whose timestamp <= target timestamp in the get method.
     * <p>
     * Note: Without that requirement, we will use HashMap to TreeMap(timestamp -> value) to rely on the internal sorting
     * from TreeMap. We can call the floorEntry(targetTimestamp) method on TreeMap to get the timestamp-value mapping
     * associated with the greatest timestamp less than or equal to the given timestamp, or null if there is no such timestamp.
     * <p>
     * Time complexity:
     * If M is the number of set function calls, N is the number of get function calls, and L is average length of key and
     * value strings.
     * <p>
     * set() function, in each call, we push a (timestamp, value) pair in the key bucket, which takes O(L) time to
     * hash the string. Thus, for M calls overall it will take, O(M⋅L) time.
     * <p>
     * Note: TreeMap solution will have O(L⋅M⋅logM)
     * <p>
     * <p>
     * get() function, we use binary search on the key's bucket which can have at most M elements and
     * to hash the string it takes O(L) time, thus overall it will take O(L⋅logM) time for binary search.
     * And, for N calls overall it will take, O(N⋅L⋅logM) time.
     * <p>
     * Note: TreeMap solution will have O(N⋅(L⋅logM+logM)) --> Not sure if this is true for Java TreeMap
     * <p>
     * Space complexity:
     * set() function, in each call we store one value string of length L, which takes O(L) space.
     * Thus, for M calls we may store M unique values, so overall it may take O(M⋅L) space.
     * <p>
     * get() function, we are not using any additional space.
     * Thus, for all N calls it is a constant space operation.
     */
    class TimeMap {

        Map<String, List<Pair<Integer, String>>> keyToTimestampVal;

        public TimeMap() {
            keyToTimestampVal = new HashMap<>();
        }

        public void set(String key, String value, int timestamp) {
            keyToTimestampVal.computeIfAbsent(key, k -> new ArrayList<>()).add(new Pair<>(timestamp, value));
        }

        public String getWithJDKBinarySearch(String key, int timestamp) {
            if (!keyToTimestampVal.containsKey(key))
                return "";
            List<Pair<Integer, String>> timestampValList = keyToTimestampVal.get(key);
            if (!timestampValList.isEmpty()) {
                Pair<Integer, String> target = new Pair<>(timestamp, timestampValList.get(0).getValue());
                // Uaw JSK build-in binary search API
                int idx = Collections.binarySearch(timestampValList, target, Comparator.comparing(p -> p.getKey()));
                if (idx >= 0)
                    return timestampValList.get(idx).getValue();
                else { // key is not found in the list
                    int insertionIdx = -1 * idx - 1; // restore to the insertion index, i.e. the idx of the first element greater than the key
                    if (insertionIdx == 0) // No smaller timestamp in the list
                        return "";
                    return timestampValList.get(--insertionIdx).getValue(); // return the value of previous timestamp
                }
            }
            return "";
        }

        public String get(String key, int timestamp) {
            if (!keyToTimestampVal.containsKey(key))
                return "";
            List<Pair<Integer, String>> timestampValList = keyToTimestampVal.get(key);
            if (!timestampValList.isEmpty()) {
                // The standard binary search implementation with additional check of right ptr position in the end
                int left = 0, right = timestampValList.size() - 1;
                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    Integer midTimestamp = timestampValList.get(mid).getKey();
                    if (midTimestamp > timestamp) {
                        right = mid - 1;
                    } else if (midTimestamp < timestamp) {
                        left = mid + 1;
                    } else
                        return timestampValList.get(mid).getValue();
                }
                if (right < 0)
                    // When right ptr moved out of the head of list, means the target timestamp is less than everything in the list
                    return "";
                return timestampValList.get(--left).getValue(); // Take the value one index before the last ptr
            }
            return "";
        }
    }

    @Test
    void testTimeMap() {
        TimeMap timeMap = new TimeMap();
        timeMap.set("foo", "bar", 1);
        Assertions.assertThat(timeMap.get("foo", 1)).isEqualTo("bar");
        Assertions.assertThat(timeMap.get("foo", 3)).isEqualTo("bar");
        timeMap.set("foo", "bar2", 4); // store the key "foo" and value "bar2" along with timestamp = 4.
        Assertions.assertThat(timeMap.get("foo", 4)).isEqualTo("bar2");
        Assertions.assertThat(timeMap.get("foo", 5)).isEqualTo("bar2");

        timeMap = new TimeMap();
        timeMap.set("love", "high", 10);
        timeMap.set("love", "low", 20);
        Assertions.assertThat(timeMap.get("love", 5)).isEqualTo("");
        Assertions.assertThat(timeMap.get("love", 10)).isEqualTo("high");
        Assertions.assertThat(timeMap.get("love", 15)).isEqualTo("high");
        Assertions.assertThat(timeMap.get("love", 20)).isEqualTo("low");
        Assertions.assertThat(timeMap.get("love", 25)).isEqualTo("low");
    }
}