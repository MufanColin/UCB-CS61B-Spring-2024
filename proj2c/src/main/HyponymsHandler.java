package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
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
        List<String> response = wordNet.getTopKHyponymsForListOfWords(words, startYear, endYear, k, nGramMap);
        return "[" + String.join(", ", response) + "]";
    }
}
