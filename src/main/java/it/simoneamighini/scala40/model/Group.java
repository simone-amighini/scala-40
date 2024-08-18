package it.simoneamighini.scala40.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Group {
    private final List<Card> cards;

    Group(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    abstract int getPoints();

    abstract void accept(SingleCardAttacher visitor);

    abstract void accept(GroupAttacher visitor);
}
