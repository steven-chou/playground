package playground.tree;

import org.junit.jupiter.api.Test;

public class BinarySearchTreeTest {

    /*
     * input:
     *     6
     *   /  \
     *  4    8
     * / \  / \
     *3   5 7  9
     */
    private BinarySearchTree createBinaryTree() {
        BinarySearchTree bt = new BinarySearchTree();

        bt.add(6);
        bt.add(4);
        bt.add(8);
        bt.add(3);
        bt.add(5);
        bt.add(7);
        bt.add(9);

        return bt;
    }

    @Test
    public void givenABinaryTree_WhenTraversingInOrder_ThenPrintValues() {

        BinarySearchTree bt = createBinaryTree();

        bt.traverseInOrder(bt.root);
        System.out.println();
        bt.traverseInOrderWithoutRecursion();
        System.out.println();
        bt.traverseInOrderIterative();
    }

    @Test
    public void givenABinaryTree_WhenTraversingPreOrder_ThenPrintValues() {

        BinarySearchTree bt = createBinaryTree();

        bt.traversePreOrder(bt.root);
        System.out.println();
        bt.traversePreOrderIterative();
    }

    @Test
    public void givenABinaryTree_WhenTraversingPostOrder_ThenPrintValues() {

        BinarySearchTree bt = createBinaryTree();

        bt.traversePostOrder(bt.root);
        System.out.println();
        bt.traversePostOrderIterative();
    }

    @Test
    public void givenABinaryTree_WhenTraversingLevelOrder_ThenPrintValues() {

        BinarySearchTree bt = createBinaryTree();

        bt.traverseLevelOrder();
    }
}
