package uta.cse3310;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private static final int MAX_PLAYERS = 10;
    private List<Player> players;

    public Lobby() {
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        if (players.size() < MAX_PLAYERS) {
            players.add(player);
        } else {
            System.out.println("Lobby is full. Cannot add more players.");
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }
}
