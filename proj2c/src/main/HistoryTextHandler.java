package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private final NGramMap nGramMap;

    public HistoryTextHandler(NGramMap map) {
        this.nGramMap = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        StringBuilder response = new StringBuilder();
        for (String word: words) {
            response.append(word).append(": ").
                    append(this.nGramMap.weightHistory(word, startYear, endYear)).
                    append("\n");
        }
        return response.toString();
    }
}
