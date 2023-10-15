package playground.tree;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Generic tree structure, each node has zero or more child nodes, no cycle
 * https://www.baeldung.com/java-breadth-first-search
 * https://www.baeldung.com/java-depth-first-search
 *
 * @param <T>
 */
@Slf4j
@ToString
public class TreeNode<T> {

    private final T value;


    private final List<TreeNode<T>> children;

    private TreeNode(T value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public static <T> TreeNode<T> of(T value) {
        return new TreeNode<>(value);
    }

    public T getValue() {
        return value;
    }

    public List<TreeNode<T>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public TreeNode<T> addChild(T value) {
        TreeNode<T> newChild = new TreeNode<>(value);
        children.add(newChild);
        return newChild;
    }

    //Time Complexity: O(V+E). Space Complexity: ?
    public Optional<TreeNode<T>> bfsSearch(T value) {
        Queue<TreeNode<T>> queue = new ArrayDeque<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            TreeNode<T> visitedNode = queue.remove();
            log.info("Visited node with value: {}", visitedNode.getValue());
            if (visitedNode.getValue().equals(value)) {
                return Optional.of(visitedNode);
            }
            queue.addAll(visitedNode.getChildren());
        }
        return Optional.empty();
    }

    // DFS w/ Preorder Traversal
    public Optional<TreeNode<T>> dfsRecursiveSearch(T value) {
        log.info("Visited node: {}", this.value);
        if (this.getValue().equals(value)) {
            return Optional.of(this);
        }
        for (TreeNode<T> child : this.children) {
            Optional<TreeNode<T>> result = child.dfsRecursiveSearch(value);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    public void dfsPreOrder() {
        log.info("Visited node: {}", this.value);
        for (TreeNode<T> child : this.children) {
            child.dfsPreOrder();
        }
    }

    // DFS w/ Preorder Traversal
    public Optional<TreeNode<T>> dfsSearch(T value) {
        Deque<TreeNode<T>> stack = new ArrayDeque<>();
        stack.push(this);
        while (!stack.isEmpty()) {
            TreeNode<T> visitedNode = stack.pop();
            log.info("Visited node: {}", visitedNode.value);
            if (visitedNode.getValue().equals(value)) {
                return Optional.of(visitedNode);
            }
            // We want Preorder Traversal, so pushing the child from the end (left child to right child)
            for (int i = visitedNode.getChildren().size() - 1; i >= 0; i--) {
                stack.push(visitedNode.getChildren().get(i));
            }
        }
        return Optional.empty();
    }
}
