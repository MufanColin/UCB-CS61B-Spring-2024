import java.util.ArrayList;
import java.util.Arrays;

public class UnionFind {
    private final int[] nodes; // Stores the parent or the negative size of the tree
    private final int size; // the total number of nodes

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        nodes = new int[N];
        size = N;
        Arrays.fill(nodes, -1);
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        return -nodes[find(v)];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return nodes[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v < 0 || v >= size) {
            throw new IllegalArgumentException("Invalid item!");
        }
        // We need to implement path compression!!!
        ArrayList<Integer> arrayList = new ArrayList<>();
        while (parent(v) >= 0) {
            arrayList.add(v);
            v = parent(v);
        }
        for (int x: arrayList) {
            nodes[x] = v;
        }
        return v;
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing an item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (v1 != v2) {
            int size1 = sizeOf(v1), size2 = sizeOf(v2);
            int root1 = find(v1), root2 = find(v2); // root1 is v1's root, root2 is v2's root
            if (size1 <= size2) {
                // connect v1's root to v2's root
                nodes[root1] = root2;
                nodes[root2] -= size1;
            } else {
                // connect v2's root to v1's root
                nodes[root2] = root1;
                nodes[root1] -= size2;
            }
        }
    }
}
