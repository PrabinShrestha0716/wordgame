package uta.cse3310;

import java.util.HashMap;
import java.util.Map;

public class Scoreboard {
    private Map<Player, Integer> scores;

    public Scoreboard() {
        this.scores = new HashMap<>();
    }

    // Update the score for a specific player
    public void updateScore(Player player, int points) {
        scores.put(player, scores.getOrDefault(player, 0) + points);
    }

    // Update the overall scoreboard without associating the score with a specific player
    public void updateScore(int points) {
        // Update the score for all players (if applicable)
        for (Player player : scores.keySet()) {
            scores.put(player, scores.getOrDefault(player, 0) + points);
        }
    }

    // Get the score for a specific player
    public int getScore(Player player) {
        return scores.getOrDefault(player, 0);
    }

    // Display the scoreboard
    public void displayScore() {
        System.out.println("Scoreboard:");
        for (Map.Entry<Player, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
        }
    }
}
