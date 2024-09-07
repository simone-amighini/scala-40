package it.simoneamighini.scala40.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttachmentCheckerTest {
    private Sequence getSequence_10_J_Q_K_hearts() {
        List<Card> cards = List.of(
                new FrenchCard(Suit.HEARTS, Rank.TEN, BackColor.DARK),
                new FrenchCard(Suit.HEARTS, Rank.JACK, BackColor.DARK),
                new FrenchCard(Suit.HEARTS, Rank.QUEEN, BackColor.DARK),
                new FrenchCard(Suit.HEARTS, Rank.KING, BackColor.DARK)
        );

        try {
            return new Sequence(cards, false);
        } catch (InvalidGroupException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Sequence getSequence_10_J_O_K_hearts() {
        List<Card> cards = List.of(
                new FrenchCard(Suit.HEARTS, Rank.TEN, BackColor.DARK),
                new FrenchCard(Suit.HEARTS, Rank.JACK, BackColor.DARK),
                new JollyCard(SymbolColor.BLACK, BackColor.DARK),
                new FrenchCard(Suit.HEARTS, Rank.KING, BackColor.DARK)
        );

        try {
            return new Sequence(cards, false);
        } catch (InvalidGroupException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Test
    void test1() {
        Match match = MatchGenerator.generateMatch();
        match.addGroup(getSequence_10_J_Q_K_hearts());
        Card heartsNine = new FrenchCard(Suit.HEARTS, Rank.NINE, BackColor.DARK);

        assertTrue(() -> match.getAttachmentChecker().isAttachable(heartsNine));
    }

    @Test
    void test2() {
        Match match = MatchGenerator.generateMatch();
        match.addGroup(getSequence_10_J_O_K_hearts());
        Card heartsNine = new FrenchCard(Suit.HEARTS, Rank.NINE, BackColor.DARK);

        assertTrue(() -> match.getAttachmentChecker().isAttachable(heartsNine));
    }
}