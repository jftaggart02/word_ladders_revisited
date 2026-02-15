import java.util.ArrayList;
import java.util.Scanner;

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
//        printState(previousWords, priorityQueue);

        // Main Loop
//        var scanner = new Scanner(System.in);
        while (!priorityQueue.isEmpty()) {
            WordInfoPriority currentBest = priorityQueue.deleteMin();
            assert !priorityQueue.contains(currentBest);
//            System.out.println("Current best is: " + currentBest);

            // Add to the priority queue all neighboring states; those that can be reached in one more move
            ArrayList<String> oneAwayWords = oneAway(currentBest.getWord(), false);
            int currentMoves = currentBest.getMoves() + 1;
            for (String word: oneAwayWords) {

                if (word.equals(end)) {
                    System.out.println(" [" + currentBest.getHistory() + " " + word + "] total enqueues " + totalEnqueues);
                    return;
                }

                else {

                    try {
//                        System.out.println("Finding " + word);
                        PreviousWord wordFound = previousWords.find(new PreviousWord(word, 0));
//                        System.out.println("Found in previous moves: " + wordFound);
                        if (currentMoves < wordFound.moves) {
//                            System.out.println("Found shorter path to " + word + ". Adding to queue.");
                            wordFound.moves = currentMoves;
                            priorityQueue.insert(new WordInfoPriority(word, currentMoves, diff(word, end), currentBest.getHistory() + " " + word));
                            totalEnqueues++;
                        }

                    }
                    // Previous word not found
                    catch (RuntimeException ex) {
//                        System.out.println("Not found in previous moves: " + word + ". Adding to queue.");
                        priorityQueue.insert(new WordInfoPriority(word, currentMoves, diff(word, end), currentBest.getHistory() + " " + word));
                        previousWords.insert(new PreviousWord(word, currentMoves));
                        totalEnqueues++;

                    }

                }

            }

//            printState(previousWords, priorityQueue);
//            scanner.nextLine();


        }

        System.out.println(" No ladder was found. Total enqueues " + totalEnqueues);

    }

    /**
     * Debug function
     * */
    private void printState(AVLTree<PreviousWord> previousWords, AVLTree<WordInfoPriority> priorityQueue) {
        previousWords.printTree("Previous words:");
        System.out.println();
        priorityQueue.printTree("Priority queue:");
        System.out.println();
        System.out.println();
    }

    /**
     * A wrapper around WordInfo class that implements comparable interface using the word.
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

