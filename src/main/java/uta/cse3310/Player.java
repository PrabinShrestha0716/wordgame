package uta.cse3310;

public class Player {
    private String name;
    private int score;
    private Scoreboard scoreboard;

    public Player(String name) {
        this.name = name;
    
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int points) {
      score += points;
      if (scoreboard != null) {
          scoreboard.updateScore(this, points); // Update scoreboard
      }
    }
  }
