package it.simoneamighini.scala40.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpeningPlacerTest {
    private final List<List<Card>> groupList1 = List.of(
            List.of(
                    new FrenchCard(
                            Suit.HEARTS,
                            Rank.FIVE,
                            BackColor.DARK
                    ),
                    new FrenchCard(
                            Suit.HEARTS,
                            Rank.SIX,
                            BackColor.DARK
                    ),
                    new FrenchCard(
                            Suit.HEARTS,
                            Rank.SEVEN,
                            BackColor.RED
                    )
            ),
            List.of(
                    new FrenchCard(
                            Suit.CLUBS,
                            Rank.TEN,
                            BackColor.RED
                    ),
                    new FrenchCard(
                            Suit.CLUBS,
                            Rank.JACK,
                            BackColor.DARK
                    ),
                    new JollyCard(
                            SymbolColor.BLACK,
                            BackColor.DARK
                    )
            )
    );

    @Test
    void testOnGroupList1() {
        assertTrue(() -> new OpeningPlacer(MatchGenerator.generateMatch()).place(groupList1));
    }
}