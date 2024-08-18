package it.simoneamighini.scala40.model;

import it.simoneamighini.scala40.utils.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SingleCardAttacherTest {
    private Combination getCombination_8_8_8_missingHearts() {
        List<Card> cards = List.of(
                new FrenchCard(Suit.SPADES, Rank.EIGHT, BackColor.DARK),
                new FrenchCard(Suit.CLUBS, Rank.EIGHT, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.EIGHT, BackColor.DARK)
        );

        try {
            return new Combination(cards);
        } catch (InvalidGroupException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Combination getFullCombination_A_A_A_A() {
        List<Card> cards = List.of(
                new FrenchCard(Suit.SPADES, Rank.ACE, BackColor.DARK),
                new FrenchCard(Suit.CLUBS, Rank.ACE, BackColor.RED),
                new FrenchCard(Suit.DIAMONDS, Rank.ACE, BackColor.DARK),
                new FrenchCard(Suit.HEARTS, Rank.ACE, BackColor.RED)
        );

        try {
            return new Combination(cards);
        } catch (InvalidGroupException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Sequence getSequence_2_3_4_5_6_7_8_9_10_J_Q_Diamonds() {
        List<Card> cards = List.of(
                new FrenchCard(Suit.DIAMONDS, Rank.TWO, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.THREE, BackColor.RED),
                new FrenchCard(Suit.DIAMONDS, Rank.FOUR, BackColor.RED),
                new FrenchCard(Suit.DIAMONDS, Rank.FIVE, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.SIX, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.SEVEN, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.EIGHT, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.NINE, BackColor.RED),
                new FrenchCard(Suit.DIAMONDS, Rank.TEN, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.JACK, BackColor.RED),
                new FrenchCard(Suit.DIAMONDS, Rank.QUEEN, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.KING, BackColor.RED)
        );

        try {
            return new Sequence(cards);
        } catch (InvalidGroupException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    void attachTest1() {
        Match match = MatchGenerator.generateMatch();
        SingleCardAttacher attacher = match.getSingleCardAttacher();
        Card heartsEight = new FrenchCard(Suit.HEARTS, Rank.EIGHT, BackColor.DARK);

        match.addGroup(getCombination_8_8_8_missingHearts());

        assertTrue(attacher.attach(heartsEight, 0, Position.START));
    }

    @Test
    void attachTest2() {
        Match match = MatchGenerator.generateMatch();
        SingleCardAttacher attacher = match.getSingleCardAttacher();
        Card spadesEight = new FrenchCard(Suit.SPADES, Rank.EIGHT, BackColor.DARK);

        match.addGroup(getCombination_8_8_8_missingHearts());

        assertFalse(attacher.attach(spadesEight, 0, Position.START));
    }

    @Test
    void attachTest3() {
        Match match = MatchGenerator.generateMatch();
        SingleCardAttacher attacher = match.getSingleCardAttacher();
        Card jolly = new JollyCard(SymbolColor.RED, BackColor.RED);

        match.addGroup(getCombination_8_8_8_missingHearts());

        assertTrue(attacher.attach(jolly, 0, Position.END));
    }

    @Test
    void attachTest4() {
        Match match = MatchGenerator.generateMatch();
        SingleCardAttacher attacher = match.getSingleCardAttacher();
        Card diamondsAce = new FrenchCard(Suit.DIAMONDS, Rank.ACE, BackColor.DARK);

        match.addGroup(getFullCombination_A_A_A_A());

        assertFalse(attacher.attach(diamondsAce, 0, Position.START));
    }

    @Test
    void attachTest5() {
        Match match = MatchGenerator.generateMatch();
        SingleCardAttacher attacher = match.getSingleCardAttacher();
        Card diamondsAceBlack = new FrenchCard(Suit.DIAMONDS, Rank.ACE, BackColor.DARK);
        Card diamondsAceRed = new FrenchCard(Suit.DIAMONDS, Rank.ACE, BackColor.DARK);

        match.addGroup(getSequence_2_3_4_5_6_7_8_9_10_J_Q_Diamonds());

        assertTrue(attacher.attach(diamondsAceBlack, 0, Position.START));
        assertTrue(attacher.attach(diamondsAceRed, 0, Position.END));
    }
}