package playground.tree;

import javafx.util.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/* TODO: General useful tips
 *  1. BST tips:
 *      - Insert/Delete a note from BST has time complexity O(H), where H is a height of the binary tree.
 *        H=log N for the balanced tree and H=N for a skewed tree.
 *      - Inorder traversal of BST is an array sorted in the ascending order.
 *        That also means it visits the node value in the sorted order when traversal.
 *      - A height-balanced (or self-balancing) binary search tree is a binary search tree that automatically
 *        keeps its height small in the face of arbitrary item insertions and deletions. That is, the height
 *        of a balanced BST with N nodes is always logN. Also, the height of the two subtrees of every node
 *        never differs by more than 1.
 *        Why log N?
 *        A binary tree with height h contains at most 2^0 + 2^1 + ... + 2^h = 2^h+1 - 1
 *        In other word, a binary tree with N nodes and height h: N <= 2^h+1 - 1
 *        That is: h >= log N (logarithm of N to base 2)
 *  2. Useful tips when implementing the solution using "stack" or "queue" data structure
 *      - Use Deque<Node> stack = new ArrayDeque<>(); --> This obj supports common ops of queue and stack
 *  3. Binary tree traversal is usually implemented in
 *      - Pointer from the root and move downward to left or right child
 *      - Recursive
 *      - Iterative using stack to simulate recursion process (DFS)
 *      - Iterative using queue to simulate recursion process (BFS)
 *  4. When the problem relates to check or validate sth for the tree, the approach is usually creating a
 *     recursive method to traversal from root to the leaf node. The following are some tips.
        1. First think what should be done at the recursive method when at the very basic use case, such as
           we are at the leaf node or we are at root node in a 3-node BST
        2. Define the base case first, i.e. the input node is null usually. What should be returned or what
           should we do particularly
        3. If the traversal from root is to validate/check/find sth, keep in mind the method MAY return early
           even before reaching the leaf node if the logic is to return false early. This part is the core logic of
           the method.
        4. The recursive method can either defined as returning sth back to the caller then subsequently bubble
           up. Or some kind of collection parameter like List such as nodes visited or AtomicInteger such as max/min
           value, passed around and get updated. The alternative is to define them as instance variable.
        5. The recursive call may be placed at the return statement directly to explore the left and right
           child nodes or inside the core logic part. The former pattern is usually used for validating or
           finding sth, so the recursion will return early if the condition is satisfied. The latter is used for
           scenario that you need to collect or do sth from both right and left sub-tree in the bottom-up manner,
           which results in a DFS style on left and right child separately.
    5. When the problem asks ALL paths satisfying certain condition. It may be able to be solved by using recursion to
       explore left and right subtree respectively and undo/remove visited node before backtracking.

 */
public class TreeQuestion {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Helper method to generate the Tree using the LeetCode binary tree representation.
     * Level order traversal, where null signifies a path terminator where no node exists below
     * https://support.leetcode.com/hc/en-us/articles/360011883654-What-does-1-null-2-3-mean-in-binary-tree-representation-
     * https://leetcode.com/problems/recover-binary-search-tree/solutions/32539/Tree-Deserializer-and-Visualizer-for-Python/
     *
     * @param nodes Integer array, e.g. [1,null,2,3]
     * @return the root node of the tree
     */
    TreeNode createBinaryTree(Integer... nodes) {
        TreeNode[] treeNodes = new TreeNode[nodes.length];
        Stack<TreeNode> stack = new Stack<>();
        int n = 0;
        for (int i = nodes.length - 1; i >= 0; i--) {
            TreeNode node = (nodes[i] == null) ? null : new TreeNode(nodes[i]);
            treeNodes[nodes.length - 1 - (n++)] = node;
            stack.push(node);
        }
        TreeNode root = stack.pop();
        for (TreeNode node : treeNodes) {
            if (node != null) {
                if (!stack.empty())
                    node.left = stack.pop();
                if (!stack.empty())
                    node.right = stack.pop();
            }
        }
        return root;
    }

