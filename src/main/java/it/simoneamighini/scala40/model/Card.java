package it.simoneamighini.scala40.model;

public abstract class Card {
    private final String name;

    Card(String name, BackColor backColor) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract int getDefaultPoints();

    @Override
    public boolean equals(Object object) {
        return (object instanceof Card) && (((Card) object).name.equals(name));
    }
}
