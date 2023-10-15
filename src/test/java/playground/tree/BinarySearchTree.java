package playground.tree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * Check source code from com.baeldung.algorithms.dfs
 * https://www.baeldung.com/java-depth-first-search
 */
public class BinarySearchTree {

    class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            right = null;
            left = null;
        }
    }

    Node root;

    public void add(int value) {
        root = addRecursive(root, value);
    }

    private Node addRecursive(Node current, int value) {

        if (current == null) {
            return new Node(value);
        }

        if (value < current.value) {
            current.left = addRecursive(current.left, value);
        } else if (value > current.value) {
            current.right = addRecursive(current.right, value);
        }

        return current;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int getSize() {
        return getSizeRecursive(root);
    }

    private int getSizeRecursive(Node current) {
        return current == null ? 0 : getSizeRecursive(current.left) + 1 + getSizeRecursive(current.right);
    }

    public boolean containsNode(int value) {
        return containsNodeRecursive(root, value);
    }

    private boolean containsNodeRecursive(Node current, int value) {
        if (current == null) {
            return false;
        }

        if (value == current.value) {
            return true;
        }

        return value < current.value
                ? containsNodeRecursive(current.left, value)
                : containsNodeRecursive(current.right, value);
    }

    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    private Node deleteRecursive(Node current, int value) {
        if (current == null) {
            return null;
        }

        if (value == current.value) {
            // Case 1: no children
            if (current.left == null && current.right == null) {
                return null;
            }

            // Case 2: only 1 child
            if (current.right == null) {
                return current.left;
            }

            if (current.left == null) {
                return current.right;
            }

            // Case 3: 2 children
            int smallestValue = findSmallestValue(current.right);
            current.value = smallestValue;
            current.right = deleteRecursive(current.right, smallestValue);
            return current;
        }
        if (value < current.value) {
            current.left = deleteRecursive(current.left, value);
            return current;
        }

        current.right = deleteRecursive(current.right, value);
        return current;
    }

    private int findSmallestValue(Node root) {
        return root.left == null ? root.value : findSmallestValue(root.left);
    }

    //Also check
    //https://dev.to/javinpaul/how-to-implement-inorder-traversal-in-a-binary-search-tree-1787
    // Time Complexity: O(N). Space Complexity: O(N)
    public void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.left);
            visit(node.value);
            traverseInOrder(node.right);
        }
    }

    public void traversePreOrder(Node node) {
        if (node != null) {
            visit(node.value);
            traversePreOrder(node.left);
            traversePreOrder(node.right);
        }
    }

    public void traversePostOrder(Node node) {
        if (node != null) {
            traversePostOrder(node.left);
            traversePostOrder(node.right);
            visit(node.value);
        }
    }

    /**
     * Generic implementation of BFS
     * - First push the root to the queue
     * - While queue is not empty
     * -- Pop out the element from the queue(or do some ops)
     * -- Push the right and left child nodes to the queue
     */
    public void traverseLevelOrder() {
        if (root == null) {
            return;
        }

        Queue<Node> nodes = new ArrayDeque<>();
        nodes.add(root);

        while (!nodes.isEmpty()) {

            Node node = nodes.remove();

            // Can be any OPS when visiting the node
            System.out.print(" " + node.value);

            if (node.left != null) {
                nodes.add(node.left);
            }

            if (node.left != null) {
                nodes.add(node.right);
            }
        }
    }

    /**
     * READ the traverseInOrderIterative method instead
     * Push root node to stack
     * While stack is not empty
     * Keep pushing left child onto stack, till we reach current node's left-most child
     * Visit current node
     * Push right child onto stack
     */
    @Deprecated
    public void traverseInOrderWithoutRecursion() {
        Deque<Node> stack = new ArrayDeque<>();
        Node current = root;
        stack.push(root);
        while (!stack.isEmpty()) {
            while (current.left != null) {
                current = current.left;
                stack.push(current);
            }
            current = stack.pop();
            visit(current.value);
            if (current.right != null) {
                // We reach here cuz we just visit the parent node, so we want to check its left child tree
                // at next iteration
                current = current.right;
                stack.push(current);
            }
        }
    }


    /**
     * Initialize current node with root
     * While current is not null or stack is not empty
     * - Keep pushing left child onto stack, till we reach current node's left-most child
     * - Pop and visit the left-most node from stack
     * - Set current to the right child of the popped node
     * -- The left child nodes push earlier will be popped first, and they are basically the "root" of subtrees, so will be visited before
     * we push their right child to the stack
     * -- This right child node will be push(for sure) & pop from the stack(if it doesn't have any left child) at next iteration at outer while loop
     * Time Complexity: O(N). Space Complexity: O(N)
     */
    public void traverseInOrderIterative() {
        Deque<Node> stack = new ArrayDeque<>();
        Node current = root;

        while (!stack.isEmpty() || current != null) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            current = stack.pop();
            visit(current.value);
            current = current.right;
        }
    }

    /**
     * Push root in our stack
     * While stack is not empty
     * - Pop current node
     * - Visit current node
     * - Push right child, then left child to stack(stack is LIFO, so the right is push first)
     * Time Complexity: O(N). Space Complexity: O(N)
     */
    public void traversePreOrderIterative() {
        Deque<Node> stack = new ArrayDeque<>();
        if (root != null) {
            stack.push(root);
            while (!stack.isEmpty()) {
                Node current = stack.pop();
                visit(current.value);

                if (current.right != null)
                    stack.push(current.right);

                if (current.left != null)
                    stack.push(current.left);
            }
        }
    }

    /**
     * Push root node in stack
     * While stack is not empty
     * - Peek the next item in the stack
     * - Check if we already traversed left and right subtree or the node is the leaf node
     * -- Pop the node from the stack and visit it
     * -- Otherwise, push right child and left child onto stack
     */
    public void traversePostOrderIterative() {
        Deque<Node> stack = new ArrayDeque<>();
        Node lastVisitedNode = null;
        stack.push(root);
        while (!stack.isEmpty()) {
            Node current = stack.peek();
            boolean hasChild = (current.left != null || current.right != null);
            // True means we have visited the child nodes
            //  1. Visited right child node at the last iteration(left must be visited already cuz the way we push to the stack)
            //  OR
            //  2. Visited left child node at the last iteration and there is no right child
            // So we should pop the current node and visit it
            boolean visitedChildNodes =
                    lastVisitedNode != null
                            && (lastVisitedNode == current.right || (lastVisitedNode == current.left && current.right == null));

            if (!hasChild || visitedChildNodes) {
                current = stack.pop();
                visit(current.value);
                lastVisitedNode = current;
            } else {
                if (current.right != null) {
                    stack.push(current.right);
                }
                if (current.left != null) {
                    stack.push(current.left);
                }
            }
        }
    }


    private void visit(int value) {
        System.out.print(" " + value);
    }


}
