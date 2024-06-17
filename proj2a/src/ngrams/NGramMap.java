package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.TreeMap;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private final TreeMap<String, TimeSeries> wordsInfoHM;
    private final TreeMap<Integer, Double> totalCountInfoHM;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordsInfoHM = new TreeMap<>();
        totalCountInfoHM = new TreeMap<>();
        In in = new In(wordsFilename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] splitLine = line.split("\t"); // tsv file
            TimeSeries newTimeSeries = new TimeSeries();
            newTimeSeries.put(Integer.parseInt(splitLine[1]), Double.parseDouble(splitLine[2]));
            // Using the merge method to add or update the entry
            wordsInfoHM.merge(splitLine[0], newTimeSeries, TimeSeries::plus);
        }
        in = new In(countsFilename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] splitLine = line.split(",");
            totalCountInfoHM.put(Integer.parseInt(splitLine[0]), Double.parseDouble(splitLine[1]));
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        return new TimeSeries(wordsInfoHM.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        return countHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        TimeSeries wordCountTS = new TimeSeries();
        for (Integer year: totalCountInfoHM.keySet()) {
            wordCountTS.put(year, totalCountInfoHM.get(year));
        }
        return wordCountTS;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries weightHistoryTS = new TimeSeries();
        TimeSeries wordCountTS = this.totalCountHistory(); // denominator
        if (wordsInfoHM.containsKey(word)) {
            TimeSeries countHistoryTS = this.countHistory(word, startYear, endYear);
            for (Integer year: countHistoryTS.years()) {
                if (wordCountTS.containsKey(year)) {
                    weightHistoryTS.put(year, wordsInfoHM.get(word).get(year) / wordCountTS.get(year));
                }
            }
        }
        return weightHistoryTS;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return weightHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries summedWeightHistoryTS = new TimeSeries();
        for (String word: words) {
            summedWeightHistoryTS = summedWeightHistoryTS.plus(weightHistory(word, startYear, endYear));
        }
        return summedWeightHistoryTS;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }
}
