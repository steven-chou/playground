package playground.unionfind;

/**
 * UnionFind/Disjoint Set (DSU) data structure implementation.
 * Copied from LeetCode Disjoint Explorer card
 * <p>
 * The main idea of a “disjoint set” is to have all connected vertices have the same parent node or root node, whether directly
 * or indirectly connected. To check if two vertices are connected, we only need to check if they have the same root node.
 * <p>
 * The two most important functions for the “disjoint set” data structure are the find function and the union function.
 * The find function locates the root node of a given vertex. The union function connects two previously unconnected vertices
 * by giving them the same root node. There is another important function named connected, which checks the “connectivity” of
 * two vertices. The find and union functions are essential for any question that uses the “disjoint set” data structure.
 * <p>
 * Note for Time Complexity: N is the number of vertices in the graph. α refers to the Inverse Ackermann function.
 * In practice, we assume it's a constant. In other words, O(α(N)) is regarded as O(1) on average.
 */
public class UnionFindLeetCode {
    private final int[] root;
    // Use a rank array to record the height of each vertex, i.e., the "rank" of each vertex.
    private final int[] rank;

    /**
     * Time Complexity: O(N)
     */
    public UnionFindLeetCode(int size) {
        root = new int[size];
        rank = new int[size];
        for (int i = 0; i < size; i++) {
            root[i] = i;
            rank[i] = 1; // The initial "rank" of each vertex is 1, because each of them is
            // a standalone vertex with no connection to other vertices.
        }
    }

    /**
     * Find the root node of a given vertex.
     * The basic implementation of finding the root node of a given vertex.
     * Time Complexity: O(N)
     * in the worst-case scenario, we need to traverse every vertex to find the root for the input vertex. The maximum
     * number of operations to get the root vertex would be no more than the tree's height, so it will take O(N) time.
     */
    public int find(int x) {
        while (x != root[x]) {
            // Traverse every vertex slot until the one referencing itself, i.e. index = root[index], which is root vertex
            x = root[x];
        }
        return x;
    }

    /**
     * Find the root node of a given vertex with path compression
     * After finding the root node, we update the parent node of all traversed elements to their root node.
     * When we search for the root node of the same element again, we only need to traverse two elements to find its root
     * node, which is highly efficient.
     * Time Complexity: O(α(N))
     */
    public int findWithPathCompression(int x) {
        if (x == root[x]) {
            return x;
        }
        int rootNode = x;
        while (rootNode != root[rootNode]) {
            // Traverse every vertex slot until the one referencing itself, i.e. index = root[index], which is root vertex
            rootNode = root[rootNode];
        }
        // Compress the path leading back to the root. Doing this operation is called "path compression"
        // and is what gives us amortized time complexity.
        while (x != rootNode) {
            // Traverse from node x to the root node we just found and update the root of the nodes on this path to the rootNode
            int next = root[x];
            root[x] = rootNode;
            x = next;
        }
        return rootNode;
    }

    public int findWithPathCompressionRecursive(int x) {
        if (x == root[x]) {
            return x;
        }
        root[x] = findWithPathCompressionRecursive(root[x]);
        return root[x];
    }

    /**
     * Connect two vertices, x, and y, by equating their root node.
     * This is the simplest implementation. We always chose the root node of x and set it as the new root node for the
     * other vertex, so it is possible for all the vertices to form a line after connecting them using union, which results
     * in the worst-case scenario for the find function.
     * Time Complexity: O(N)
     * It consists of two find operations which (only in the worst-case) will take O(N) time, and two constant time
     * operations, including the equality check and updating the array value at a given index. Therefore, the union
     * operation also costs O(N) in the worst-case.
     */
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            root[rootY] = rootX;
        }
    }

    /**
     * The union function with union by rank
     * The “rank” refers to the HEIGHT(the number of edges in the longest simple path from a descendant leaf to x) of each
     * vertex. When we union two vertices, instead of always picking the root of x (or y, it doesn't matter as long as
     * we're consistent) as the new root node, we choose the root node of the vertex with a larger “rank”. We will merge
     * the shorter tree under the taller tree and assign the root node of the taller
     * tree as the root node for both vertices. In this way, we effectively avoid the possibility of connecting all vertices
     * into a straight line.
     * <p>
     * ==== Note ===
     * The "rank" sometimes can be replaced w/ the size of the set in the union function. (Refer to UnionFindWF class)
     * In that case, the size of the larger set should be updated and the size of the smaller set should be set to zero
     * after the merge/union
     * <p>
     * Time Complexity: O(α(N))
     */
    public void unionByRank(int x, int y) {
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
/*          The rank only increases when the rank of both sets are equal. This is because there is no way to add one to the
            other without increasing the rank by exactly one. If one is less than the other, adding the smaller 'tree' to
            the larger one will not increase it's rank (height).
            Example of rank of equal height

            # before, two disjoint sets of equal rank
             {2}      {5}
             / \      / \
            0   1    3   4
            # after union, rank increase by one
             { 2 }
             / | \
            0  1  5
                 / \
                3   4
            Now an example of union-ing a smaller set with a larger one.

            # before, two disjoint sets of un-equal rank
             { 2 }      {8}
             / | \      / \
            0  1  5    6   7
                 / \
                3   4

            # after union, rank remains the same
             {    2    }
             / |  |    \
            0  1  5     8
                 / \   / \
                3   4 6   7
           */
            }
        }
    }

    /**
     * Time Complexity:
     * O(α(N)), when calling findWithPathCompression()
     * O(N) when calling find()
     */
    public boolean connected(int x, int y) {
        // Depends on which version of find function you want
        return findWithPathCompression(x) == findWithPathCompression(y);
        //return find(x) == find(y);
    }
}