    /**
     * Search in a Binary Search Tree
     * You are given the root of a binary search tree (BST) and an integer val.
     * <p>
     * Find the node in the BST that the node's value equals val and return the subtree rooted with that node. If such a node does not exist, return null.
     * <p>
     * Input: root = [4,2,7,1,3], val = 2
     * Output: [2,1,3]
     * https://leetcode.com/problems/search-in-a-binary-search-tree/description/
     */
    @Test
    void testSearchBST() {
        /**
         * input:
         *       4
         *     /  \
         *    2    7
         *   /\
         *  1  3
         */
        TreeNode secondLv2LL = new TreeNode(1);
        TreeNode secondLv2LR = new TreeNode(3);
        TreeNode secondLvlRight = new TreeNode(7, null, null);
        TreeNode secondLvlLeft = new TreeNode(2, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(4, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(searchBSTRecursive(root, 2)).isEqualTo(secondLvlLeft);
        Assertions.assertThat(searchBSTIterative(root, 2)).isEqualTo(secondLvlLeft);
    }

    /**
     * Time complexity: O(H), where H is a tree height. That results in
     * O(log N) in the average case, and O(N) in the worst case.
     * <p>
     * Space complexity : O(H) to keep the recursion stack,
     * i.e. O(log N) in the average case, and O(N) in the worst case.
     */
    TreeNode searchBSTRecursive(TreeNode root, int val) {
        if (root == null || root.val == val)
            return root;
        if (val > root.val)
            return searchBSTRecursive(root.right, val);
        return searchBSTRecursive(root.left, val);
    }

    /**
     * Time complexity: O(H), where H is a tree height. That results in
     * O(log N) in the average case, and O(N) in the worst case.
     * <p>
     * Space complexity : O(1)
     */
    TreeNode searchBSTIterative(TreeNode root, int val) {
        TreeNode current = root;
        while (current != null && current.val != val) {
            if (val > current.val)
                current = current.right;
            else
                current = current.left;
        }
        return current;
    }

    /**
     * Insert into a Binary Search Tree
     * You are given the root node of a binary search tree (BST) and a value to insert into the tree.
     * Return the root node of the BST after the insertion. It is guaranteed that the new value does not exist in the original BST.
     * <p>
     * Notice that there may exist multiple valid ways for the insertion, as long as the tree remains a BST after insertion.
     * You can return any of them.
     * <p>
     * Input: root = [4,2,7,1,3], val = 5
     * Output: [4,2,7,1,3,5]
     * https://leetcode.com/problems/insert-into-a-binary-search-tree/description/
     */
    @Test
    void testInsertIntoBST() {
        /**
         * input:
         *       4
         *     /  \
         *    2    7
         *   /\
         *  1  3
         */
        TreeNode secondLv2LL = new TreeNode(1);
        TreeNode secondLv2LR = new TreeNode(3);
        TreeNode secondLvlRight = new TreeNode(7, null, null);
        TreeNode secondLvlLeft = new TreeNode(2, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(4, secondLvlLeft, secondLvlRight);
        TreeNode node = insertIntoBSTRecursive(root, 5);
        Assertions.assertThat(node.right.left.val).isEqualTo(5);
        node = insertIntoBSTIterative(root, 5);
        Assertions.assertThat(node.right.left.val).isEqualTo(5);
        //Assertions.assertThat(searchBSTIterative(root, 2)).isEqualTo(secondLvlLeft);
    }

    /**
     * Time complexity: O(H), where H is a tree height. That results in
     * O(log N) in the average case, and O(N) in the worst case.
     * <p>
     * Space complexity : O(H) to keep the recursion stack,
     * i.e. O(log N) in the average case, and O(N) in the worst case.
     */
    TreeNode insertIntoBSTRecursive(TreeNode root, int val) {
        if (root == null)
            return new TreeNode(val);
        if (val > root.val)
            root.right = insertIntoBSTRecursive(root.right, val);
        else
            root.left = insertIntoBSTRecursive(root.left, val);
        return root;
    }

    /**
     * Time complexity: O(H), where H is a tree height. That results in
     * O(log N) in the average case, and O(N) in the worst case.
     * <p>
     * Space complexity : O(1)
     */
    TreeNode insertIntoBSTIterative(TreeNode root, int val) {
        TreeNode current = root;
        while (current != null) {
            if (val > current.val) { // insert into the right subtree
                if (current.right == null) {
                    current.right = new TreeNode(val);
                    return root;
                } else {
                    current = current.right;
                }
            } else { // insert into the left subtree
                if (current.left == null) {
                    current.left = new TreeNode(val);
                    return root;
                } else
                    current = current.left;
            }
        }
        return new TreeNode(5);
    }

    @Test
    void testDeleteNode() {
        /**
         * input:
         *       5
         *     /  \
         *    3    6
         *   /\     \
         *  2  4     7
         */
        TreeNode secondLv2LL = new TreeNode(2);
        TreeNode secondLv2LR = new TreeNode(4);
        TreeNode secondLv2RR = new TreeNode(7);
        TreeNode secondLvlRight = new TreeNode(6, null, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(3, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(5, secondLvlLeft, secondLvlRight);

        TreeNode node = deleteNode(root, 3);
        /**
         * Result: (One of the valid answers)
         *       5
         *     /  \
         *    2    6
         *     \     \
         *      4     7
         */
        Assertions.assertThat(node.left.val).isEqualTo(2);
        Assertions.assertThat(node.left.left).isNull();
        Assertions.assertThat(node.left.right.val).isEqualTo(4);
    }

    //TODO: IMPORTANT!
    //  Successor = "after node", i.e. the next node, or the smallest node after the current one.
    //  Move to the right once and then as many times to the left as you could.
    TreeNode findInorderSuccessor(TreeNode node) {
        node = node.right;
        while (node.left != null)
            node = node.left;
        return node;
    }

    //TODO: IMPORTANT!
    //  Predecessor = "before node", i.e. the previous node, or the largest node before the current one.
    //  Move to the left once and then as many times to the right as you could.
    TreeNode findInorderPredecessor(TreeNode node) {
        node = node.left;
        while (node.right != null)
            node = node.right;
        return node;
    }

    /**
     * There are three possible situations here :
     * <p>
     * 1. Node is a leaf, and one could delete it straightforward : node = null.
     * 2. Node is not a leaf and has a right child. Then the node could be replaced by its
     * successor which is somewhere lower in the right subtree. Then one could proceed down
     * recursively to delete the successor.
     * 3. Node is not a leaf, has no right child and has a left child. That means that its successor
     * is somewhere upper in the tree but we don't want to go back. Let's use the predecessor
     * here which is somewhere lower in the left subtree. The node could be replaced by its
     * predecessor and then one could proceed down recursively to delete the predecessor.
     * <p>
     * Time complexity : O(log N).
     * During the algorithm execution we go down the tree all the time - on the left or on the right,
     * first to search the node to delete O(H1) time complexity and then to actually delete it.
     * H1 is a tree height from the root to the node to delete.
     * Delete process takes O(H2) time, where H2 is a tree height from the root to delete to the leafs.
     * That in total results in O(H1+H2) = O(H) time complexity, where H is a tree height,
     * equal to log N in the case of the balanced tree.
     * <p>
     * Space complexity : O(H) to keep the recursion stack, where H is a tree height. H = log N for the balanced tree.
     */
    TreeNode deleteNode(TreeNode root, int key) {
        if (root == null)
            return null;
        if (key > root.val) // delete from the right subtree
            root.right = deleteNode(root.right, key);
        else if (key < root.val) // delete from the left subtree
            root.left = deleteNode(root.left, key);
        else { // Now we find the node!
            if (root.left == null && root.right == null)
                // The node is a leaf, set it to null so when this recursive call returns,
                // null will be set to either its parent.right/left
                root = null;
            else if (root.left == null) { // deleting node has right child
                root.val = findInorderSuccessor(root).val; // We don't really move the nodes, just copy its value to deleting node
                // recursively delete the successor in the right subtree
                root.right = deleteNode(root.right, root.val);
            } else { // deleting node has left child
                root.val = findInorderPredecessor(root).val;
                //  recursively delete the predecessor in the left subtree
                root.left = deleteNode(root.left, root.val);
            }
        }
        return root;
    }

    /**
     * Maximum Depth of Binary Tree
     * Given the root of a binary tree, return its maximum depth.
     * A binary tree's maximum depth is the number of nodes along the longest path from the
     * root node down to the farthest leaf node.
     * <p>
     * Input: root = [3,9,20,null,null,15,7]
     * Output: 3
     * <p>
     * Input: root = [1,null,2]
     * Output: 2
     * https://leetcode.com/problems/maximum-depth-of-binary-tree/solution/
     */
    @Test
    void testTreeMaxDepth() {
        TreeNode thirdLvl1 = new TreeNode(15);
        TreeNode thirdLvl2 = new TreeNode(7);
        TreeNode secondLvlRight = new TreeNode(20, thirdLvl1, thirdLvl2);
        TreeNode secondLvlLeft = new TreeNode(9);
        TreeNode root = new TreeNode(3, secondLvlLeft, secondLvlRight);
        Assertions.assertThat(maxDepth(root)).isEqualTo(3);
    }

    /**
     * Traverse the left and right child respectively and record the height during the traversal.
     * The recursive function should return the max of left and right height and plus 1(for the level count)
     * Time Complexity: O(N). Space Complexity: O(logN)
     */
    int maxDepth(TreeNode root) {
        if (root == null)
            return 0;
        int leftHeight = maxDepth(root.left);
        int rightHeight = maxDepth(root.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * Validate Binary Search Tree
     * Given the root of a binary tree, determine if it is a valid binary search tree (BST).
     * A valid BST is defined as follows:
     * The left subtree of a node contains only nodes with keys less than the node's key.
     * The right subtree of a node contains only nodes with keys greater than the node's key.
     * Both the left and right subtrees must also be binary search trees.
     * <p>
     * Input: root = [2,1,3]
     * Output: true
     * <p>
     * Input: root = [5,1,4,null,null,3,6]
     * Output: false
     * Explanation: The root node's value is 5 but its right child's value is 4.
     * https://leetcode.com/problems/validate-binary-search-tree/solution/
     */
    @Test
    void testIsValidBST() {
        /**
         * input:
         *       5
         *     /  \
         *    1    4
         *        / \
         *       3   6
         */
        TreeNode thirdLvlLeft = new TreeNode(3);
        TreeNode thirdLvlRight = new TreeNode(6);
        TreeNode secondLvlRight = new TreeNode(4, thirdLvlLeft, thirdLvlRight);
        TreeNode secondLvlLeft = new TreeNode(1);
        TreeNode root = new TreeNode(5, secondLvlLeft, secondLvlRight);
        Assertions.assertThat(isValidBSTRecursive(root)).isFalse();
        Assertions.assertThat(isValidBSTVv1(root)).isFalse();
        Assertions.assertThat(isValidBST(root)).isFalse();
        Assertions.assertThat(isValidBST(root)).isFalse();

        /**
         * input:
         *       2
         *     /  \
         *    1   3
         */
        secondLvlRight = new TreeNode(3);
        secondLvlLeft = new TreeNode(1);
        root = new TreeNode(2, secondLvlLeft, secondLvlRight);
        Assertions.assertThat(isValidBST(root)).isTrue();
        Assertions.assertThat(isValidBST(root)).isTrue();
        Assertions.assertThat(isValidBSTRecursive(root)).isTrue();
        Assertions.assertThat(isValidBSTVv1(root)).isTrue();

    }

    /**
     * Pass the nullable max and min value when recursively visit each node. If the current node <= min
     * or >= max, return false. The recursive function is called on left child w/ current node value as
     * mas, right child w/ current node value as min
     * Time Complexity: O(N). Space Complexity: O(N)
     */
    boolean isValidBST(TreeNode root) {
        // Use null to denote negative/positive infinity as initial lower/upper bound
        return validate(root, null, null);
    }

    boolean validate(TreeNode node, Integer low, Integer high) {
        if (node == null)
            // Empty trees are valid BSTs. We validate every internal node while traversal to the leaf node,
            // which means it is also valid so far when we reach here
            return true;
        // The current node's value must be between low and high.
        if ((low != null && node.val <= low) || (high != null && node.val >= high))
            return false;
        // The left and right subtree must also be valid.
        return validate(node.left, low, node.val) && validate(node.right, node.val, high);
    }

    private Integer prev;

    boolean isValidBSTRecursive(TreeNode root) {
        prev = null;
        return inOrderTraversalCheckBST(root);
    }

    // When a tree is BST, In order traversal will visit the node value in the sorted order
    boolean inOrderTraversalCheckBST(TreeNode node) {
        if (node == null) {
            return true;
        }
        if (!inOrderTraversalCheckBST(node.left)) {
            return false;
        }
        if (prev != null && node.val <= prev) {
            return false;
        }
        prev = node.val;
        return inOrderTraversalCheckBST(node.right);
    }

    // Time Complexity: O(N). Space Complexity: O(N)
    // Use LIFO stack to do in-order traversal
    boolean isValidBSTv2(TreeNode node) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        Integer prev = null;

        while (!stack.isEmpty() || node != null) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }


            node = stack.pop();
            // If next element in inorder traversal
            // is smaller than the previous one
            // that's not BST.
            if (prev != null && node.val <= prev) {
                return false;
            }
            prev = node.val;
            node = node.right;
        }
        return true;
    }

    boolean isValidBSTVv1(TreeNode node) {
        if (node == null) {
            return false;
        }

        Deque<TreeNode> stack = new ArrayDeque<>();
        Integer prev = null;

        stack.push(node);
        while (!stack.isEmpty()) {
            while (node.left != null) {
                node = node.left;
                stack.push(node);
            }

            node = stack.pop();
            // If next element in inorder traversal
            // is smaller than the previous one
            // that's not BST.
            if (prev != null && node.val <= prev) {
                return false;
            }
            // Move the prev to current node
            prev = node.val;

            if (node.right != null) {
                // Push right child to stack. We reach here cuz we just visited the
                // the parent node. So we want to check the left tree of it at next iteration
                node = node.right;
                stack.push(node);
            }

        }
        return true;
    }

    /**
     * Same Tree
     * Given the roots of two binary trees p and q, write a function to check if they
     * are the same or not.
     * <p>
     * Two binary trees are considered the same if they are structurally identical, and
     * the nodes have the same value.
     * <p>
     * Input: p = [1,2,3], q = [1,2,3]
     * Output: true
     * <p>
     * Input: p = [1,2], q = [1,null,2]
     * Output: false
     */
    @Test
    void testIsSameTree() {
        /**
         * input:
         *       1
         *     /  \
         *    2    3
         */
        TreeNode tree1 = createBinaryTree(1, 2, 3);
        TreeNode tree2 = createBinaryTree(1, 2, 3);
        Assertions.assertThat(isSameTreeRecursive(tree1, tree2)).isTrue();
        Assertions.assertThat(isSameTreeBFS(tree1, tree2)).isTrue();
        /**
         * input:
         *       1
         *     /
         *    2
         *
         * input:
         *       1
         *     /  \
         *         2
         */
        tree1 = createBinaryTree(1, 2);
        tree2 = createBinaryTree(1, null, 3);
        Assertions.assertThat(isSameTreeRecursive(tree1, tree2)).isFalse();
        Assertions.assertThat(isSameTreeBFS(tree1, tree2)).isFalse();
        Assertions.assertThat(isSameTreeBFS(null, null)).isTrue();
    }

    /**
     * Bottom-up recursion to compare the current node and the left node
     * and right node recursively on both tree
     */
    boolean isSameTreeRecursive(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        } else if (p == null || q == null) {
            return false;
        } else {
            return (p.val == q.val) && isSameTreeRecursive(p.left, q.left) && isSameTreeRecursive(p.right, q.right);
        }
    }

    private boolean check(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        } else if (p == null) {
            return false;
        } else if (q == null) {
            return false;
        } else
            return p.val == q.val;
    }

    /**
     * BFS traversal using two queues to traversal and compare the node on each tree at one time
     */
    boolean isSameTreeBFS(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (!check(p, q))
            return false;
        Queue<TreeNode> queueP = new ArrayDeque<>();
        queueP.offer(p);
        Queue<TreeNode> queueQ = new ArrayDeque<>();
        queueQ.offer(q);
        while (!queueP.isEmpty()) {
            TreeNode nodeP = queueP.poll();
            TreeNode nodeQ = queueQ.poll();
            if (!check(nodeP, nodeQ))
                return false;
            if (!check(nodeP.left, nodeQ.left))
                return false;
            // both left child nodes are equal
            if (nodeP.left != null) {
                queueP.offer(nodeP.left);
                queueQ.offer(nodeQ.left);
            }

            if (!check(nodeP.right, nodeQ.right))
                return false;
            // both right child nodes are equal
            if (nodeP.right != null) {
                queueP.offer(nodeP.right);
                queueQ.offer(nodeQ.right);
            }
        }
        return true;
    }

    /**
     * Subtree of Another Tree
     * Given the roots of two binary trees root and subRoot, return true if there is a subtree
     * of root with the same structure and node values of subRoot and false otherwise.
     * <p>
     * A subtree of a binary tree  is a tree that consists of a node in tree and ALL of
     * this node's descendants. The tree could also be considered as a subtree of itself.
     * <p>
     * Input: root = [3,4,5,1,2], subRoot = [4,1,2]
     * Output: true
     * <p>
     * Input: root = [3,4,5,1,2,null,null,null,null,0], subRoot = [4,1,2]
     * Output: false
     */
    @Test
    void testIsSubtree() {
        /**
         * input:
         *       3
         *     /  \
         *    4    5
         *  / \
         * 1   2
         *    /
         *   0
         *
         *   subRoot
         *       4
         *     /  \
         *    1    2
         */
        TreeNode tree1 = createBinaryTree(3, 4, 5, 1, 2, null, null, null, null, 0);
        TreeNode tree2 = createBinaryTree(4, 1, 2);
        Assertions.assertThat(isSubtree(tree1, tree2)).isFalse();
        /**
         * input:
         *       3
         *     /  \
         *    4    5
         *  / \
         * 1   2
         *
         *   subRoot
         *       4
         *     /  \
         *    1    2
         */
        tree1 = createBinaryTree(3, 4, 5, 1, 2);
        tree2 = createBinaryTree(4, 1, 2);
        Assertions.assertThat(isSubtree(tree1, tree2)).isTrue();
    }

    /**
     * Bottom-up recursion to first check if using the current node as the root of the 1st tree
     * is the same tree as the root node of 2nd tree. Then recursively call itself using the
     * left and the right child node as the root of 1st tree. Returns true if any branch is true.
     * <p>
     * Observation:
     * 1. This is the extension of the "Same Tree" problem. The difference is the checking logic
     * will be applied on each node in the 1st tree.
     * 2. Hence, we need to traverse the 1st tree, and when the node is not null, call the
     * logic of checking if two tree are the same from current visiting node in 1st tree and
     * the root node of 2nd tree. If not, recursively call itself using left and right child
     * as root of first tree respectively.
     * <p>
     * <p>
     * Time complexity: O(MN). For every N node in the first tree, we check if the tree rooted
     * at node is identical to subRoot. This check takes O(M) time, where M is the number of
     * nodes in 2nd tree, i.e. target subtree. Hence, the overall time complexity is O(MN).
     * <p>
     * Space complexity: O(M+N).
     * There will be at most N recursive call to isSubtree(). Now, each of these calls will
     * have M recursive calls to isSameTreeRecursive. Before calling isSameTreeRecursive,
     * our call stack has at most O(N) elements and might increase to O(N+M) during the call.
     * After calling isSameTreeRecursive, it will be back to at most O(N) since all elements
     * made by isSameTreeRecursive are popped out. Hence, the maximum number of elements in
     * the call stack will be M+N.
     */
    boolean isSubtree(TreeNode root, TreeNode subRoot) {
        // If current visiting node in the first tree is null, returns false cuz no need to compare an
        // empty subtree w/ the 2nd tree
        if (root == null) {
            return false;
        }
        // Check if using the current root node as the root of the 1st tree, two trees are the same
        if (isSameTreeRecursive(root, subRoot))
            return true;
        // Recursively try the left and right as the new root of the 1st tree. Return true if any branch returns true.
        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    /**
     * Symmetric Tree
     * Given the root of a binary tree, check whether it is a mirror of
     * itself (i.e., symmetric around its center).
     * <p>
     * Input: root = [1,2,2,3,4,4,3]
     * Output: true
     * <p>
     * Input: root = [1,2,2,null,3,null,3]
     * Output: false
     * https://leetcode.com/problems/symmetric-tree/editorial/
     */
    @Test
    void testIsSymmetric() {
        /**
         * input:
         *       5
         *     /  \
         *    1    4
         *        / \
         *       3   6
         */
        TreeNode thirdLvlLeft = new TreeNode(3);
        TreeNode thirdLvlRight = new TreeNode(6);
        TreeNode secondLvlRight = new TreeNode(4, thirdLvlLeft, thirdLvlRight);
        TreeNode secondLvlLeft = new TreeNode(1);
        TreeNode root = new TreeNode(5, secondLvlLeft, secondLvlRight);
        Assertions.assertThat(isSymmetric(root)).isFalse();

        /**
         * input:
         *       1
         *     /  \
         *    2   2
         *   /\   /\
         *  3  4 4  3
         */
        TreeNode secondLv2LL = new TreeNode(3);
        TreeNode secondLv2LR = new TreeNode(4);
        TreeNode secondLv2RL = new TreeNode(4);
        TreeNode secondLv2RR = new TreeNode(3);
        secondLvlRight = new TreeNode(2, secondLv2RL, secondLv2RR);
        secondLvlLeft = new TreeNode(2, secondLv2LL, secondLv2LR);
        root = new TreeNode(1, secondLvlLeft, secondLvlRight);
        Assertions.assertThat(isSymmetric(root)).isTrue();
    }

    boolean isSymmetric(TreeNode root) {
        return isMirror(root.left, root.right);
    }

    /**
     * Recursively compare the left child of node 1 with the right child of node 2, and the
     * right child of node 1 with the left child of node 2. Returns false if two nodes are not equal.
     * null and null is considered equal.
     * Time Complexity: O(N). Space Complexity: O(N)
     */
    boolean isMirror(TreeNode t1, TreeNode t2) {
        // null is symmetric, if we make here, it means the nodes visited on this path are valid so far
        if (t1 == null && t2 == null)
            return true;
        if ((t1 == null) || (t2 == null) || (t1.val != t2.val))
            // Two nodes are not equal if one is null and not the other, or node value are different
            return false;
        // To check symmetric, compare the left child of node 1 with the right child of node 2,
        // and the right child of node 1 with the left child of node 2
        return isMirror(t1.left, t2.right) && isMirror(t1.right, t2.left);
    }

    /**
     * Binary Tree Level Order Traversal
     * Given the root of a binary tree, return the level order traversal of its nodes'
     * values. (i.e., from left to right, level by level).
     * <p>
     * Input: root = [3,9,20,null,null,15,7]
     * Output: [[3],[9,20],[15,7]]
     * https://leetcode.com/problems/binary-tree-level-order-traversal/description/
     */
    @Test
    void testLevelOrderTraversal() {
        /**
         * input:
         *       1
         *     /  \
         *    2   2
         *   /\   /\
         *  3  4 4  3
         */
        TreeNode secondLv2LL = new TreeNode(3);
        TreeNode secondLv2LR = new TreeNode(4);
        TreeNode secondLv2RL = new TreeNode(4);
        TreeNode secondLv2RR = new TreeNode(3);
        TreeNode secondLvlRight = new TreeNode(2, secondLv2RL, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(2, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(1, secondLvlLeft, secondLvlRight);
        List<List<Integer>> results = levelOrder(root);

        Assertions.assertThat(results).hasSize(3);
        Assertions.assertThat(results.get(0)).containsExactly(1);
        Assertions.assertThat(results.get(1)).containsExactly(2, 2);
        Assertions.assertThat(results.get(2)).containsExactly(3, 4, 4, 3);
    }

    /**
     * Traversal the tree using Queue at BFS manner. Need to get the current queue size first, i.e. the numbers
     * of node added at last iteration. Then only remove those number of nodes from the queue and add them to
     * the list of that level
     * <p>
     * Time Complexity: O(N). Space Complexity: O(N)
     */
    List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null)
            return new ArrayList<>();
        List<List<Integer>> levels = new ArrayList<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> currentLvl = new ArrayList<>();
            // Get the total num of the nodes from the current queue size first cuz the size will change
            // when adding the child node while iterating
            int qSize = queue.size();
            // Only iterate the nodes in the current queue. They are all at the same level which were
            // added from the last iteration.
            for (int i = 0; i < qSize; i++) {
                TreeNode node = queue.poll();
                currentLvl.add(node.val);
                // Add child nodes to the queue, so they will be visited at next "level" iteration
                if (node.left != null)
                    queue.offer(node.left);
                if (node.right != null)
                    queue.offer(node.right);
            }
            levels.add(currentLvl);
        }
        return levels;
    }


    /**
     * Convert Sorted Array to Binary Search Tree
     * https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/description/
     */
    @Test
    void testSortedArrayToBST() {

        nums = new int[]{-10, -3, 0, 5, 9,};
        TreeNode root = sortedArrayToBST(nums);
        Assertions.assertThat(root.val).isEqualTo(0);
        Assertions.assertThat(root.left.val).isEqualTo(-10);
        Assertions.assertThat(root.right.val).isEqualTo(5);
        Assertions.assertThat(root.left.left).isNull();
        Assertions.assertThat(root.right.left).isNull();
        Assertions.assertThat(root.left.right.val).isEqualTo(-3);
        Assertions.assertThat(root.right.right.val).isEqualTo(9);
        /*
         * output:
         *       0
         *     /  \
         *   -10   5
         *     \    \
         *     -3    9
         */
    }

    int[] nums;

    /**
     * It's known that inorder traversal of BST is an array sorted in the ascending order. Therefore, having sorted array as an input, we could rewrite the problem as Construct Binary Search Tree from Inorder Traversal. However, inorder traversal is not a unique identifier of BST.
     * From these traversals, one could restore the inorder one:
     * inorder = sorted(postorder) = sorted(preorder)
     * Here we use Preorder traversal to construct the BST recursively from the array and the way we chose the
     * root is left middle element, i.e. rootIdx = (left + right) / 2
     * <p>
     * Algo:
     * Implement helper function helper(left, right),which constructs BST from nums elements between indexes left and right:
     * - If left > right, then there is no elements available for that subtree. Return None.
     * - Pick left middle element: rootIdx = (left + right) / 2.
     * - Initiate the root: root = TreeNode(nums[rootIdx]).
     * - Compute recursively left and right subtrees: root.left = helper(left, rootIdx - 1), root.right = helper(rootIdx + 1, right).
     * Return helper(0, len(nums) - 1).
     * Time Complexity: O(N). Space Complexity: O(logN) --> recursion stack
     */
    TreeNode helper(int left, int right) {
        if (left > right) return null;

        // always choose left middle node as a root
        int rootIdx = (left + right) / 2;

        // preorder traversal: node -> left -> right
        TreeNode root = new TreeNode(nums[rootIdx]);
        root.left = helper(left, rootIdx - 1);
        root.right = helper(rootIdx + 1, right);
        return root;
    }

    TreeNode sortedArrayToBST(int[] nums) {
        this.nums = nums; // input nums is always the sorted array
        return helper(0, nums.length - 1);
    }


    /**
     * Binary Tree Zigzag Level Order Traversal
     * Given the root of a binary tree, return the zigzag level order traversal of its
     * nodes' values. (i.e., from left to right, then right to left for the next level and alternate between).
     * <p>
     * Input: root = [3,9,20,null,null,15,7]
     * Output: [[3],[20,9],[15,7]]
     * https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/description/
     */
    @Test
    void testZigzagLevelOrder() {
        /**
         * input:
         *       3
         *     /  \
         *    9   20
         *        /\
         *       15  7
         */
        TreeNode thirdLvlRL = new TreeNode(15);
        TreeNode thirdLvlRR = new TreeNode(7);
        TreeNode secondLvlRight = new TreeNode(20, thirdLvlRL, thirdLvlRR);
        TreeNode secondLvlLeft = new TreeNode(9, null, null);
        TreeNode root = new TreeNode(3, secondLvlLeft, secondLvlRight);
        Assertions.assertThat(zigzagLevelOrder(root)).containsExactly(List.of(3), List.of(20, 9), List.of(15, 7));
    }

    /**
     * Use BFS traversal by level and maintain a boolean flag(iterateFromLeft). For the nodes at the
     * same level removed from the queue, if iterateFromLeft is true, add to the end of LinkedList,
     * otherwise, add to the head of LinkedList. After iterating one level, add LinkedList to the
     * result list and flip the iterateFromLeft flag.
     * <p>
     * Time Complexity: O(N), where N is the number of nodes in the tree.
     * Space Complexity: O(N) where N is the number of nodes in the tree.
     */
    List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        if (root == null)
            return new ArrayList<>();
        List<List<Integer>> results = new ArrayList<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        boolean iterateFromLeft = true;
        queue.offer(root);
        // Perform BFS traversal by level
        while (!queue.isEmpty()) {
            int qSize = queue.size();
            // Use LinkedList so we can insert at head/tail to create different order
            LinkedList<Integer> levelList = new LinkedList<>();
            for (int i = 0; i < qSize; i++) {
                TreeNode node = queue.poll();
                if (iterateFromLeft)
                    levelList.add(node.val);
                else
                    levelList.addFirst(node.val);
                if (node.left != null)
                    queue.offer(node.left);
                if (node.right != null)
                    queue.offer(node.right);
            }
            results.add(levelList);
            // Flip the direction before iterating the next level
            iterateFromLeft = !iterateFromLeft;
        }
        return results;
    }

    /**
     * Construct Binary Tree from Preorder and Inorder Traversal
     * Given two integer arrays preorder and inorder where preorder is the preorder traversal
     * of a binary tree and inorder is the inorder traversal of the same tree, construct and return the binary tree.
     * <p>
     * Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
     * Output: [3,9,20,null,null,15,7]
     * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/description/
     */
    @Test
    void testBuildTree() {
        /**
         * output:
         *       3
         *     /  \
         *    9   20
         *        /\
         *       15  7
         */
        TreeNode root = buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        Assertions.assertThat(root.val).isEqualTo(3);
        Assertions.assertThat(root.left.val).isEqualTo(9);
        Assertions.assertThat(root.right.val).isEqualTo(20);
        Assertions.assertThat(root.left.left).isNull();
        Assertions.assertThat(root.left.right).isNull();
        Assertions.assertThat(root.right.left.val).isEqualTo(15);
        Assertions.assertThat(root.right.right.val).isEqualTo(7);
    }

    Map<Integer, Integer> nodeValToInorderIdx = new HashMap<>();
    int preorderIdx = 0;

    /**
     * Recursion
     * Key points:
     * 1. Preorder traversal follows Root -> Left -> Right, therefore, given the preorder array preorder, we have easy access to the root which is preorder[0].
     * The sequence of building the nodes will be like this.
     * 2. Inorder traversal follows Left -> Root -> Right, therefore if we know the position of Root, we can recursively split the entire array into two subtrees.
     * This will be our main logic of recursive function to drive the tree structure. In short, inorder array gives info of the key structure.
     * <p>
     * Implementation details:
     * 1. We need to build a map to record the relation of value -> index at inorder array, so that we can find the position of root in constant time. The root index is needed so we can figure out the end/beginning index of the left/right
     * hand side range when making the recursive call.
     * 2. We need a preorderIndex instance var starting from 0. The sequence that we construct the node in the recursive function
     * is in fact the preorder traversal way, so we need to keep track of it when init the node and increment it
     * at the same time.
     * 3. Implement the recursion function arrayToTree which takes a range of inorder and returns the constructed binary tree:
     * - if the range is empty, return null;
     * - initialize the root with preorder[preorderIndex] and then increment preorderIndex;
     * - recursively use the left and right portions of inorder to construct the left and right subtrees.
     * <p>
     * Time Complexity: O(N), where N is the number of nodes in the tree.
     * Space Complexity: O(N)
     * Building the hashmap and storing the entire tree each requires O(N) memory.
     * The size of the implicit system stack used by recursion calls depends on the height of the tree,
     * which is O(N) in the worst case and O(logN) on average. Taking both into consideration, the space complexity is O(N).
     */
    TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            nodeValToInorderIdx.put(inorder[i], i);
        }
        return arrayToTree(preorder, 0, preorder.length - 1);
    }

    TreeNode arrayToTree(int[] preorder, int leftIdx, int rightIdx) {
        // leftIdx and rightIdx are the boundary range at inorder array when constructing the subtree in the recursive call
        // At each call, the range will eventually narrow down to the leaf node, and leftIdx will pass right rightIdx in the end, which
        // is the termination state, it sets null to both children of leaf node and return to the top.
        if (leftIdx > rightIdx)
            return null;
        int rootVal = preorder[preorderIdx];
        ++preorderIdx;
        TreeNode root = new TreeNode(rootVal);
        root.left = arrayToTree(preorder, leftIdx, nodeValToInorderIdx.get(rootVal) - 1);
        root.right = arrayToTree(preorder, nodeValToInorderIdx.get(rootVal) + 1, rightIdx);
        return root;
    }

    /**
     * Populating Next Right Pointers in Each Node
     * <p>
     * You are given a perfect binary tree where all leaves are on the same level, and every parent has two children. The binary tree has the following definition:
     * <p>
     * struct Node {
     * int val;
     * Node *left;
     * Node *right;
     * Node *next;
     * }
     * Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.
     * <p>
     * Initially, all next pointers are set to NULL.
     * https://leetcode.com/problems/populating-next-right-pointers-in-each-node/description/
     */
    @Test
    void testConnect() {
        Node root = new Node();
        connect(root);
    }

    private class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }


    /**
     * Using previously established next pointers
     * Key points:
     * 1. At any node, we can easily connect the next ptr of the left and right childdren nodes.
     * node.left.next = node.right
     * 2. To connect the children nodes having different parent, we can use the connection previously created between those parents.
     * When we are at level N, the connections among the nodes at current level built from the last round can be used here.
     * node.right.next = node.next.left
     * <p>
     * Algo:
     * 1. A leftMost ptr to only iterate the most left node, this tell us when we reach the leaf node
     * 2. A levelPtr ptr to iterate nodes at each level and build the two type of connections above.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public Node connect(Node root) {
        if (root == null)
            return null;
        // Start with the root node. There are no next pointers
        // that need to be set up on the first level
        Node leftMost = root;
        while (leftMost.left != null) {
            // Iterate the "linked list" starting from the head node and using the next pointers, establish the
            // corresponding links for the next level
            Node levelPtr = leftMost;
            while (levelPtr != null) {
                // Node w/ the same parent
                levelPtr.left.next = levelPtr.right;
                // Node w/ different parent
                if (levelPtr.next != null)
                    levelPtr.right.next = levelPtr.next.left;
                levelPtr = levelPtr.next;
            }
            // Move to the next level
            leftMost = leftMost.left;
        }
        return root;
    }

    /**
     * Kth Smallest Element in a BST
     * Given the root of a binary search tree, and an integer k, return the kth smallest value (1-indexed) of all the values of
     * the nodes in the tree.
     * <p>
     * Input: root = [3,1,4,null,2], k = 1
     * Output: 1
     * <p>
     * https://leetcode.com/problems/kth-smallest-element-in-a-bst/description/
     */
    @Test
    void testKthSmallest() {
        /**
         * input:
         *       3
         *     /  \
         *    1   4
         *     \
         *      2
         */
        TreeNode thirdLvlLR = new TreeNode(2);
        TreeNode secondLvlRight = new TreeNode(4, null, null);
        TreeNode secondLvlLeft = new TreeNode(1, null, thirdLvlLR);
        TreeNode root = new TreeNode(3, secondLvlLeft, secondLvlRight);
        Assertions.assertThat(kthSmallest(root, 1)).isEqualTo(1);
        Assertions.assertThat(kthSmallestRecursive(root, 1)).isEqualTo(1);
    }

    /**
     * Do Inorder traversal using stack to iteratively visit the tree. After popping the node from
     * the stack, decrement "k". If k becomes 0, the current node is the kth smallest node
     * <p>
     * Observation:
     * When doing inorder traversal over BST, we visit the node value in the ascending order.
     * <p>
     * Time complexity: O(H+k), where H is the tree height.
     * This complexity is defined by the stack, which contains at least H + k elements,
     * since before starting to pop out one has to go down to a leaf.
     * This results in O(log N + k) for the balanced tree and O(N + k) for completely
     * unbalanced tree with all the nodes in the left subtree.
     * <p>
     * Space complexity: O(H) to keep the stack, where H is a tree height.
     * That makes O(N) in the worst case of the skewed tree,
     * and O(log N) in the average case of the balanced tree.
     */
    int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode current = root;
        while (!stack.isEmpty() || current != null) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            current = stack.pop();
            if (--k == 0)
                return current.val;
            current = current.right;
        }
        return -1;
    }

    /**
     * Time complexity : O(N) to build a traversal.
     * Space complexity : O(N) to keep an inorder traversal.
     */
    int kthSmallestRecursive(TreeNode root, int k) {
        ArrayList<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result.get(k - 1);
    }

    void inorder(TreeNode node, ArrayList<Integer> result) {
        if (node != null) {
            inorder(node.left, result);
            result.add(node.val);
            inorder(node.right, result);
        }
    }

    /**
     * Inorder Successor in BST
     * Given the root of a binary search tree and a node p in it, return the in-order successor of that node in the BST. If the given node has no in-order successor in the tree, return null.
     * <p>
     * The successor of a node p is the node with the smallest key greater than p.val.
     * <p>
     * Input: root = [2,1,3], p = 1
     * Output: 2
     * Explanation: 1's in-order successor node is 2. Note that both p and the return value is of TreeNode type.
     * https://leetcode.com/problems/inorder-successor-in-bst/description/
     */
    @Test
    void testInorderSuccessor() {
        /**
         * input:
         *       2
         *     /  \
         *    1    3
         */
        TreeNode thirdLvlLR = new TreeNode(3);
        TreeNode secondLvlRight = new TreeNode(3, null, null);
        TreeNode secondLvlLeft = new TreeNode(1, null, null);
        TreeNode root = new TreeNode(2, secondLvlLeft, secondLvlRight);
        Assertions.assertThat(inorderSuccessor(root, secondLvlLeft)).isEqualTo(root);
    }

    /*
     * Using BST properties
     * In BST, the left descendants have smaller values than the current node and right
     * descendants have larger values than the current node. Therefore, by comparing the
     * values of the node p and the current node in the tree during our traversal, we can discard half of the remaining nodes at each step.
     * Algo:
     * 1. We start our traversal with the root node and continue the traversal until our current node reaches a null value,
     *    i.e. there are no more nodes left to process.
     * 2. At each step we compare the value of node p with that of node.
     * 	- If p.val >= node.val that implies we can safely discard the left subtree since all the nodes
     * 	  there including the current node have values less than p.
     * 	- However, if p.val < node.val, that implies that the successor must lie in the left subtree and
     * 	  that the current node is a potential candidate for inorder successor. Thus, we update our local
     * 	  variable for keeping track of the successor, successor, to node.
     * 3. Return successor.
     * Time Complexity: O(N), since we might end up encountering a skewed tree and in that case,
     * we will just be discarding one node at a time. For a balanced binary-search tree, however,
     * the time complexity will be O(logN) which is what we usually find in practice.
     * Space Complexity: O(1)
     */
    TreeNode inorderSuccessor(TreeNode current, TreeNode p) {
        TreeNode successor = null;
        while (current != null) { // until no more nodes to process
            if (p.val >= current.val)
                // This condition is the key of the logic.
                // This implies we can safely discard the left subtree since all the nodes there including the current node
                // have values less than p. We move to the right child cuz successor can only be at right subtree.
                current = current.right;
            else { // p < current
                // The current node is greater than p, which means current node can be the successor candidate(if p is its left child)
                // So we need to set the successor here.
                successor = current;
                // We move to the left child to explore other successor
                current = current.left;
            }
        }
        return successor;
    }

    /**
     * Binary Tree Inorder Traversal
     * Given the root of a binary tree, return the inorder traversal of its nodes' values.
     * <p>
     * Input: root = [1,null,2,3]
     * Output: [1,3,2]
     * https://leetcode.com/problems/binary-tree-inorder-traversal/description/
     */
    @Test
    void testInorderTraversal() {
        /**
         * input:
         *       1
         *        \
         *         2
         *        /
         *       3
         */
        TreeNode thirdLvlRL = new TreeNode(3);
        TreeNode secondLvlRight = new TreeNode(2, thirdLvlRL, null);
        TreeNode root = new TreeNode(1, null, secondLvlRight);
        Assertions.assertThat(inorderTraversalRecursive(root)).containsExactly(1, 3, 2);
        Assertions.assertThat(inorderTraversalIterative(root)).containsExactly(1, 3, 2);
    }

    /**
     * Time Complexity: O(N). Space Complexity: O(N)
     */
    List<Integer> inorderTraversalRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        visitNode(root, result);
        return result;
    }

    void visitNode(TreeNode node, List<Integer> result) {
        if (node != null) {
            visitNode(node.left, result);
            result.add(node.val);
            visitNode(node.right, result);
        }
    }

    /**
     * Initialize current node with root
     * While current is not null or stack is not empty
     * - Keep pushing left child onto stack, till we reach current node's left-most child
     * - Pop and visit the left-most node from stack
     * - Set current to the right child of the popped node
     * Note:
     * The left child nodes push earlier will be popped first, and they are basically the "root" of subtrees,
     * so will be visited before we pop its right child from the stack
     * Time Complexity: O(N). Space Complexity: O(N)
     */
    List<Integer> inorderTraversalIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null)
            return result;
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode current = root;

        while (!stack.isEmpty() || current != null) {
            while (current != null) {
                stack.push(current);
                // Keep pushing the left child to the stack, so they will be visited first later
                current = current.left;
            }
            current = stack.pop();
            result.add(current.val);
            current = current.right;
            // This right child node will be pushed to the stack at the next iteration. However, the inner
            // while loop will iteratively push its left childs to the stack. Therefore, the right child
            // will only be pop out after the left and parent nodes are pop and visited
        }
        return result;
    }


    /**
     * Lowest Common Ancestor of a Binary Search Tree
     * Given a binary search tree (BST), find the lowest common ancestor (LCA) node of two
     * given nodes in the BST.
     * <p>
     * According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined
     * between two nodes p and q as the lowest node in T that has both p and q as descendants
     * (where we allow a node to be a descendant of itself).”
     * <p>
     * Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
     * Output: 6
     * Explanation: The LCA of nodes 2 and 8 is 6.
     * <p>
     * Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4
     * Output: 2
     * Explanation: The LCA of nodes 2 and 4 is 2, since a node can be a descendant of itself
     * according to the LCA definition.
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/description/
     */
    @Test
    void testLowestCommonAncestorBST() {
        /**
         * input:
         *       6
         *     /  \
         *    2   8
         *   /\   /\
         *  0  4 7  9
         */
        TreeNode secondLv2LL = new TreeNode(0);
        TreeNode secondLv2LR = new TreeNode(4);
        TreeNode secondLv2RL = new TreeNode(7);
        TreeNode secondLv2RR = new TreeNode(9);
        TreeNode secondLvlRight = new TreeNode(8, secondLv2RL, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(2, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(6, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(lowestCommonAncestorBST(root, secondLvlLeft, secondLv2LR)).isEqualTo(secondLvlLeft);
    }

    /**
     * Start a ptr from root node, move ptr to right child if both node p and q are less than ptr node,
     * move ptr to left child if both node p and q are larger than ptr node. Otherwise, the current
     * ptr node is the LCA node.
     * <p>
     * Observation:
     * 1. We can use the BST property to know from a given node, whether node p and q are both on right/left
     * sub-tree or not.
     * 2. LCA node must be a node that makes the node p and q NOT both fall on the one of the sub-tree,
     * which means LCA must be one of the conditions:
     * p < LCA < q
     * q < LCA < p
     * p < LCA(q)
     * q < LCA(p)
     * LCA(p) < q
     * LCA(q) < p
     * <p>
     * Algo:
     * 1. Start traversing the tree from the root node.
     * 2. If both the nodes p and q are less than current node, move the ptr to right node.
     * 3. If both the nodes p and q are more than current node, move the ptr to left node.
     * 4. Otherwise, this means we have found the node which is common to node p's and q's subtrees.
     * and hence we return this common node as the LCA.
     * <p>
     * Time Complexity : O(N), where N is the number of nodes in the BST.
     * In the worst case we might be visiting all the nodes of the BST.
     * <p>
     * Space Complexity : O(1)
     */
    TreeNode lowestCommonAncestorBST(TreeNode node, TreeNode p, TreeNode q) {
        while (node != null) {
            if (p.val > node.val && q.val > node.val)
                node = node.right;
            else if (p.val < node.val && q.val < node.val) {
                node = node.left;
            } else // We have found the split point, i.e. the LCA node.
                return node;
        }
        return null;
    }

    /**
     * Lowest Common Ancestor of a Binary Tree
     * Given a binary tree, find the lowest common ancestor (LCA) of two given nodes
     * in the tree.
     * <p>
     * According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined
     * between two nodes p and q as the lowest node in T that has both p and q as descendants
     * (where we allow a node to be a descendant of itself).”
     * <p>
     * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
     * Output: 3
     * Explanation: The LCA of nodes 5 and 1 is 3.
     * <p>
     * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
     * Output: 5
     * Explanation: The LCA of nodes 5 and 4 is 5, since a node can be a descendant of itself
     * according to the LCA definition.
     * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/description/
     */
    @Test
    void testLowestCommonAncestor() {
        /**
         * input:
         *       3
         *     /  \
         *    5    1
         *   /\   /\
         *  6  2 0  8
         */
        TreeNode secondLv2LL = new TreeNode(6);
        TreeNode secondLv2LR = new TreeNode(2);
        TreeNode secondLv2RL = new TreeNode(0);
        TreeNode secondLv2RR = new TreeNode(8);
        TreeNode secondLvlRight = new TreeNode(1, secondLv2RL, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(5, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(3, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(lowestCommonAncestor(root, secondLvlLeft, secondLv2RR)).isEqualTo(root);
    }

    /**
     * First traversal the tree in BFS to build nodeToParent Map. Then build the Set(pAncestors) containing node
     * p and all its ancestor nodes by looking up the map. Then iterate from node q to all its ancestor node by
     * looking up the map and check if it is found in the pAncestors set. If so, that is LCA.
     * <p>
     * Observation:
     * We can't access the parent of a given node, so we need do some pre-process to get the child-parent data
     * stored at somewhere like Map first. So we can easily traversal from bottom up to visit its ancestors
     * form any node.
     * <p>
     * Algo:
     * 1. Perform BFS from root node and build the node to parent Map until it contains entry for node p and q
     * 2. Get all the ancestors for p using the nodeToParent map and add to a set(pAncestors) including p itself.
     * 3. Traverse node q and all it ancestors. If the ancestor is present in the ancestors set for p,
     * this means this is the first ancestor common between p and q and hence this is the LCA node.
     * <p>
     * Time Complexity : O(N), where N is the number of nodes in the binary tree.
     * In the worst case we might be visiting all the nodes of the binary tree.
     * <p>
     * O(N). In the worst case space utilized by the queue, the parent pointer map
     * and the ancestor set, would be N each, since the height of a skewed binary tree could be N.
     */
    TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        Deque<TreeNode> stack = new ArrayDeque<>(); // We can also use it as queue. Result here will be the same
        Map<TreeNode, TreeNode> nodeToParent = new HashMap<>();

        nodeToParent.put(root, null);
        stack.push(root);

        // Iterate until we find both the nodes p and q
        while (!nodeToParent.containsKey(p) || !nodeToParent.containsKey(q)) {
            TreeNode node = stack.pop();
            // While traversing the tree, keep saving the parent pointers.
            if (node.left != null) {
                nodeToParent.put(node.left, node);
                stack.push(node.left);
            }
            if (node.right != null) {
                nodeToParent.put(node.right, node);
                stack.push(node.right);
            }
        }

        Set<TreeNode> pAncestors = new HashSet<>();
        TreeNode pParent = p;
        while (pParent != null) { // Go upward the parent link until we reach the root, i.e. null parent
            pAncestors.add(pParent); // P itself should also be in the set cuz it can be q's ancestor
            pParent = nodeToParent.get(pParent);
        }
        TreeNode qParent = q; // Start from q itself
        while (!pAncestors.contains(qParent))
            // Cuz we go from q to all its ancestors, once we find a match in the qAncestor set, it is their
            // lowest common ancestor
            qParent = nodeToParent.get(qParent);

        return qParent;
    }

    /**
     * Balanced Binary Tree
     * Given a binary tree, determine if it is height-balanced.
     * <p>
     * Input: root = [3,9,20,null,null,15,7]
     * Output: true
     * <p>
     * Input: root = [1,2,2,3,3,null,null,4,4]
     * Output: false
     * <p>
     * https://leetcode.com/problems/balanced-binary-tree/description/
     */
    @Test
    void testIsBalanced() {
        /**
         * input:
         *       3
         *     /  \
         *    9   20
         *        /\
         *      15  7
         */
        TreeNode secondLv2RL = new TreeNode(15);
        TreeNode secondLv2RR = new TreeNode(7);
        TreeNode secondLvlRight = new TreeNode(20, secondLv2RL, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(9, null, null);
        TreeNode root = new TreeNode(3, secondLvlLeft, secondLvlRight);
        Assertions.assertThat(isBalanced(root)).isTrue();

        /**
         * input:
         *          1
         *        /  \
         *       2    2
         *      / \
         *     3   3
         *    / \
         *   4   4
         *  /
         * 5
         */
        TreeNode secondLv4LLL2 = new TreeNode(5);
        TreeNode secondLv3LLL2 = new TreeNode(4, secondLv4LLL2, null);
        TreeNode secondLv3LLR2 = new TreeNode(4);
        TreeNode secondLv2LL2 = new TreeNode(3, secondLv3LLL2, secondLv3LLR2);
        TreeNode secondLv2LR2 = new TreeNode(3);
        TreeNode secondLvlRight2 = new TreeNode(2, null, null);
        TreeNode secondLvlLeft2 = new TreeNode(2, secondLv2LL2, secondLv2LR2);
        TreeNode root2 = new TreeNode(1, secondLvlLeft2, secondLvlRight2);
        Assertions.assertThat(isBalanced(root2)).isFalse();
    }

    /**
     * Bottom-up recursion
     * Check if the child subtrees are balanced. If they are, use their heights to determine
     * if the current subtree is balanced as well as to calculate the current subtree's height.
     * <p>
     * The function does the following things
     * 1. Recursively call itself with the left child and check if returned node(subtree) is balanced.
     * Returns immediately if we find it is not balanced
     * 2. Recursively call itself with the right child and check if returned node(subtree) is balanced.
     * Returns immediately if we find it is not balanced
     * 3. If the difference between left and right tree is less than 2, return the height(Max(left, right)) and balanced:true to the caller
     * Otherwise, return balanced:false
     * <p>
     * Time complexity : O(n)
     * For every subtree, we compute its height in constant time as well as compare the height of its children.
     * <p>
     * Space complexity : O(n). The recursion stack may go up to O(n) if the tree is unbalanced.
     */
    private class TreeInfo {
        private int height;
        private boolean balanced;

        public TreeInfo(int height, boolean balanced) {
            this.height = height;
            this.balanced = balanced;
        }
    }

    TreeInfo isBalancedHelper(TreeNode node) {
        // base case: When we are at the leaf node child. An empty tree is balanced and has height = -1
        // leaf node should have height: 0
        if (node == null)
            return new TreeInfo(-1, true);
        TreeInfo left = isBalancedHelper(node.left);
        if (!left.balanced)
            // Once left/right tree returns balanced equal to false, we just bubble up the result, and it will propagate to
            // the root node in the end. The height value doesn't matter here once we find one subtree is not balanced.
            return new TreeInfo(-1, false);

        TreeInfo right = isBalancedHelper(node.right);
        if (!right.balanced)
            return new TreeInfo(-1, false);

        if (Math.abs(left.height - right.height) < 2)
            return new TreeInfo(Math.max(left.height, right.height) + 1, true);
        // When it is not balanced, the returned height value doesn't matter cuz we always check if left/right tree is
        // balanced first and return early.
        return new TreeInfo(-1, false);
    }

    boolean isBalanced(TreeNode root) {
        return isBalancedHelper(root).balanced;
    }

    /**
     * Serialize and Deserialize Binary Tree
     * Serialization is the process of converting a data structure or object into a sequence of
     * bits so that it can be stored in a file or memory buffer, or transmitted across a network
     * connection link to be reconstructed later in the same or another computer environment.
     * <p>
     * Design an algorithm to serialize and deserialize a binary tree. There is no restriction on
     * how your serialization/deserialization algorithm should work. You just need to ensure that
     * a binary tree can be serialized to a string and this string can be deserialized to the
     * original tree structure.
     * <p>
     * Clarification: The input/output format is the same as how LeetCode serializes a binary
     * tree. You do not necessarily need to follow this format, so please be creative and come
     * up with different approaches yourself.
     * <p>
     * Example
     * Input: root = [1,2,3,null,null,4,5]
     * Output: [1,2,3,null,null,4,5]
     * <p>
     * https://leetcode.com/problems/serialize-and-deserialize-binary-tree/description/
     */
    @Test
    void testCodec() {
        /**
         * input:
         *       1
         *     /  \
         *    2    5
         *   /\
         *  3  4
         */
        TreeNode secondLv2LL = new TreeNode(3);
        TreeNode secondLv2LR = new TreeNode(4);
        TreeNode secondLvlRight = new TreeNode(5, null, null);
        TreeNode secondLvlLeft = new TreeNode(2, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(1, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(serialize(root)).isEqualTo("1,2,3,null,null,4,null,null,5,null,null,");
        TreeNode newRoot = deserialize("1,2,3,null,null,4,null,null,5,null,null,");
        Assertions.assertThat(newRoot.val).isEqualTo(1);
        Assertions.assertThat(newRoot.left.val).isEqualTo(2);
        Assertions.assertThat(newRoot.left.left.val).isEqualTo(3);
        Assertions.assertThat(newRoot.left.right.val).isEqualTo(4);
        Assertions.assertThat(newRoot.left.left.left).isNull();
        Assertions.assertThat(newRoot.left.left.right).isNull();
        Assertions.assertThat(newRoot.right.val).isEqualTo(5);
    }


    /**
     * For serialization, use DFS(Preorder traversal) to generate the string
     * The output format we will use is preorder traversal, separated by comma, use "null" to denote the children
     * of the leaf node. The serialization process is in the order of
     * root -> left subtree -> right subtree (DFS preorder) in the recursive way
     * Time complexity : O(N)
     * where N is the number of nodes, i.e. the size of tree.
     * <p>
     * Space complexity : O(N)
     */
    String serialize(TreeNode root) {
        StringBuilder stb = new StringBuilder();
        serializeTreeToString(root, stb);
        return stb.toString();
    }

    void serializeTreeToString(TreeNode node, StringBuilder stb) {
        // base case: append "null," for the leaf node child
        if (node == null) {
            stb.append("null,");
            return;
        }
        stb.append(node.val).append(",");
        serializeTreeToString(node.left, stb);
        serializeTreeToString(node.right, stb);
    }

    /**
     * For deserialization, use DFS(Preorder traversal) to process the string
     * The input format we will use is preorder traversal, separated by comma, use "null" to denote the children
     * of the leaf node. The deserialization process to instantiate the node is in the order of
     * root -> left subtree -> right subtree (DFS preorder). We need to split the input string and put in a
     * LinkedList and keep removing the head element after generating a tree node.
     * Time complexity : O(N)
     * where N is the number of nodes, i.e. the size of tree.
     * <p>
     * Space complexity : O(N)
     */
    TreeNode deserialize(String data) {
        String[] nodeArray = data.split(",");
        return deserializeStrToTree(new LinkedList<>(Arrays.asList(nodeArray)));
    }

    TreeNode deserializeStrToTree(LinkedList<String> treeNodes) {
        // base case: just return null for the leaf node child
        if (treeNodes.getFirst().equals("null")) {
            treeNodes.removeFirst();
            return null;
        }
        TreeNode node = new TreeNode(Integer.parseInt(treeNodes.getFirst()));
        treeNodes.removeFirst();
        node.left = deserializeStrToTree(treeNodes);
        node.right = deserializeStrToTree(treeNodes);
        return node;
    }

    /**
     * Path Sum
     * Given the root of a binary tree and an integer targetSum, return true if the tree has a root-to-leaf
     * path such that adding up all the values along the path equals targetSum.
     * <p>
     * A leaf is a node with no children.
     * <p>
     * Input: root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
     * Output: true
     * Explanation: The root-to-leaf path with the target sum is shown.
     * <p>
     * Input: root = [1,2,3], targetSum = 5
     * Output: false
     * Explanation: There two root-to-leaf paths in the tree:
     * (1 --> 2): The sum is 3.
     * (1 --> 3): The sum is 4.
     * There is no root-to-leaf path with sum = 5.
     * <p>
     * https://leetcode.com/problems/path-sum/
     */
    @Test
    void testHasPathSum() {
        /**
         * input:
         *        5
         *      /  \
         *     4    8
         *    /    /\
         *   11  13  4
         *  / \       \
         * 7   2       1
         */
        TreeNode secondLv3LLL = new TreeNode(7);
        TreeNode secondLv3LLR = new TreeNode(2);
        TreeNode secondLv2LL = new TreeNode(11, secondLv3LLL, secondLv3LLR);
        TreeNode secondLv2RL = new TreeNode(13);
        TreeNode secondLv3RRR = new TreeNode(1);
        TreeNode secondLv2RR = new TreeNode(4, null, secondLv3RRR);
        TreeNode secondLvlRight = new TreeNode(8, secondLv2RL, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(4, secondLv2LL, null);
        TreeNode root = new TreeNode(5, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(hasPathSum(root, 22)).isTrue();
    }

    /**
     * Use recursive DFS traversal. At each node, first subtract the current node value from the target sum.
     * If current node is a leaf node, check if target sum has become 0. If not leaf node, recursively call for
     * left and right child w/ the updated target sum.
     * <p>
     * Time complexity: O(n)
     * <p>
     * Space complexity: O(n) when the tree is completely unbalanced.
     * O(log n) if tree is completely balanced(Tree height: log n)
     */
    boolean hasPathSum(TreeNode root, int targetSum) {
        // Base case and when the root node is null
        if (root == null)
            return false;
        targetSum -= root.val;
        // 2nd base case
        if (root.left == null && root.right == null)
            // We stop at the real leaf node and don't traversal down to its null child
            // Here we check if target is reduced to 0. If so, we find a path
            return targetSum == 0;
        return hasPathSum(root.left, targetSum) || hasPathSum(root.right, targetSum);
    }

    /**
     * Path Sum II
     * Given the root of a binary tree and an integer targetSum, return all root-to-leaf paths where the
     * sum of the node values in the path equals targetSum. Each path should be returned as a list of the
     * node values, not node references.
     * <p>
     * A root-to-leaf path is a path starting from the root and ending at any leaf node. A leaf is a node
     * with no children.
     * <p>
     * Input: root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
     * Output: [[5,4,11,2],[5,8,4,5]]
     * Explanation: There are two paths whose sum equals targetSum:
     * 5 + 4 + 11 + 2 = 22
     * 5 + 8 + 4 + 5 = 22
     * <p>
     * https://leetcode.com/problems/path-sum-ii/description/
     */
    @Test
    void testPathSumII() {
        /**
         * input:
         *        5
         *      /  \
         *     4    8
         *    /    /\
         *   11  13  4
         *  / \     / \
         * 7   2   5   1
         */
        TreeNode secondLv3LLL = new TreeNode(7);
        TreeNode secondLv3LLR = new TreeNode(2);
        TreeNode secondLv2LL = new TreeNode(11, secondLv3LLL, secondLv3LLR);
        TreeNode secondLv2RL = new TreeNode(13);
        TreeNode secondLv3RLR = new TreeNode(5);
        TreeNode secondLv3RRR = new TreeNode(1);
        TreeNode secondLv2RR = new TreeNode(4, secondLv3RLR, secondLv3RRR);
        TreeNode secondLvlRight = new TreeNode(8, secondLv2RL, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(4, secondLv2LL, null);
        TreeNode root = new TreeNode(5, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(pathSumII(root, 22)).containsOnly(List.of(5, 4, 11, 2), List.of(5, 8, 4, 5));
    }

    /**
     * First update target sum by subtracting the current node value and add the node value to the path list.
     * If the current node is leaf node and target sum is equal to 0, add the path list to the result list.
     * Otherwise, recursively call w/ updated target sum and left child and right child separately.
     * Finally, remove the current node from the path list before backtrack.
     * <p>
     * Observation:
     * The question asks to return ALL root-to-leaf paths. This implies we need to explore all traversal
     * path in the tree that meet the condition. Therefore, we should use recursion w/ backtracking
     * <p>
     * Time complexity: O(N⋅logN) where N is the number of nodes in a tree. In the worst case, we could have a
     * complete binary tree, then there would be N/2 leafs and tree height is logN. Say if every leaf node
     * can match the target sum.
     * 1. Traversal to a leaf node: O(logN)
     * 2. Copy the path to the final result list: O(logN)
     * We will do (1) and (2) on total N/2 leaf nodes, therefore, O(N/2 * (logN + logN)) = O(N⋅logN)
     * <p>
     * <p>
     * Space complexity: O(n)
     * The path list to keep track of the nodes along a branch
     */
    List<List<Integer>> pathSumII(TreeNode root, int targetSum) {
        if (root == null)
            return new ArrayList<>();
        List<List<Integer>> result = new ArrayList<>();
        findTargetPathSumNode(root, targetSum, new ArrayList<>(), result);
        return result;
    }

    void findTargetPathSumNode(TreeNode node, int targetSum, List<Integer> path, List<List<Integer>> result) {
        // Base case
        if (node == null)
            return;
        targetSum -= node.val;
        path.add(node.val);

        if (node.left == null && node.right == null) {
            // We are at the leaf node, so check if this is a path reducing the target sum to zero
            if (targetSum == 0)
                result.add(new ArrayList<>(path));
        } else {
            // For any internal nodes, recursively check left & right subtree
            findTargetPathSumNode(node.left, targetSum, path, result);
            findTargetPathSumNode(node.right, targetSum, path, result);
        }
        // Need to remove the last node we just visited before backtrack
        path.remove(path.size() - 1);
    }

    /**
     * Diameter of Binary Tree
     * Given the root of a binary tree, return the length of the diameter of the tree.
     * The diameter of a binary tree is the length of the longest path between any two nodes
     * in a tree. This path may or may not pass through the root.
     * <p>
     * The length of a path between two nodes is represented by the number of edges between them.
     * <p>
     * Input: root = [1,2,3,4,5]
     * Output: 3
     * Explanation: 3 is the length of the path [4,2,1,3] or [5,2,1,3].
     * <p>
     * https://leetcode.com/problems/diameter-of-binary-tree/description/
     */
    @Test
    void testDiameterOfBinaryTree() {
        /**
         * input:
         *       1
         *     /  \
         *    2    3
         *   /\
         *  4  5
         */
        TreeNode secondLv2LL = new TreeNode(4);
        TreeNode secondLv2LR = new TreeNode(5);
        TreeNode secondLvlRight = new TreeNode(3, null, null);
        TreeNode secondLvlLeft = new TreeNode(2, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(1, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(diameterOfBinaryTree(root)).isEqualTo(3);
        Assertions.assertThat(diameterOfBinaryTreeWithPathTest(root)).isEqualTo(3);
    }

    /**
     * Recursively get the longest paths from current node to leaf for the left and right subtree.
     * Update the longestPathOfTwoNodes w/ the sum of two paths(left & right), finally return the larger
     * one between the left and right + 1 when backtrack to the parent node.
     * <p>
     * Observation:
     * 1. The "path" in the question is basically the edge count from a node to the other.
     * 2. The longest path should be between two leaf nodes when it is NOT a skewed tree.
     * Otherwise, it is guarantee that one of two nodes must be leaf.
     * 3. The longest path of two nodes in the tree must go thru a node having the max sum of
     * the longest path from its left subtree and right subtree.
     * <p>
     * Algo:
     * 1. Recursive call to get the longest path from left subtree
     * 2. Recursive call to get the longest path from right subtree
     * 3. Updates the current longest path of two node with the sum of path of left and right subtree above
     * 4. Returns the max of the longest path from left subtree and the longest path from right subtree and
     * plus one (The returned value(path length/edge count) needs to include the edge connecting to current
     * node's parent when we backtrack to the parent node)
     * <p>
     * Time complexity: O(N)
     * Space complexity: O(N). The space complexity depends on the size of our implicit call stack during our DFS,
     * which relates to the height of the tree. In the worst case, the tree is skewed so the height of the tree
     * is O(N). If the tree is balanced, it'd be O(log N).
     */
    int diameterOfBinaryTree(TreeNode root) {
        AtomicInteger diameter = new AtomicInteger();
        findLongestPathFromNode(root, diameter);
        return diameter.intValue();
    }

    /**
     * @param node                  The current visiting node
     * @param longestPathOfTwoNodes The max length of path between two nodes in the tree
     * @return The max length of path(edge count) to a leaf node when traversal/extending from this node
     */
    int findLongestPathFromNode(TreeNode node, AtomicInteger longestPathOfTwoNodes) {
        if (node == null)
            return 0; // return 0 cuz we came from leaf node. No edge
        // Get max edge count from the left subtree
        int longestPathToLeafFromLeft = findLongestPathFromNode(node.left, longestPathOfTwoNodes);
        // Get max edge count from the right subtree
        int longestPathToLeafFromRight = findLongestPathFromNode(node.right, longestPathOfTwoNodes);
        // Update the longestPathOfTwoNodes so far. The two nodes from left and right subtree may form the longest path
        // in the tree. We need to keep track of it.
        longestPathOfTwoNodes.set(Math.max(longestPathToLeafFromLeft + longestPathToLeafFromRight, longestPathOfTwoNodes.intValue()));
        // Return the longest one between left and right. Plus 1 cuz the returned value(path length/edge count) needs to
        // include the edge connecting to current node's parent when we backtrack to the parent node.
        // This is the case that ONLY one of the current node's branches may be just part of the longest path. In other words,
        // current node may not be the "turning point" where the path turns from left subtree to right subtree in the final
        // longest path. However, this will be determined after visiting all nodes.
        // When the path goes thru the current node, it can only branch to left or right side.
        return Math.max(longestPathToLeafFromLeft, longestPathToLeafFromRight) + 1;
    }


    int diameterOfBinaryTreeWithPathTest(TreeNode root) {
        AtomicInteger diameter = new AtomicInteger();
        findLongestPathFromNodeWithPath(root, diameter);
        return diameter.intValue();
    }

    // The list of nodes from the leftmost to the rightmost
    List<Integer> longestPathNodes = new ArrayList<>();

    /**
     * This is the updated method from findLongestPathFromNode with output the path of the longest diameter
     */
    Pair<Integer, List<Integer>> findLongestPathFromNodeWithPath(TreeNode node, AtomicInteger diameter) {
        if (node == null)
            return new Pair<>(0, new ArrayList<>());
        // Pair[Longest path length to leaf from left, List of nodes from the leaf]
        Pair<Integer, List<Integer>> leftPath = findLongestPathFromNodeWithPath(node.left, diameter);
        // Pair[Longest path length to leaf from right, List of nodes from the leaf]
        Pair<Integer, List<Integer>> rightPath = findLongestPathFromNodeWithPath(node.right, diameter);

        if (leftPath.getKey() > 0)
            leftPath.getValue().add(node.left.val);

        if (rightPath.getKey() > 0)
            rightPath.getValue().add(node.right.val);

        int pathSum = leftPath.getKey() + rightPath.getKey();
        if (pathSum > 0 && pathSum >= diameter.intValue()) {
            diameter.set(pathSum);
            longestPathNodes = new ArrayList<>(leftPath.getValue());
            longestPathNodes.add(node.val);
            List<Integer> reversedRightPath = new ArrayList<>(rightPath.getValue());
            // The order of the rightPath is from the leaf to the current node, so we want to reverse it before add
            // them to the left path (or we can just iterate it in reverse order)
            Collections.reverse(reversedRightPath);
            longestPathNodes.addAll(reversedRightPath);
        }

        if (leftPath.getKey() >= rightPath.getKey())
            return new Pair<>(leftPath.getKey() + 1, leftPath.getValue());
        else
            return new Pair<>(rightPath.getKey() + 1, rightPath.getValue());
    }

    /**
     * Binary Tree Maximum Path Sum
     * A path in a binary tree is a sequence of nodes where each pair of adjacent nodes in the
     * sequence has an edge connecting them. A node can only appear in the sequence at most once.
     * Note that the path does not need to pass through the root.
     * <p>
     * The path sum of a path is the sum of the node's values in the path.
     * <p>
     * Given the root of a binary tree, return the maximum path sum of any non-empty path.
     * <p>
     * Input: root = [1,2,3]
     * Output: 6
     * Explanation: The optimal path is 2 -> 1 -> 3 with a path sum of 2 + 1 + 3 = 6.
     * <p>
     * Input: root = [-10,9,20,null,null,15,7]
     * Output: 42
     * Explanation: The optimal path is 15 -> 20 -> 7 with a path sum of 15 + 20 + 7 = 42.
     * <p>
     * https://leetcode.com/problems/binary-tree-maximum-path-sum/description/
     */
    @Test
    void testMaxPathSum() {
        /**
         * input:
         *      -10
         *     /  \
         *    9    20
         *         /\
         *       15  7
         */
        TreeNode secondLv2RL = new TreeNode(15);
        TreeNode secondLv2RR = new TreeNode(7);
        TreeNode secondLvlRight = new TreeNode(20, secondLv2RL, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(9, null, null);
        TreeNode root = new TreeNode(-10, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(maxPathSum(root)).isEqualTo(42);
        TreeNode tree = createBinaryTree(9, 6, -3, null, null, -6, 2, null, null, 2, null, -6, -6, -6);
        Assertions.assertThat(maxPathSum(tree)).isEqualTo(16); // [6 9 -3 2 2]
    }


    /**
     * Recursively get the max path sum from the left node and the right node (DFS). Set any negative returned
     * path sum to zero so it will be ignored when calculating the total path sum. Then sum up the current node
     * value and left & right path sum and update the the max path sum if possible. Finally returns the current
     * node value + the bigger path sum of the left/right node
     * <p>
     * Note: This problem uses the same pattern as "Diameter of Binary Tree"
     * <p>
     * Observation:
     * The maximum path sum can be form in three different use cases.
     * <p>
     * 1. The path is consist of a path from a node's left subtree through it and to its right subtree.
     * This node can be ANY node in the tree.
     * Ex: [1,2,3] --> 6
     * <p>
     * 2. The path is a segment cross one or more nodes on a node's left or right subtree. In other words,
     * one of the subtrees is ignored cuz it doesn't increase the total path sum.
     * Ex: [1, 2, -5] --> 3 (path: 2 - 1)
     * <p>
     * 3. The path doesn't involve any child. The node itself is the only element of the path with maximum sum.
     * Ex: [-10, 5, -2] --> 5
     * <p>
     * We can see that we need to process the children before we process a node. This indicates that we need to
     * perform a post-order DFS traversal of the tree because, in post-order, children are processed before the parent.
     * <p>
     * In each recursive DFS call, we need to take care of the above 3 cases.
     * <p>
     * 1. Recursively call on the current node's left child to get the max path sum from the left subtree.
     * <p>
     * 2. Recursively call on the current node's right child to get the max path sum from the right subtree
     * <p>
     * 3. If any one of the returned path sum is negative, set it to zero first so it will have no effect when
     * computing the path sum later. Cuz negative path sum will only decrease the total path sum, we want to
     * ignore it (Use case #2 & #3)
     * <p>
     * 4. Compute sum of path formed from left subtree path sum + current node value + right subtree path
     * sum. (Use case #1) We keep track of the max path sum and update it accordingly.
     * <p>
     * 5. Return the max path sum when traversal through this node. This means the current node value +
     * the larger path sum from left subtree or the right subtree. The path can only branch out to one
     * side, so we take the one w/ bigger path sum(They can be 0 at this point cuz we do at step 3)
     * <p>
     * Time complexity: O(N)
     * <p>
     * Space complexity: O(N). The space complexity depends on the size of our implicit call stack during our DFS,
     * which relates to the height of the tree. In the worst case, the tree is skewed so the height of the tree
     * is O(N). If the tree is balanced, it'd be O(log N).
     */
    int maxPathSum(TreeNode root) {
        AtomicInteger maxPathSum = new AtomicInteger(Integer.MIN_VALUE);
        findMaxPathSumFromNode(root, maxPathSum);
        return maxPathSum.intValue();
    }

    int findMaxPathSumFromNode(TreeNode node, AtomicInteger maxPathSum) {
        if (node == null)
            return 0;
        int leftPathSum = findMaxPathSumFromNode(node.left, maxPathSum);
        int rightPathSum = findMaxPathSumFromNode(node.right, maxPathSum);
        // If the path sum from the left/right is negative, we want to ignore it and not include it in the path,
        // this will be taken care of by just count it as 0 due to the way we compute the maxPathSum
        leftPathSum = Math.max(leftPathSum, 0);
        rightPathSum = Math.max(rightPathSum, 0);

        // Update the maxPathSum if the current node + leftPath + rightPath produces the max path sum so far
        maxPathSum.set(Math.max(maxPathSum.intValue(), node.val + leftPathSum + rightPathSum));

        // Return path sum of current node and larger child's path sum from either left or right side
        // This is the case that the current node and either left or right child may be part of the final max path sum
        // When the path goes thru the current node, it can only branch to left or right side.
        return node.val + Math.max(leftPathSum, rightPathSum);
    }

    /**
     * Maximum Width of Binary Tree
     * Given the root of a binary tree, return the maximum width of the given tree.
     * <p>
     * The maximum width of a tree is the maximum width among all levels.
     * <p>
     * The width of one level is defined as the length between the end-nodes (the leftmost and rightmost non-null nodes),
     * where the null nodes between the end-nodes that would be present in a complete binary tree extending down to
     * that level are also counted into the length calculation.
     * <p>
     * It is guaranteed that the answer will in the range of a 32-bit signed integer.
     * <p>
     * Input: root = [1,3,2,5,3,null,9]
     * Output: 4
     * Explanation: The maximum width exists in the third level with length 4 (5,3,null,9).
     * <p>
     * Input: root = [1,3,2,5,null,null,9,6,null,7]
     * Output: 7
     * Explanation: The maximum width exists in the fourth level with length 7 (6,null,null,null,null,null,7).
     * <p>
     * Input: root = [1,3,2,5]
     * Output: 2
     * Explanation: The maximum width exists in the second level with length 2 (3,2).
     * <p>
     * https://leetcode.com/problems/maximum-width-of-binary-tree/description/
     */
    @Test
    void testWidthOfBinaryTree() {
        /**
         * input:
         *       1
         *     /  \
         *    3    2
         *   /\     \
         *  5  3     9
         */
        TreeNode secondLv2LL = new TreeNode(5);
        TreeNode secondLv2LR = new TreeNode(3);
        TreeNode secondLv2RR = new TreeNode(9);
        TreeNode secondLvlRight = new TreeNode(2, null, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(3, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(1, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(widthOfBinaryTree(root)).isEqualTo(4);
    }

    /**
     * Use BFS traversal by level. Assign every child node an id computed from the current node removed from the queue.
     * Get the id of the first and last nodes at the same level. At end of each level, update and calculate
     * the width using lastNodeId - firstNodeId + 1
     * <p>
     * The problem can be rephrased as to find the maximum node count between two nodes at each level, however, the
     * null nodes between these two nodes are also counted.
     * <p>
     * There are two key things to solve the problem.
     * 1. To assign id/index to every node so given two nodes at the same level, we can compute the width/node count
     * between them even if there are some nodes are missing.
     * - If there are two or more nodes at the same level, width = (last node id - first node id + 1)
     * <p>
     * 2. Need an algorithm to assign the node id sequentially from the root node. Cuz some nodes may be missing,
     * we must do it from the root and every node id should be derived from the parent node id.
     * - For a node w/ id equal to n, left child id = 2*n + 1, right child id = 2*n + 2
     * <p>
     * With above two formulas, we just need to perform the BFS traversal by level.
     * For each level get the first and last node ids, and put their child node w/ id into the queue.
     * At the end of each level, we use those two node ids to compute the width and update the current max width.
     * <p>
     * Time Complexity: O(N)
     * <p>
     * Space Complexity: O(N)
     * Due to the nature of BFS, at any given moment, the queue holds no more than two levels of nodes. In the worst case,
     * a level in a full binary tree contains at most half of the total nodes, i.e. N/2.
     */
    int widthOfBinaryTree(TreeNode root) {
        if (root == null)
            return 0;
        int maxWidth = 1; // Starts w/ 1 cuz we have one root node in the beginning
        Queue<Pair<TreeNode, Integer>> queue = new ArrayDeque<>();
        queue.offer(new Pair<>(root, 0)); // Root node id starts with 0
        while (!queue.isEmpty()) {
            // each iteration is one level of tree
            int qSize = queue.size();
            int firstNodeId = -1;
            int lastNodeId = -1;
            for (int i = 0; i < qSize; i++) {
                Pair<TreeNode, Integer> node = queue.poll();
                int nodeId = node.getValue();
                if (i == 0)
                    firstNodeId = nodeId;
                if (i == qSize - 1)
                    lastNodeId = nodeId;

                // Put the child node to the queue
                // For a node w/ id equal to n
                // left child id = 2*n + 1
                // right child id = 2*n + 2
                if (node.getKey().left != null)
                    queue.offer(new Pair<>(node.getKey().left, nodeId * 2 + 1));
                if (node.getKey().right != null)
                    queue.offer(new Pair<>(node.getKey().right, nodeId * 2 + 2));
            }
            // Update the maxWidth ONLY when there are two nodes at this level, so we can compute the width
            if (qSize > 1)
                // Width = last node id - first node id + 1
                maxWidth = Math.max(maxWidth, lastNodeId - firstNodeId + 1);
        }
        return maxWidth;
    }

    /**
     * Binary Tree Right Side View
     * Given the root of a binary tree, imagine yourself standing on the right side of it, return the values of
     * the nodes you can see ordered from top to bottom.
     * <p>
     * Input: root = [1,2,3,null,5,null,4]
     * Output: [1,3,4]
     * <p>
     * Input: root = [1,null,3]
     * Output: [1,3]
     * <p>
     * https://leetcode.com/problems/binary-tree-right-side-view/description/
     */
    @Test
    void testRightSideView() {
        /**
         * input:
         *       1
         *     /  \
         *    2    3
         *     \    \
         *      5    4
         */
        TreeNode secondLv2LR = new TreeNode(5);
        TreeNode secondLv2RR = new TreeNode(4);
        TreeNode secondLvlRight = new TreeNode(3, null, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(2, null, secondLv2LR);
        TreeNode root = new TreeNode(1, secondLvlLeft, secondLvlRight);

        Assertions.assertThat(rightSideView(root)).containsExactly(1, 3, 4);
    }

    /**
     * Use BFS traversal by level to get the last node at each level and add to the returned result
     * <p>
     * Time complexity: O(N)
     * <p>
     * Space complexity: O(D) to keep the queues, where D is a tree diameter.
     * If use the last level to estimate the queue size.
     * This level could contain up to N/2 tree nodes in the case of complete binary tree.
     */
    List<Integer> rightSideView(TreeNode root) {
        if (root == null)
            return new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            // Perform BFS level traversal
            int lvlSize = queue.size();
            for (int i = 0; i < lvlSize; i++) {
                TreeNode node = queue.poll();
                if (i == lvlSize - 1)
                    // The last node at each level is what we want
                    result.add(node.val);
                if (node.left != null)
                    queue.offer(node.left);
                if (node.right != null)
                    queue.offer(node.right);
            }
        }
        return result;
    }

    /**
     * Invert Binary Tree
     * Given the root of a binary tree, invert the tree, and return its root.
     * <p>
     * Input: root = [4,2,7,1,3,6,9]
     * Output: [4,7,2,9,6,3,1]
     * <p>
     * Input: root = [2,1,3]
     * Output: [2,3,1]
     * <p>
     * https://leetcode.com/problems/invert-binary-tree/description/
     */
    @Test
    void testInvertTree() {
        /**
         * input:
         *       4
         *     /  \
         *    2    7
         *   /\   /\
         *  1  3 6  9
         */
        TreeNode secondLv2LL = new TreeNode(1);
        TreeNode secondLv2LR = new TreeNode(3);
        TreeNode secondLv2RL = new TreeNode(6);
        TreeNode secondLv2RR = new TreeNode(9);
        TreeNode secondLvlRight = new TreeNode(7, secondLv2RL, secondLv2RR);
        TreeNode secondLvlLeft = new TreeNode(2, secondLv2LL, secondLv2LR);
        TreeNode root = new TreeNode(4, secondLvlLeft, secondLvlRight);

        root = invertTree(root);
        /**
         * Expected output:
         *       4
         *     /  \
         *    7    2
         *   /\   /\
         *  9  6 3  1
         */
        Assertions.assertThat(root.val).isEqualTo(4);
        Assertions.assertThat(root.left.val).isEqualTo(7);
        Assertions.assertThat(root.right.val).isEqualTo(2);
        Assertions.assertThat(root.left.left.val).isEqualTo(9);
        Assertions.assertThat(root.left.right.val).isEqualTo(6);
        Assertions.assertThat(root.right.left.val).isEqualTo(3);
        Assertions.assertThat(root.right.right.val).isEqualTo(1);
    }

    /**
     * Use BFS traversal w/ queue. Iteratively remove the node from the queue, and then swap
     * the right and left child node. Then add the right and left node to the queue if not null.
     * <p>
     * Time complexity: O(N)
     * <p>
     * Space complexity: O(n)
     * In the worst case, the queue will contain all nodes in one level of the binary tree.
     * For a full binary tree, the leaf level has n/2 = O(n) leaves.
     */
    TreeNode invertTree(TreeNode root) {
        if (root == null)
            return null;
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            // Perform BFS traversal
            TreeNode node = queue.poll();
            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;
            if (node.left != null)
                queue.offer(node.left);
            if (node.right != null)
                queue.offer(node.right);
        }
        return root;
    }

    /**
     * Binary Tree Vertical Order Traversal
     * Given the root of a binary tree, return the vertical order traversal of its nodes'
     * values. (First from column by column, then for each column go from top to bottom).
     * <p>
     * If two nodes are in the same row and column, the order should be from left to right.
     * <p>
     * Note: A node with two child means it span cross 3 columns, which means a left node
     * is at the left column of the root's column, and right node is at the right column
     * of the root's column
     * <p>
     * Input: root = [3,9,20,null,null,15,7]
     * Output: [[9],[3,15],[20],[7]]
     * <p>
     * Input: root = [3,9,8,4,0,1,7]
     * Output: [[4],[9],[3,0,1],[8],[7]]
     * <p>
     * https://leetcode.com/problems/binary-tree-vertical-order-traversal/description/
     */
    @Test
    void testVerticalOrder() {
        TreeNode root = createBinaryTree(3, 9, 20, null, null, 15, 7);
        Assertions.assertThat(verticalOrder(root)).containsExactly(List.of(9), List.of(3, 15), List.of(20), List.of(7));
        root = createBinaryTree(1);
        Assertions.assertThat(verticalOrder(root)).containsExactly(List.of(1));
    }

    /**
     * Use BFS level traversal and assign each node a column index and use the map(colIdxToNodeValList)
     * to group the node w/ the same column at traversal. The left child node has parent colIdx-1,
     * and right hcild has parent colIdx+1. Also need to track the minColIdx and maxColIdx so the
     * result can be built from left to right column order.
     * <p>
     * Observation:
     * 1. Need a systematic way to determine what column a node is at. We can assign column index
     * to each node. The root node starts at 0, right child column index is parent's + 1, and left
     * child column index is parent's - 1. So we will be able to group the nodes at the same column
     * <p>
     * 2. Using BFS can let us scan from top to bottom and and group the nodes with the same column
     * 3. We also need to keep track of the leftmost(min) and the rightmost(max) column index
     * <p>
     * Algo:
     * 1. Add the root node w/ column idx(0) to the queue
     * 2. Perform BFS level traversal, for each node at the same level
     * - Add the node removed from the queue to the colIdxToNodeValList map
     * - Add the left node w/ parent node column idx - 1
     * - Update the minColIdx
     * - Add the right node w/ parent node column idx + 1
     * - Update the maxColIdx
     * 3. Iterate from minColIdx to maxColIdx and add the list from colIdxToNodeValList map
     * to the result list.
     * <p>
     * Time complexity: O(n)
     * Space complexity: O(n)
     */
    List<List<Integer>> verticalOrder(TreeNode root) {
        if (root == null)
            return new ArrayList<>();
        List<List<Integer>> results = new ArrayList<>();
        // Each entry is the column index to the list of node values that has the same column index, i.e. at the same column
        Map<Integer, List<Integer>> colIdxToNodeValList = new HashMap<>();
        // Need to keep track of the leftmost and rightmost column index, so we construct the result list from left
        // column to right from the map
        int maxColIdx = 0, minColIdx = 0;
        Queue<Pair<TreeNode, Integer>> queue = new ArrayDeque<>();
        // Perform BFS traversal by level
        queue.offer(new Pair<>(root, 0)); // root node has column index 0
        while (!queue.isEmpty()) {
            int qSize = queue.size();
            for (int i = 0; i < qSize; i++) {
                Pair<TreeNode, Integer> node = queue.poll();
                // The top node is at the head of the list cuz the BFS
                colIdxToNodeValList.computeIfAbsent(node.getValue(), k -> new ArrayList<>()).add(node.getKey().val);
                if (node.getKey().left != null) {
                    int lColIdx = node.getValue() - 1; // Left node has parent node column index - 1
                    minColIdx = Math.min(minColIdx, lColIdx);
                    queue.offer(new Pair<>(node.getKey().left, lColIdx));
                }
                if (node.getKey().right != null) {
                    int rColIdx = node.getValue() + 1; // Right node has parent node column index + 1
                    maxColIdx = Math.max(maxColIdx, rColIdx);
                    queue.offer(new Pair<>(node.getKey().right, rColIdx));
                }
            }
        }
        // Iterate from the min column idx to the max, so it is added to the result from left to right column direction
        for (int i = minColIdx; i <= maxColIdx; i++) {
            if (colIdxToNodeValList.containsKey(minColIdx))
                results.add(colIdxToNodeValList.get(i));
        }
        return results;
    }

    /**
     * Vertical Order Traversal of a Binary Tree
     * Given the root of a binary tree, calculate the vertical order traversal of the binary tree.
     * <p>
     * For each node at position (row, col), its left and right children will be at positions
     * (row + 1, col - 1) and (row + 1, col + 1) respectively. The root of the tree is at (0, 0).
     * <p>
     * The vertical order traversal of a binary tree is a list of top-to-bottom orderings for each column
     * index starting from the leftmost column and ending on the rightmost column. There may be multiple
     * nodes in the same row and same column. In such a case, sort these nodes by their values.
     * <p>
     * Return the vertical order traversal of the binary tree.
     * <p>
     * Input: root = [3,9,20,null,null,15,7]
     * Output: [[9],[3,15],[20],[7]]
     * Explanation:
     * Column -1: Only node 9 is in this column.
     * Column 0: Nodes 3 and 15 are in this column in that order from top to bottom.
     * Column 1: Only node 20 is in this column.
     * Column 2: Only node 7 is in this column.
     * <p>
     * Input: root = [1,2,3,4,6,5,7]
     * Output: [[4],[2],[1,5,6],[3],[7]]
     * Explanation:
     * This case is the exact same as example 2, but with nodes 5 and 6 swapped.
     * Note that the solution remains the same since 5 and 6 are in the same location and should be
     * ordered by their values.
     * <p>
     * https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/description/
     */
    @Test
    void testVerticalTraversal() {
        TreeNode root = createBinaryTree(3, 9, 20, null, null, 15, 7);
        Assertions.assertThat(verticalTraversal(root)).containsExactly(List.of(9), List.of(3, 15), List.of(20), List.of(7));
        root = createBinaryTree(1);
        Assertions.assertThat(verticalTraversal(root)).containsExactly(List.of(1));
        root = createBinaryTree(1, 2, 3, 4, 6, 5, 7);
        Assertions.assertThat(verticalTraversal(root)).containsExactly(List.of(4), List.of(2), List.of(1, 5, 6), List.of(3), List.of(7));
    }

    /**
     * This problem is almost the same as "Binary Tree Vertical Order Traversal" besides an additional
     * requirement, " There may be multiple nodes in the same row and same column. In such a case,
     * sort these nodes by their values."
     * <p>
     * Use the same approach as previous problem, but we need to keep separate colIdxToNodeValListLvl map.
     * The nodes at the same level removed from the queue are put into this map first, after iteration
     * of one level is over, sort every list in the map and add to the colIdxToNodeValList map.
     * <p>
     * Time complexity: O(n⋅log n ⋅ log n/k)
     * The sorting part is dominated the time complexity. In average case, given k is
     * the width of the tree, which means there are k columns.
     * Tree height x sorting each list in the map:
     * O(log n) x O(k⋅n/k⋅log n/k) = O(log n) x O(n⋅log n/k)
     * <p>
     * Space complexity: O(n)
     */
    List<List<Integer>> verticalTraversal(TreeNode root) {
        if (root == null)
            return new ArrayList<>();
        List<List<Integer>> results = new ArrayList<>();
        Map<Integer, List<Integer>> colIdxToNodeValList = new HashMap<>();
        int maxColIdx = 0, minColIdx = 0;
        Queue<Pair<TreeNode, Integer>> queue = new ArrayDeque<>();
        queue.offer(new Pair<>(root, 0));
        while (!queue.isEmpty()) {
            int qSize = queue.size();
            // Use separate map for node grouped by column index at the same level, so we can sort the node list at the
            // same column later
            Map<Integer, List<Integer>> colIdxToNodeValListLvl = new HashMap<>();
            for (int i = 0; i < qSize; i++) {
                Pair<TreeNode, Integer> node = queue.poll();
                colIdxToNodeValListLvl.computeIfAbsent(node.getValue(), k -> new ArrayList<>()).add(node.getKey().val);
                if (node.getKey().left != null) {
                    int lColIdx = node.getValue() - 1;
                    minColIdx = Math.min(minColIdx, lColIdx);
                    queue.offer(new Pair<>(node.getKey().left, lColIdx));
                }
                if (node.getKey().right != null) {
                    int rColIdx = node.getValue() + 1;
                    maxColIdx = Math.max(maxColIdx, rColIdx);
                    queue.offer(new Pair<>(node.getKey().right, rColIdx));
                }
            }
            colIdxToNodeValListLvl.forEach((k, v) -> {
                // sort the node value list before adding to the colIdxToNodeValList map
                v.sort(null);
                colIdxToNodeValList.computeIfAbsent(k, x -> new ArrayList<>()).addAll(v);
            });

        }
        for (int i = minColIdx; i <= maxColIdx; i++) {
            if (colIdxToNodeValList.containsKey(minColIdx))
                results.add(colIdxToNodeValList.get(i));
        }
        return results;
    }
}


