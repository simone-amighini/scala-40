package it.simoneamighini.scala40.model;

enum Rank {
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING;

    Rank previous() {
        return Rank.values()[(this.ordinal() - 1 + 13) % 13];
    }

    Rank next() {
        return Rank.values()[(this.ordinal() + 1 + 13) % 13];
    }

    boolean precedes(Rank rank) {
        return this == rank.previous();
    }

    boolean succeeds(Rank rank) {
        return this == rank.next();
    }
}
