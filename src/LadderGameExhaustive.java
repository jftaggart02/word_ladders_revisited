import java.util.ArrayList;

public class LadderGameExhaustive extends LadderGame {

    public LadderGameExhaustive(String dictionaryFile) {
        super(dictionaryFile);
    }

    /**
     * Attempt to find the shortest word ladder from start to end using an exhaustive search.
     * */
    @Override
    public void play(String start, String end) throws IllegalArgumentException {

        start = start.toLowerCase();
        end = end.toLowerCase();
        reset();
        validateInput(start, end);

        System.out.println("Seeking exhaustive solution from " + start + " -> " + end);

        // Special case: Start and end are the same. No need to run algorithm.
        if (start.equals(end)) {
            System.out.println(" [" + start + " " + end + "] total enqueues 0");
            return;
        }

        // Create an initial ladder consisting of the start word and add it to the partial solution queue
        var solutionQueue = new Queue<WordInfo>();
        solutionQueue.enqueue(new WordInfo(start, 0));
        totalEnqueues++;

        // While queue is not empty and word ladder not complete
        while (!solutionQueue.isEmpty()) {

            // Remove the first item from the queue (this is the current shortest partial ladder)
            WordInfo currentShortest = solutionQueue.dequeue();

            // For each unused word in the dictionary (of the same length of the last word in the ladder) that is one away (has one letter different) from the last word in the partial ladder
            ArrayList<String> oneAwayWords = this.oneAway(currentShortest.getWord(), true);
            for (String w: oneAwayWords) {
                // If the word is equal to the end word, then the word ladder is complete
                if (w.equals(end)) {
                    System.out.println(" [" + currentShortest.getHistory() + " " + w + "] total enqueues " + totalEnqueues);
                    return;
                }

                // otherwise, extend the current ladder by appending the new word to a new WordInfo instance and add this new ladder to the queue
                else {
                    solutionQueue.enqueue(new WordInfo(w, currentShortest.getMoves() + 1, currentShortest.getHistory() + " " + w));
                    totalEnqueues++;
                }

            }
        }
        System.out.println(" No ladder was found.");

    }

}