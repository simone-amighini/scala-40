package it.simoneamighini.scala40.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SequenceTest {
    @Test
    void correctSequence_A_2_3_4_5_6_7_8_9_10_J_Q_K() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.ACE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TWO,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.THREE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.FOUR,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.FIVE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.SIX,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.SEVEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.EIGHT,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.NINE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.QUEEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.KING,
                        BackColor.DARK
                )
        );

        assertDoesNotThrow(() -> new Sequence(cards));
    }

    @Test
    void correctSequence_A_2_3_4_5_6_7_8_9_10_J_Q_K_O() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.ACE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TWO,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.THREE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.FOUR,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.FIVE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.SIX,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.SEVEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.EIGHT,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.NINE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.QUEEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.KING,
                        BackColor.DARK
                ),
                new JollyCard(
                        SymbolColor.RED,
                        BackColor.DARK
                )
        );

        assertDoesNotThrow(() -> new Sequence(cards));
    }

    @Test
    void correctSequence_O_2_3_4_5_6_7_8_9_10_J_Q_K_A() {
        List<Card> cards = List.of(
                new JollyCard(
                        SymbolColor.RED,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TWO,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.THREE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.FOUR,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.FIVE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.SIX,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.SEVEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.EIGHT,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.NINE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.QUEEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.KING,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.ACE,
                        BackColor.DARK
                )
        );

        assertDoesNotThrow(() -> new Sequence(cards));
    }

    @Test
    void invalidSequence_K_O_2_3_4_5_6_7_8_9_10_J_Q_K() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.KING,
                        BackColor.DARK
                ),
                new JollyCard(
                        SymbolColor.RED,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TWO,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.THREE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.FOUR,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.FIVE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.SIX,
                        BackColor.RED
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.SEVEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.EIGHT,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.NINE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.QUEEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.KING,
                        BackColor.DARK
                )
        );

        assertThrows(
                InvalidGroupException.class,
                () -> new Sequence(cards)
        );
    }

    @Test
    void invalidSequence_J_Q_K_A_2() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.QUEEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.KING,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.ACE,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.CLUBS,
                        Rank.TWO,
                        BackColor.DARK
                )
        );

        assertThrows(
                InvalidGroupException.class,
                () -> new Sequence(cards)
        );
    }

    @Test
    void invalidSequence_J_O_Q_K_A() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new JollyCard(
                        SymbolColor.RED,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.QUEEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.KING,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.ACE,
                        BackColor.DARK
                )
        );

        assertThrows(
                InvalidGroupException.class,
                () -> new Sequence(cards)
        );
    }

    @Test
    void invalidSequenceWithDifferentSuits() {
        List<Card> cards = List.of(
                new FrenchCard(
                        Suit.SPADES,
                        Rank.JACK,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.QUEEN,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.KING,
                        BackColor.DARK
                ),
                new FrenchCard(
                        Suit.DIAMONDS,
                        Rank.ACE,
                        BackColor.DARK
                )
        );

        assertThrows(
                InvalidGroupException.class,
                () -> new Sequence(cards)
        );
    }
}