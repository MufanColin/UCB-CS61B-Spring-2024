import java.util.ArrayList;
import java.util.List;

public class ListExercises {

    /** Returns the total sum in a list of integers */
	public static int sum(List<Integer> L) {
        // TODO: Fill in this function.
        int sumOfIntegers = 0;
        for (int i: L) {
            sumOfIntegers += i;
        }
        return sumOfIntegers;
    }

    /** Returns a list containing the even numbers of the given list */
    public static List<Integer> evens(List<Integer> L) {
        // TODO: Fill in this function.
        List<Integer> evenNumbers = new ArrayList<>();
        for (int i: L) {
            if (i % 2 == 0) {
                evenNumbers.add(i);
            }
        }
        return evenNumbers;
    }

    /** Returns a list containing the common item of the two given lists */
    public static List<Integer> common(List<Integer> L1, List<Integer> L2) {
        // TODO: Fill in this function.
        List<Integer> commonItems = new ArrayList<>();
        for (int i: L1) {
            if (L2.contains(i)) {
                commonItems.add(i);
            }
        }
        return commonItems;
    }


    /** Returns the number of occurrences of the given character in a list of strings. */
    public static int countOccurrencesOfC(List<String> words, char c) {
        // TODO: Fill in this function.
        int count = 0;
        for (String word: words) {
            int n = word.length();
            for (int i = 0; i < n; i++) {
                if (word.charAt(i) == c) {
                    count += 1;
                }
            }
        }
        return count;
    }
}
