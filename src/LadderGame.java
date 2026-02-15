import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class LadderGame {

    LadderGame(String dictionaryFile) {
        readDictionary(dictionaryFile);
        reset();
    }

    public abstract void play(String start, String end);

    /**
     * Display a list containing the specified number of words of a given length.
     * */
    public void listWords(int length, int howMany) {

        int count = 0;
        for (String word: this.getWordsOfLength(length)) {
            System.out.println(word);
            count++;
            if (count >= howMany) {
                break;
            }
        }

    }

    protected int totalEnqueues;

    /**
     * Return a list of words that are one character off from the given word.
     * If specified, also remove the words from the dictionary.
     * */
    protected ArrayList<String> oneAway(String word, boolean withRemoval) {
        ArrayList<String> words = new ArrayList<>();

        ArrayList<String> wordsOfSameLength = this.getWordsOfLength(word.length());

        // Find all words that have only one letter difference
        for (String w : wordsOfSameLength) {
            if (diff(word, w) == 1) {
                words.add(w);
            }
        }

        // Remove them from the dictionary if specified
        if (withRemoval) {
            wordsOfSameLength.removeAll(words);
        }

        return words;
    }

    protected void reset() {
        resetDictionaryCopy();
        totalEnqueues = 0;
    }

    /**
     * Get a list of all words of a given length.
     * */
    protected ArrayList<String> getWordsOfLength(int length) {
        return this.dictionaryCopy.get(length);
    }

    /**
     * Find how many characters are different between two words of the same length.
     * */
    protected static int diff(String w1, String w2) {
        assert w1.length() == w2.length();

        int count = 0;
        for (int i = 0; i < w1.length(); i++) {
            if (w1.charAt(i) != w2.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Assumes start and end are lowercase.
     * */
    protected void validateInput(String start, String end) throws IllegalArgumentException {

        // Ensure start and end words are the same length
        if (start.length() != end.length()) {
            throw new IllegalArgumentException("Start and end words must have the same length.");
        }

        // Ensure start and end words are in the dictionary
        ArrayList<String> wordsOfSameLength = this.getWordsOfLength(start.length());
        if (!wordsOfSameLength.contains(start) || !wordsOfSameLength.contains(end)) {
            throw new IllegalArgumentException("Start and end words must be in the dictionary.");
        }
    }

    private ArrayList<ArrayList<String>> dictionary;
    private ArrayList<ArrayList<String>> dictionaryCopy;

    /**
     * Read a list of words from a file and put all words of the same length into the same array.
     * */
    private void readDictionary(String dictionaryFile) {
        File file = new File(dictionaryFile);
        ArrayList<String> allWords = new ArrayList<>();

        //
        // Track the longest word, because that tells us how big to make the array.
        int longestWord = 0;
        try (Scanner input = new Scanner(file)) {
            //
            // Start by reading all the words into memory.
            while (input.hasNextLine()) {
                String word = input.nextLine().toLowerCase();
                allWords.add(word);
                longestWord = Math.max(longestWord, word.length());
            }

            // For every word length from 1 to longestWord, initialize an empty ArrayList
            this.dictionary = new ArrayList<>();
            for (int i = 0; i <= longestWord; i++) {
                this.dictionary.add(new ArrayList<>());
            }

            // Next, loop through allWords and add each word to its proper place in this.dictionary
            // The index into this.dictionary corresponds to word length - 1.
            for (String word : allWords) {
                this.dictionary.get(word.length()).add(word);
            }

        }
        catch (java.io.IOException ex) {
            System.out.println("An error occurred trying to read the dictionary: " + ex);
        }
    }

    /**
     * Create a deep copy of the dictionary to be searched through and modified by the word ladder algorithm.
     * */
    private void resetDictionaryCopy() {
        this.dictionaryCopy = new ArrayList<>();

        for (ArrayList<String> a: this.dictionary) {
            this.dictionaryCopy.add(new ArrayList<>(a));
        }

        // Do some checking to make sure we did the deep copy right
        assert this.dictionary.size() == this.dictionaryCopy.size();
        for (int i = 0; i < this.dictionary.size(); i++) {
            assert this.dictionary.get(i).size() == this.dictionaryCopy.get(i).size();
        }
    }

}
