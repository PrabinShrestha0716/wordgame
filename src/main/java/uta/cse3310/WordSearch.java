package uta.cse3310;

public class WordSearch {
    private String word;
    private int[] startPosition;
    private String direction;

    public WordSearch(String word, int[] startPosition, String direction) {
        this.word = word;
        this.startPosition = startPosition;
        this.direction = direction;
    }

    public String getWord() {
        return word;
    }

    public int[] getStartPosition() {
        return startPosition;
    }

    public String getDirection() {
        return direction;
    }
}
