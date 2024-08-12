package it.simoneamighini.scala40.model;

public abstract class Card {
    private final String name;

    Card(String name, BackColor backColor) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
