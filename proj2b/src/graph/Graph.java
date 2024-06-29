package graph;

import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    // maps the index of a node to all the indexes of its adjacent nodes
    private final HashMap<Integer, HashSet<Integer>> adjList;

    // maps the index of a node to all the words in that node
    // a node may contain multiple words
    private final HashMap<Integer, HashSet<String>> nodesHM;

    // maps a word to all the indexes of the nodes containing that word
    // a word may appear in multiple nodes
    private final HashMap<String, HashSet<Integer>> wordsToIndexHM;

    // maps the index of a node to all the indexes of its parents
    // a node may have multiple parents (1, 2, 3, ...)
    private final HashMap<Integer, HashSet<Integer>> predHM;

    public Graph() {
        adjList = new HashMap<>();
        nodesHM = new HashMap<>();
        wordsToIndexHM = new HashMap<>();
        predHM = new HashMap<>();
    }

    /** Creating a new node by specifying its index and all words stored inside of it.*/
    public void createNode(int index, HashSet<String> words) {
        nodesHM.put(index, words);
        for (String word: words) {
            // a little bit fancy way to combine containsKey and add together
            // inspired by ChatGPT
            wordsToIndexHM.computeIfAbsent(word, k -> new HashSet<>()).add(index);
        }
    }

    /**
     * Given the index of the root node and the child node, add an edge
     * between them by updating adjList and predHM.
     */
    public void addEdge(int root, int child) {
        // a little bit fancy way to combine containsKey and add together
        // inspired by ChatGPT
        adjList.computeIfAbsent(root, k -> new HashSet<>()).add(child);
        predHM.computeIfAbsent(child, k -> new HashSet<>()).add(root);
    }

    /** Given a word, return all the indexes of nodes containing that word.*/
    public HashSet<Integer> getNodeIndex(String word) {
        return wordsToIndexHM.get(word);
    }

    /** Given the index of a node, return all the words stored in that node. */
    public HashSet<String> getNodeValue(int nodeIndex) {
        return nodesHM.get(nodeIndex);
    }

    /** Given the index of a node, return all the indexes of its adjacent nodes. */
    public HashSet<Integer> getAdjNodes(int nodeIndex) {
        return adjList.get(nodeIndex);
    }
}
