package playground.tree;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * TODO: Note:
 *  1. BFS: Use Queue, good for searching for shortest path between two nodes
 *  2. DFS: Use Stack
 *
 * @param <T>
 */
@Slf4j
public class GraphNode<T> {
    private final T value;
    private final Set<GraphNode<T>> neighbors;

    public GraphNode(T value) {
        this.value = value;
        this.neighbors = new HashSet<>();
    }

    public T getValue() {
        return value;
    }

    public Set<GraphNode<T>> getNeighbors() {
        return Collections.unmodifiableSet(neighbors);
    }

    public void connect(GraphNode<T> node) {
        if (this == node) throw new IllegalArgumentException("Can't connect node to itself");
        this.neighbors.add(node);
        node.neighbors.add(this);
    }

    public Optional<GraphNode<T>> bfsSearch(T value) {
        Queue<GraphNode<T>> queue = new ArrayDeque<>();
        queue.add(this);

        GraphNode<T> currentNode;
        Set<GraphNode<T>> alreadyVisited = new HashSet<>();

        while (!queue.isEmpty()) {
            currentNode = queue.remove();
            log.info("Visited node with value: {}", currentNode.getValue());

            if (currentNode.getValue().equals(value)) {
                return Optional.of(currentNode);
            } else {
                alreadyVisited.add(currentNode);
                queue.addAll(currentNode.getNeighbors());
                // Cuz the graph can have cycle, after adding the node's neighbors to the queue,
                // we remove all visited nodes from the queue, which is an alternative way of checking the current node's presence in that set
                queue.removeAll(alreadyVisited);
            }
        }

        return Optional.empty();
    }

    public Optional<GraphNode<T>> dfsSearch(T value) {
        Stack<GraphNode<T>> stack = new Stack<>();
        stack.push(this);
        Set<GraphNode<T>> visitedNodes = new HashSet<>();
        while (!stack.isEmpty()) {
            GraphNode<T> visitedNode = stack.pop();
            visitedNodes.add(visitedNode);
            log.info("Visited node with value: {}", visitedNode.getValue());
            if (visitedNode.getValue().equals(value)) {
                return Optional.of(visitedNode);
            }
            for (GraphNode<T> neighbor : visitedNode.neighbors) {
                if (!visitedNodes.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<GraphNode<T>> dfsRecursive(T value, GraphNode<T> currentNode, Set<GraphNode<T>> visitedNodes) {
        log.info("Visited node: {}", currentNode.getValue());
        visitedNodes.add(currentNode);
        if (currentNode.getValue().equals(value)) {
            return Optional.of(currentNode);
        }
        for (GraphNode<T> neighbor : currentNode.getNeighbors()) {
            if (!visitedNodes.contains(neighbor)) {
                Optional<GraphNode<T>> result = dfsRecursive(value, neighbor, visitedNodes);
                if (result.isPresent()) {
                    return result;
                }
            }
        }
        return Optional.empty();
    }

    public Optional<GraphNode<T>> dfsRecursiveSearch(T value) {
        Set<GraphNode<T>> visitedNodes = new HashSet<>();
        return dfsRecursive(value, this, visitedNodes);
    }
}
