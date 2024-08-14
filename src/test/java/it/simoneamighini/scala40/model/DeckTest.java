package it.simoneamighini.scala40.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    @Test
    void deckShouldContain108CardsAfterInitialisation() {
        Deck deck = new Deck();

        int count = 0;
        while (!deck.isEmpty()) {
            deck.draw();
            count++;
        }

        assertEquals (
                108,
                count
        );
    }
}