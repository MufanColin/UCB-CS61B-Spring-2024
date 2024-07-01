package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import wordnet.WordNet;

import java.util.List;

public class HypohistTextHandler extends NgordnetQueryHandler {
    private final WordNet wordNet;
    private final NGramMap nGramMap;

    public HypohistTextHandler(WordNet wordNet, NGramMap nGramMap) {
        this.wordNet = wordNet;
        this.nGramMap = nGramMap;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        if (k == 0) {
            k = 5;
        }
        List<String> results = wordNet.getTopKHOrAForListOfWords(words, startYear, endYear, k, nGramMap, wordNet.getGraph().getAdjList());
        StringBuilder response = new StringBuilder();
        for (String result : results) {
            response.append(result).append(": ")
                    .append(this.nGramMap.weightHistory(result, startYear, endYear))
                    .append("\n");
        }
        return response.toString();
    }
}
