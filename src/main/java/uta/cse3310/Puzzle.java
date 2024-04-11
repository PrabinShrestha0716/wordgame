package uta.cse3310;

import java.util.ArrayList;
import java.util.Random;

public class Puzzle {
    private char[][] wordSearchPuzzle;
    private int rows;
    private int columns;
    private char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private WordList wordList = new WordList();
    private ArrayList<String> words = wordList.getWordList();
    private Random rand = new Random();

    public Puzzle(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.wordSearchPuzzle = new char[rows][columns];
        initializeBoard();
        fillRandomLetters();
    }

    public void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                wordSearchPuzzle[i][j] = '-';
            }
        }
        for (String word : words) {
            placeWord(word.toUpperCase());
        }
    }

    public void displayPuzzle() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(wordSearchPuzzle[i][j]);
            }
            System.out.println();
        }
    }

    public void fillRandomLetters() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (wordSearchPuzzle[i][j] == '-') {
                    wordSearchPuzzle[i][j] = letters[rand.nextInt(letters.length)];
                }

            }
        }
    }

    public void placeWord(String word) {
        int startRow = rand.nextInt(rows);
        int startColumn = rand.nextInt(columns);
        int direction = rand.nextInt(3);
        int wordLength = word.length();
        int endRow = startRow;
        int endColumn = startColumn;

        if (direction == 0) {
            endColumn += wordLength - 1;
        } else if (direction == 1) {
            endRow += wordLength - 1;
        } else {
            endRow += wordLength - 1;
            endColumn += wordLength - 1;
        }
        if (endRow >= rows || endColumn >= columns) {
            placeWord(word);
            return;
        }
        for (int i = 0; i < wordLength; i++) {
            char currentChar = word.charAt(i);
            char existingChar = wordSearchPuzzle[startRow][startColumn];
            if (existingChar != '-' && existingChar != currentChar) {
                placeWord(word);
                return;
            }
            if (direction == 0) {
                if (startColumn + 1 < columns) {
                    startColumn++;
                }
            } else if (direction == 1) {
                if (startRow + 1 < rows) {
                    startRow++;
                }
            } else if (startRow + 1 < rows && startColumn + 1 < columns) {
                startRow++;
                startColumn++;
            }
        }
        for (int i = 0; i < wordLength; i++) {
            if (direction == 0) {
                wordSearchPuzzle[startRow][startColumn - wordLength + 1 + i] = word.charAt(i);
            } else if (direction == 1) {
                wordSearchPuzzle[startRow - wordLength + 1 + i][startColumn] = word.charAt(i);
            } else {
                wordSearchPuzzle[startRow - wordLength + 1 + i][startColumn - wordLength + 1 + i] = word.charAt(i);
            }
        }
    }

    public char[][] getGrid() {
        return wordSearchPuzzle;
    }
}
