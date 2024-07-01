package wordnet;

import edu.princeton.cs.algs4.In;
import graph.Graph;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class WordNet {
    private final Graph graph;
    private final HashMap<String, Double> totalCountHM;
    public WordNet(String synsetsFile, String hyponymsFile) {
        graph = new Graph();
        totalCountHM = new HashMap<>();
        // Read from the given synsets file
        In in = new In(synsetsFile);
        while (in.hasNextLine()) {
            // index, one (or more) word(s), definition
            String line = in.readLine();
            String[] splitLine = line.split(",");
            int index = Integer.parseInt(splitLine[0]);
            String[] words = splitLine[1].split(" ");
            HashSet<String> values = new HashSet<>(List.of(words));
            graph.createNode(index, values);
        }
        // Read from the given hyponyms file
        in = new In(hyponymsFile);
        while (in.hasNextLine()) {
            // root index, children indexes
            String line = in.readLine();
            String[] splitLine = line.split(",");
            int rootIndex = Integer.parseInt(splitLine[0]);
            for (int i = 1; i < splitLine.length; i++) {
                int childIndex = Integer.parseInt(splitLine[i]);
                graph.addEdge(rootIndex, childIndex);
            }
        }
    }

    /** Given a word, return all hyponyms of it (the word may appear in multiple nodes).*/
    public HashSet<String> getHyponymsForSingleWord(String word) {
        // We need to somehow do a graph traversal
        HashSet<String> hyponymsHS = new HashSet<>();
        HashSet<Integer> nodeIndexes = graph.getNodeIndex(word);
        // Check that the input word is valid
        if (nodeIndexes != null) {
            for (Integer nodeIndex : nodeIndexes) {
                // We need to somehow traverse from a given node index
                hyponymsHS.addAll(getHyponymsForSingleWord(nodeIndex));
            }
        }
        return hyponymsHS;
    }

    /** Helper function of the method above. */
    public HashSet<String> getHyponymsForSingleWord(int nodeIndex) {
        HashSet<String> ans;
        HashSet<Integer> adjNodes = graph.getAdjNodes(nodeIndex);
        // "leaf" node
        if (adjNodes == null) {
            return new HashSet<>(graph.getNodeValue(nodeIndex));
        } else {
            // "non-leaf" node
            ans = new HashSet<>(graph.getNodeValue(nodeIndex));
            for (Integer adjNode: adjNodes) {
                ans.addAll(getHyponymsForSingleWord(adjNode));
            }
        }
        return ans;
    }

    /** Return words which are hyponyms of all words in the list. */
    public List<String> getHyponymsForListOfWords(List<String> words) {
        HashSet<String> commonHyponyms = getHyponymsForSingleWord(words.getFirst());
        for (int i = 1; i < words.size(); i++) {
            commonHyponyms.retainAll(getHyponymsForSingleWord(words.get(i)));
        }
        List<String> list = new ArrayList<>(commonHyponyms);
        Collections.sort(list);
        return list;
    }

    /**
     * Return top k words which are hyponyms of all words in the list (between startYear and endYear).
     * If k < 0, we should return an empty list,
     * If k == 0, we should call the method above,
     * If k > 0, we should select the top k hyponyms of all words in the list.
     * The k words which occurred the most times in the time range requested.
     * For words with the same counts, we can break ties alphabetically, or randomly, or not at all.
     */
    public List<String> getTopKHyponymsForListOfWords(List<String> words, int startYear, int endYear, int k, NGramMap nGramMap) {
        List<String> allHyponyms = getHyponymsForListOfWords(words);
        if (k < 0) {
            return new ArrayList<>();
        }
        if (k > 0) {
            for (String hyponym: allHyponyms) {
                totalCountHM.put(hyponym, getTotalCountOfAWordInSomeTimeRange(nGramMap, hyponym, startYear, endYear));
            }
            PriorityQueue<String> topKHyponymsPQ =
                    new PriorityQueue<>((s1, s2) -> Double.compare(totalCountHM.get(s1), totalCountHM.get(s2)));
            for (String hyponym: allHyponyms) {
                if (totalCountHM.get(hyponym) > 0) {
                    topKHyponymsPQ.add(hyponym);
                }
                if (topKHyponymsPQ.size() > k) {
                    topKHyponymsPQ.poll();
                }
            }
            List<String> list = new ArrayList<>(topKHyponymsPQ);
            Collections.sort(list);
            return list;
        }
        return allHyponyms;
    }

    private double getTotalCountOfAWordInSomeTimeRange(NGramMap nGramMap, String word, int startYear, int endYear) {
        TimeSeries wordCountTS = nGramMap.countHistory(word, startYear, endYear);
        return wordCountTS.data().stream().mapToDouble(Double::doubleValue).sum();
    }
}
