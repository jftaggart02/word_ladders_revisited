import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class LadderGameExhaustive extends LadderGame {

    /**
     * Constructor. Read in the dictionary .txt file and store it as a 2D array.
     * Also create a copy of the dictionary to be searched through and modified during the word ladder algorithm.
     * */
    public LadderGameExhaustive(String dictionaryFile) {
        this.readDictionary(dictionaryFile);
        this.resetDictionaryCopy();
    }

    /**
     * Attempt to find the shortest word ladder from start to end.
     * Indicate if no word ladder through the dictionary can be found.
     * Throw an error if start and end are different lengths or if either are not in the dictionary.
     * */
    @Override
    public void play(String start, String end) throws IllegalArgumentException {

        // Create a fresh copy of the dictionary to search through and modify
        this.resetDictionaryCopy();

        // Convert to lower case
        start = start.toLowerCase();
        end = end.toLowerCase();

        // Ensure start and end words are the same length
        if (start.length() != end.length()) {
            throw new IllegalArgumentException("Start and end words must have the same length.");
        }

        // Special case: Start and end are the same
        if (start.equals(end)) {
            System.out.println(start + " -> " + end + " : 0 Moves [" + start + "] total enqueues 0");
            return;
        }

        // Ensure start and end words are in the dictionary
        ArrayList<String> wordsOfSameLength = this.getWordsOfLength(start.length());
        if (!wordsOfSameLength.contains(start) || !wordsOfSameLength.contains(end)) {
            throw new IllegalArgumentException("Start and end words must be in the dictionary.");
        }

        int moves = 0;
        int totalEnqueues = 0;

        // Create an initial ladder consisting of the start word and add it to the partial solution queue
        var solutionQueue = new Queue<WordInfo>();
        solutionQueue.enqueue(new WordInfo(start, moves));
        totalEnqueues++;

        // While queue is not empty and word ladder not complete
        while (!solutionQueue.isEmpty()) {

            // Remove the first item from the queue (this is the current shortest partial ladder)
            WordInfo currentShortest = solutionQueue.dequeue();

            // Update number of moves
            if (currentShortest.getMoves() == moves + 1) {
                moves++;
            }
            else if (currentShortest.getMoves() > moves + 1) {
                throw new AssertionError("Moves counting is invalid.");
            }

            // For each unused word in the dictionary (of the same length of the last word in the ladder) that is one away (has one letter different) from the last word in the partial ladder
            ArrayList<String> oneAwayWords = this.oneAway(currentShortest.getWord(), true);
            for (String w: oneAwayWords) {
                // If the word is equal to the end word, then the word ladder is complete
                if (w.equals(end)) {
                    System.out.println(start + " -> " + end + " : " + (moves + 1) + " Moves [" + currentShortest.getHistory() + " " + w + "] total enqueues " + totalEnqueues);
                    return;
                }

                // otherwise, extend the current ladder by appending the new word to a new WordInfo instance and add this new ladder to the queue
                else {
                    solutionQueue.enqueue(new WordInfo(w, moves + 1, currentShortest.getHistory() + " " + w));
                    totalEnqueues++;
                }

            }
        }
        System.out.println(start + " -> " + end + " : No ladder was found");

    }

    /**
     * Return a list of words that are one character off from the given word.
     * If specified, also remove the words from the dictionary.
     * */
    public ArrayList<String> oneAway(String word, boolean withRemoval) {
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

    /**
     * Find how many characters are different between two words of the same length.
     * */
    private int diff(String w1, String w2) {
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
     * Get a list of all words of a given length.
     * */
    private ArrayList<String> getWordsOfLength(int length) {
        return this.dictionaryCopy.get(length);
    }

}