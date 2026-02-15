public class WordInfoPriority extends WordInfo implements Comparable<WordInfoPriority> {

    public WordInfoPriority(String word, int moves, int estimatedWork) {
        super(word, moves);
        this.priority = estimatedWork + moves;
    }

    public WordInfoPriority(String word, int moves, int estimatedWork, String history) {
        super(word, moves, history);
        this.priority = estimatedWork + moves;
    }

    public int getPriority() { return this.priority; }

    @Override
    public int compareTo(WordInfoPriority w) {
        return Integer.compare(this.getPriority(), w.getPriority());
    }

    private int priority;

}
