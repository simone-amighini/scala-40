package it.simoneamighini.scala40.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Game implements Serializable {
    private final List<Player> players;
    private Match currentMatch;
    private int matchNumber;

    public Game(List<Player> players) throws IllegalArgumentException {
        this.players = new ArrayList<>();
        if (players.size() < 2 || players.size() > 6) {
            throw new IllegalArgumentException("Invalid number of players: " + players.size());
        }
        for (Player player : players) {
            addPlayer(player);
        }

        this.currentMatch = null;
        this.matchNumber = 0;

        shufflePlayers();
    }

    private void shufflePlayers() {
        Collections.shuffle(players);
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public List<Player> getActivePlayers() {
        return players.stream()
                .filter(player -> player.getPoints() < 201)
                .collect(Collectors.toList());
    }

    private void addPlayer(Player player) throws IllegalArgumentException {
        if (players.stream().anyMatch(p -> p.getUsername().equals(player.getUsername()))) {
            throw new IllegalArgumentException("Cannot add player " + player + " because its name is already in use");
        }

        players.add(player);
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public Player[] getRanking() {
        return players.stream()
                .sorted(Comparator.comparingInt(Player::getPoints))
                .toArray(Player[]::new);
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void startNewMatch() {
        currentMatch = new Match(this, getActivePlayers());
        matchNumber++;
    }

    void postMatchProcedure() throws EndOfGame, EndOfMatch {
        if (getActivePlayers().size() == 1) {
            // if only 1 player remains active, the game is finished
            throw new EndOfGame();
        } else {
            throw new EndOfMatch();
        }
    }
}
