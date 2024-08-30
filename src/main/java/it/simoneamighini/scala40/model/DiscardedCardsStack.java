package it.simoneamighini.scala40.model;

import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Stack;

public class DiscardedCardsStack implements Serializable {
    private final Stack<Card> cards;

    DiscardedCardsStack() {
        this.cards = new Stack<>();
    }

    Card pickTopCard() {
        try {
            return cards.pop();
        } catch (EmptyStackException exception) {
            return null;
        }
    }

    public Card viewTopCard() {
        return cards.peek();
    }

    void put(Card card) {
        cards.push(card);
    }
}
