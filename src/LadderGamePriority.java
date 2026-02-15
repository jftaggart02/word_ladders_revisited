import java.util.ArrayList;

public class LadderGamePriority extends LadderGame {

    public LadderGamePriority(String dictionaryFile) {
        super(dictionaryFile);
    }

    /**
     * Attempt to find the shortest word ladder from start to end using A*.
     * */
    @Override
    public void play(String start, String end) throws IllegalArgumentException {

        start = start.toLowerCase();
        end = end.toLowerCase();
        reset();
        validateInput(start, end);

        System.out.println("Seeking A* solution from " + start + " -> " + end);

        // Special case: Start and end are the same
        if (start.equals(end)) {
            System.out.println(" [" + start + " " + end + "] total enqueues 0");
            return;
        }

        // Keep track of previously used words
        var previousWords = new AVLTree<PreviousWord>();

        // Initialize priority queue
        var priorityQueue = new AVLTree<WordInfoPriority>();

        // Begin by inserting the initial state into a priority queue (AVL tree)
        priorityQueue.insert(new WordInfoPriority(start, 0, diff(start, end)));
        previousWords.insert(new PreviousWord(start, 0));
        totalEnqueues++;

        while (!priorityQueue.isEmpty()) {
            WordInfoPriority currentBest = priorityQueue.deleteMin();

            // Add to the priority queue all neighboring states; those that can be reached in one more move
            ArrayList<String> oneAwayWords = oneAway(currentBest.getWord(), false);
            int currentMoves = currentBest.getMoves() + 1;
            for (String word: oneAwayWords) {

                // We found the word
                if (word.equals(end)) {
                    System.out.println(" [" + currentBest.getHistory() + " " + word + "] total enqueues " + totalEnqueues);
                    return;
                }

                else {

                    // If the word has already been visited, but we found a shorter path to the word, add it to the queue.
                    PreviousWord finder = new PreviousWord(word, 0);
                    if (previousWords.contains(finder)) {
                        PreviousWord wordFound = previousWords.find(finder);
                        if (currentMoves < wordFound.moves) {
                            wordFound.moves = currentMoves;  // Update the fewest number of moves to get to the word.
                            priorityQueue.insert(new WordInfoPriority(word, currentMoves, diff(word, end), currentBest.getHistory() + " " + word));
                            totalEnqueues++;
                        }
                    }
                    // If the word hasn't already been visited, add it to the queue.
                    else {
                        priorityQueue.insert(new WordInfoPriority(word, currentMoves, diff(word, end), currentBest.getHistory() + " " + word));
                        previousWords.insert(new PreviousWord(word, currentMoves));
                        totalEnqueues++;
                    }

                }

            }

        }

        System.out.println(" No ladder was found. Total enqueues " + totalEnqueues);

    }

    /**
     * A simple class for storing previous words and their word ladder size.
     * */
    private static class PreviousWord implements Comparable<PreviousWord> {

        public String word;
        public int moves;

        PreviousWord(String word, int moves) {
            this.word = word;
            this.moves = moves;
        }

        @Override
        public int compareTo(PreviousWord other) {
            return word.compareTo(other.word);
        }

        @Override
        public String toString() {
            return String.format("Word %s Moves %d", word, moves);
        }
    }

}
