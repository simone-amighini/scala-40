package it.simoneamighini.scala40.model;

import java.io.Serializable;

public abstract class Card implements Serializable {
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
