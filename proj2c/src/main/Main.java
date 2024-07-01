package main;

import browser.NgordnetServer;
import ngrams.NGramMap;
import org.slf4j.LoggerFactory;
import wordnet.WordNet;

public class Main {
    static {
        LoggerFactory.getLogger(Main.class).info("\033[1;38mChanging text color to white");
    }
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();

        String wordFile = "./data/ngrams/top_14377_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";
        NGramMap ngm = new NGramMap(wordFile, countFile);

        String synsetFile = "./data/wordnet/synsets.txt";
        String hyponymFile = "./data/wordnet/hyponyms.txt";
        WordNet wordNet = new WordNet(synsetFile, hyponymFile);

        hns.startUp();
        hns.register("history", new HistoryHandler(ngm));
        hns.register("historytext", new HistoryTextHandler(ngm));
        hns.register("hypohist", new HypohistHandler(wordNet, ngm));
        hns.register("hypohisttext", new HypohistTextHandler(wordNet, ngm));
        hns.register("hyponyms", new HyponymsHandler(wordNet, ngm));

        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet.html");
    }
}
