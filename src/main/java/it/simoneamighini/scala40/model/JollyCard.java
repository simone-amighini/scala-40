package it.simoneamighini.scala40.model;

public class JollyCard extends Card {
    private final SymbolColor symbolColor;

    JollyCard(SymbolColor symbolColor, BackColor backColor) {
        super("JOLLY_" + symbolColor + "_" + backColor, backColor);
        this.symbolColor = symbolColor;
    }

    SymbolColor getSymbolColor() {
        return symbolColor;
    }

    @Override
    int getDefaultPoints() {
        return 25;
    }
}
