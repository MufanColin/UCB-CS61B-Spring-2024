package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.knowm.xchart.XYChart;
import plotting.Plotter;
import wordnet.WordNet;

import java.util.ArrayList;
import java.util.List;

public class HypohistHandler extends NgordnetQueryHandler {
    private final WordNet wordNet;
    private final NGramMap nGramMap;

    public HypohistHandler(WordNet wordNet, NGramMap nGramMap) {
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
        ArrayList<TimeSeries> lts = new ArrayList<>();
        for (String result : results) {
            lts.add(this.nGramMap.weightHistory(result, startYear, endYear));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(results, lts);
        return Plotter.encodeChartAsString(chart);
    }
}
