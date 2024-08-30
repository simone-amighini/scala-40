package it.simoneamighini.scala40.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class Deck implements Serializable {
    private final Stack<Card> cards;

    Deck() {
        this.cards = new Stack<>();

        // add french cards (2 decks = 52x2 cards = 104 cards)
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                for (BackColor backColor : BackColor.values()) {
                    cards.push(new FrenchCard(suit, rank, backColor));
                }
            }
        }

        // add 2x2 jollies = 4 jollies
        for (BackColor backColor : BackColor.values()) {
            cards.push(new JollyCard(SymbolColor.BLACK, backColor));
            cards.push(new JollyCard(SymbolColor.RED, backColor));
        }

        shuffle();
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    Card draw() {
        try {
            return cards.pop();
        } catch (EmptyStackException exception) {
            return null;
        }
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    void fillWith(List<Card> cards) throws IllegalStateException {
        if (isEmpty()) {
            for (Card card : cards) {
                this.cards.push(card);
            }
            shuffle();
        } else {
            throw new IllegalStateException("Could not refill deck because it is not empty.");
        }
    }
}
