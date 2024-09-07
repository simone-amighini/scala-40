package it.simoneamighini.scala40.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JollyReplacerTest {
    private Combination getCombination_4_4_4_O_missingClubs() {
        List<Card> cards = List.of(
                new FrenchCard(Suit.HEARTS, Rank.FOUR, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.FOUR, BackColor.DARK),
                new FrenchCard(Suit.SPADES, Rank.FOUR, BackColor.DARK),
                new JollyCard(SymbolColor.RED, BackColor.DARK)
        );

        try {
            return new Combination(cards, false);
        } catch (InvalidGroupException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Sequence getSequence_O_10_J_Q_K_diamonds() {
        List<Card> cards = List.of(
                new JollyCard(SymbolColor.BLACK, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.TEN, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.JACK, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.QUEEN, BackColor.DARK),
                new FrenchCard(Suit.DIAMONDS, Rank.KING, BackColor.DARK)
        );

        try {
            return new Sequence(cards, false);
        } catch (InvalidGroupException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    void jollyReplaceTest1() {
        Match match = MatchGenerator.generateMatch();
        JollyReplacer jollyReplacer = match.getJollyReplacer();
        Card clubsFour = new FrenchCard(Suit.CLUBS, Rank.FOUR, BackColor.RED);
        JollyCard exceptedReturnedJolly = new JollyCard(SymbolColor.RED, BackColor.DARK);

        match.addGroup(getCombination_4_4_4_O_missingClubs());

        assertEquals(exceptedReturnedJolly, jollyReplacer.replace(clubsFour, 0, 3));
    }

    @Test
    void jollyReplaceTest2() {
        Match match = MatchGenerator.generateMatch();
        JollyReplacer jollyReplacer = match.getJollyReplacer();
        Card diamondsFour = new FrenchCard(Suit.DIAMONDS, Rank.FOUR, BackColor.RED);

        match.addGroup(getCombination_4_4_4_O_missingClubs());

        assertNull(jollyReplacer.replace(diamondsFour, 0, 3));
    }

    @Test
    void jollyReplaceTest3() {
        Match match = MatchGenerator.generateMatch();
        JollyReplacer jollyReplacer = match.getJollyReplacer();
        Card diamondsNine = new FrenchCard(Suit.DIAMONDS, Rank.NINE, BackColor.DARK);
        JollyCard exceptedReturnedJolly = new JollyCard(SymbolColor.BLACK, BackColor.DARK);

        match.addGroup(getSequence_O_10_J_Q_K_diamonds());

        assertEquals(exceptedReturnedJolly, jollyReplacer.replace(diamondsNine, 0, 0));
    }
}