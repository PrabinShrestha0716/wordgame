package uta.cse3310;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordSearchGame {
    public Puzzle gameBoard;
    public List<String> wordList;
    public Set<String> foundWords;
    public Scoreboard scoreboard;
    public String[] Msg;
    public int activeGames;
    public int GameId;
    public Player Players;
    public PlayerType players;
    public PlayerType CurrentTurn;

    public WordSearchGame(Puzzle gameBoard, List<String> wordList, HashSet<String> foundWords, Scoreboard scoreboard) {
        this.gameBoard = gameBoard;
        this.wordList = wordList;
        this.foundWords = foundWords;
        this.scoreboard = scoreboard;
        
    }
    WordSearchGame(){
        Msg = new String[2];
        Msg[0] = "Waiting for other player to join";
        Msg[1] = "";
        activeGames = 0;


    }

    public void startGame() {
        System.out.println("Starting the word search game...");
                // X player goes first. Because that is how it is.
                Msg[0] = "You are X. Your turn";
                Msg[1] = "You are O. Other players turn";
                CurrentTurn = PlayerType.Player1;

                

    }
    

    public void findWord(String word) {
        if (wordList.contains(word) && !foundWords.contains(word)) {
            foundWords.add(word);
            int points = word.length(); // Points based on word length
            scoreboard.updateScore(points); // Update player's score
            System.out.println("Word found: " + word + ". Scored " + points + " points!");
            if (foundWords.size() == wordList.size()) {
                endGame(); // If all words found, end the game
            }
        } else {
            System.out.println("Word not found or already found.");
        }
    }

    public void endGame() {
        System.out.println("All words found! Game over.");
        scoreboard.displayScore(); // Display final scores
    }

    public void displayRemainingWords() {
        System.out.println("Remaining words:");
        for (String word : wordList) {
            if (!foundWords.contains(word)) {
                System.out.println(word);
            }
        }
    }

    public void Update(UserEvent U) {
        System.out.println("The user event is " + U.name + "  " + U.Button);
    }
}
