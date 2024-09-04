package it.simoneamighini.scala40.model;

import java.util.List;

public class MatchGenerator {
    public static Match generateMatch() {
        List<Player> players = List.of(
                new Player("A"),
                new Player("B"),
                new Player("C"),
                new Player("D")
        );

        Game game = new Game(players);
        game.startNewMatch();

        return game.getCurrentMatch();
    }
}
