package playground.tree;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class Search {
    /*
    https://www.baeldung.com/java-breadth-first-search
     */
    @Test
    void testTreeBFS() {
        TreeNode<Integer> root = TreeNode.of(10);
        TreeNode<Integer> rootFirstChild = root.addChild(2);
        TreeNode<Integer> FirstChildChild = rootFirstChild.addChild(3);
        TreeNode<Integer> rootSecondChild = root.addChild(4);
        Assertions.assertThat(root.bfsSearch(3)).isNotEmpty();
    }

    @Test
    void testGraphBFS() {
        GraphNode<Integer> start = new GraphNode<>(10);
        GraphNode<Integer> firstNeighbor = new GraphNode<>(2);
        start.connect(firstNeighbor);
        GraphNode<Integer> firstNeighborNeighbor = new GraphNode<>(3);
        firstNeighbor.connect(firstNeighborNeighbor);
        firstNeighborNeighbor.connect(start);

        GraphNode<Integer> secondNeighbor = new GraphNode<>(4);
        start.connect(secondNeighbor);

        Assertions.assertThat(firstNeighborNeighbor.bfsSearch(4)).isNotEmpty();
    }

    @Test
    void testTreeDFS() {
        TreeNode<Integer> root = TreeNode.of(10);
        TreeNode<Integer> secondLvlNode1 = root.addChild(2);
        secondLvlNode1.addChild(3);
        secondLvlNode1.addChild(4);
        TreeNode<Integer> secondLvlNode2 = root.addChild(5);
        secondLvlNode2.addChild(6);
        Optional<TreeNode<Integer>> result = root.dfsRecursiveSearch(4);
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getValue()).isEqualTo(4);
        result = root.dfsSearch(6);
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getValue()).isEqualTo(6);
    }

    @Test
    void testGraphDFS() {
        GraphNode<Integer> start = new GraphNode<>(10);
        GraphNode<Integer> node2 = new GraphNode<>(2);
        start.connect(node2);
        GraphNode<Integer> node3 = new GraphNode<>(3);
        node2.connect(node3);
        node3.connect(start);

        GraphNode<Integer> node4 = new GraphNode<>(4);
        start.connect(node4);
        Optional<GraphNode<Integer>> result = start.dfsSearch(2);
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getValue()).isEqualTo(2);

        result = start.dfsRecursiveSearch(2);
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().getValue()).isEqualTo(2);
    }

    // TODO: Check Topological Sort
    //  https://www.baeldung.com/java-depth-first-search
}
