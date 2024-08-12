package it.simoneamighini.scala40.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RankTest {
    @Nested
    class precedenceTests {
        @Test
        void precedenceFourFive() {
            assertTrue(Rank.FOUR.precedes(Rank.FIVE));
        }

        @Test
        void precedenceFourSix() {
            assertFalse(Rank.FOUR.precedes(Rank.SIX));
        }

        @Test
        void precedenceFourKing() {
            assertFalse(Rank.FOUR.precedes(Rank.KING));
        }

        @Test
        void precedenceAceKing() {
            assertFalse(Rank.ACE.precedes(Rank.KING));
        }

        @Test
        void precedenceKingAce() {
            assertTrue(Rank.KING.precedes(Rank.ACE));
        }

        @Test
        void precedenceQueenKing() {
            assertTrue(Rank.QUEEN.precedes(Rank.KING));
        }

        @Test
        void precedenceAceTwo() {
            assertTrue(Rank.ACE.precedes(Rank.TWO));
        }
    }

    @Nested
    class successionTests {
        @Test
        void successionEightSeven() {
            assertTrue(Rank.EIGHT.succeeds(Rank.SEVEN));
        }

        @Test
        void successionKingJack() {
            assertFalse(Rank.KING.succeeds(Rank.JACK));
        }

        @Test
        void successionKingAce() {
            assertFalse(Rank.KING.succeeds(Rank.ACE));
        }

        @Test
        void successionAceKing() {
            assertTrue(Rank.ACE.succeeds(Rank.KING));
        }
    }
}