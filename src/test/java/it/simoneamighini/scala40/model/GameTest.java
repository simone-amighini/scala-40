package it.simoneamighini.scala40.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void constructorTestWith2UniquePlayers() {
        List<Player> players = List.of(
                new Player("A"),
                new Player("B")
        );

        assertDoesNotThrow(() -> new Game(players));
    }

    @Test
    void constructorTestWith4UniquePlayers() {
        List<Player> players = List.of(
                new Player("A"),
                new Player("B"),
                new Player("C"),
                new Player("D")
        );

        assertDoesNotThrow(() -> new Game(players));
    }

    @Test
    void constructorTestWith6UniquePlayers() {
        List<Player> players = List.of(
                new Player("A"),
                new Player("B"),
                new Player("C"),
                new Player("D"),
                new Player("E"),
                new Player("F")
        );

        assertDoesNotThrow(() -> new Game(players));
    }

    @Test
    void constructorTestWith1Player() {
        List<Player> players = List.of(
                new Player("A")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Game(players)
        );
    }

    @Test
    void constructorTestWith3NonUniquePlayers() {
        List<Player> players = List.of(
                new Player("A"),
                new Player("B"),
                new Player("A")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Game(players)
        );
    }

    @Test
    void constructorTestWith7UniquePlayers() {
        List<Player> players = List.of(
                new Player("A"),
                new Player("B"),
                new Player("C"),
                new Player("D"),
                new Player("E"),
                new Player("F"),
                new Player("G")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new Game(players)
        );
    }
}