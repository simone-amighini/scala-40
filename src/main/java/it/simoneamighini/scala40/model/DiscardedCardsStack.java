package it.simoneamighini.scala40.model;

import java.util.EmptyStackException;
import java.util.Stack;

public class DiscardedCardsStack {
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
