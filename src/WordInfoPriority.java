public class WordInfoPriority implements Comparable<WordInfoPriority> {

    public WordInfoPriority(String word, int moves, int estimatedWork) {
        this.word = word;
        this.moves = moves;
        this.priority = moves + estimatedWork;
        this.history = word;
    }

    public WordInfoPriority(String word, int moves, int estimatedWork, String history) {
        this.word = word;
        this.moves = moves;
        this.priority = moves + estimatedWork;
        this.history = history;
    }

    public String getWord() {
        return this.word;
    }

    public int getMoves() {
        return this.moves;
    }

    public String getHistory() {
        return this.history;
    }

    public int getPriority() { return this.priority; }

    @Override
    public int compareTo(WordInfoPriority w) {
        return Integer.compare(this.getPriority(), w.getPriority());
    }

    private int priority;
    private String word;
    private int moves;
    private String history;

}
