package it.simoneamighini.scala40.model;

public class FrenchCard extends Card {
    private final Suit suit;
    private final Rank rank;

    FrenchCard(Suit suit, Rank rank, BackColor backColor) {
        super(suit + "_" + rank + "_" + backColor, backColor);
        this.suit = suit;
        this.rank = rank;
    }

    Suit getSuit() {
        return suit;
    }

    Rank getRank() {
        return rank;
    }
}
