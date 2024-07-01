package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import ngrams.NGramMap;
import wordnet.WordNet;

import java.util.List;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final WordNet wordNet;
    private final NGramMap nGramMap;
    public HyponymsHandler(WordNet wordNet, NGramMap nGramMap) {
        this.wordNet = wordNet;
        this.nGramMap = nGramMap;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        NgordnetQueryType type = q.ngordnetQueryType();
        List<String> response;
        // different mode below
        if (type == NgordnetQueryType.HYPONYMS) {
            response = wordNet.getTopKHOrAForListOfWords(words, startYear, endYear, k, nGramMap, wordNet.getGraph().getAdjList());
        } else {
            response = wordNet.getTopKHOrAForListOfWords(words, startYear, endYear, k, nGramMap, wordNet.getGraph().getPredHM());
        }
        return "[" + String.join(", ", response) + "]";
    }
}
