package it.simoneamighini.scala40.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombinationTest {
    @Test
    void correctCombination_2_2_2_2() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TWO,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.TWO,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.HEARTS,
                        Rank.TWO,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.SPADES,
                        Rank.TWO,
                        BackColor.DARK
                )
        );

        assertDoesNotThrow(() -> new Combination(cards));
    }

    @Test
    void correctCombination_K_K_O_K() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.KING,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.KING,
                        BackColor.RED
                ),
                new JollyCard(
                        SymbolColor.BLACK,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.SPADES,
                        Rank.KING,
                        BackColor.DARK
                )
        );

        assertDoesNotThrow(() -> new Combination(cards));
    }

    @Test
    void invalidCombination_2_2_2_2_O() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TWO,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.TWO,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.HEARTS,
                        Rank.TWO,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.SPADES,
                        Rank.TWO,
                        BackColor.DARK
                ),
                new JollyCard(
                        SymbolColor.BLACK,
                        BackColor.RED
                )
        );

        assertThrows(
                InvalidGroupException.class,
                () -> new Combination(cards)
        );
    }

    @Test
    void invalidCombination_J_J_J_7() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.JACK,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.HEARTS,
                        Rank.JACK,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.SPADES,
                        Rank.SEVEN,
                        BackColor.DARK
                )
        );

        assertThrows(
                InvalidGroupException.class,
                () -> new Combination(cards)
        );
    }

    @Test
    void invalidCombinationWithCardRepetition_J_J_J() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.HEARTS,
                        Rank.JACK,
                        BackColor.RED
                )
        );

        assertThrows(
                InvalidGroupException.class,
                () -> new Combination(cards)
        );
    }

    @Test
    void invalidCombinationWithTwoJolly() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TWO,
                        BackColor.DARK
                ),
                new JollyCard(
                        SymbolColor.RED,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.HEARTS,
                        Rank.TWO,
                        BackColor.RED
                ),
                new JollyCard(
                        SymbolColor.BLACK,
                        BackColor.RED
                )
        );

        assertThrows(
                InvalidGroupException.class,
                () -> new Combination(cards)
        );
    }
}