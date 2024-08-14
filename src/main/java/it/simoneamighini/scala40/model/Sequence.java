package it.simoneamighini.scala40.model;

import java.util.ArrayList;
import java.util.List;

public class Sequence extends Group {
    Sequence(List<Card> cards) throws InvalidGroupException {
        super(cards);

        // perform check
        List<FrenchCard> frenchCards = cards.stream()
                .filter(card -> card instanceof FrenchCard)
                .map(card -> (FrenchCard) card)
                .toList();
        List<JollyCard> jollyCards = cards.stream()
                .filter(card -> card instanceof JollyCard)
                .map(card -> (JollyCard) card)
                .toList();

        boolean betweenThreeAndFourteenCards = cards.size() >= 3 && cards.size() <= 14;

        boolean noMoreThanOneJolly = jollyCards.size() <= 1;

        boolean allCardsInTheRightOrder = true;
        boolean allCardsWithTheSameSuit = true;
        boolean noSequenceCrossover = true;

        if (noMoreThanOneJolly & betweenThreeAndFourteenCards) {
            // replace the jolly (if present) with the card that it substitutes
            List<FrenchCard> cardsWithoutJolly = new ArrayList<>();

            for (int i = 0; i < cards.size() - 1; i++) {
                Card currentCard = cards.get(i);
                Card nextCard = cards.get(i + 1);

                if (currentCard instanceof FrenchCard) {
                    cardsWithoutJolly.add((FrenchCard) currentCard);
                } else if (currentCard instanceof JollyCard) {
                    cardsWithoutJolly.add(
                            new FrenchCard(
                                    ((FrenchCard) nextCard).getSuit(),
                                    ((FrenchCard) nextCard).getRank().previous(),
                                    BackColor.DARK
                            )
                    );
                }
            }
            Card lastCard = cards.getLast();
            if (lastCard instanceof  FrenchCard) {
                cardsWithoutJolly.add((FrenchCard) lastCard);
            } else if (cards.getLast() instanceof JollyCard) {
                FrenchCard secondLastCard = (FrenchCard) cards.get(cards.size() - 2);
                cardsWithoutJolly.add(
                        new FrenchCard(
                                secondLastCard.getSuit(),
                                secondLastCard.getRank().next(),
                                BackColor.DARK
                        )
                );
            }

            // check that the new sequence has the cards in the right order
            for (int i = 0; i < cardsWithoutJolly.size() - 1; i++) {
                FrenchCard currentCard = cardsWithoutJolly.get(i);
                FrenchCard nextCard = cardsWithoutJolly.get(i + 1);

                if (!currentCard.getRank().precedes(nextCard.getRank())) {
                    allCardsInTheRightOrder = false;
                    break;
                }
            }

            // check that all cards have the same suit
            for (int i = 0; i < cardsWithoutJolly.size() - 1; i++) {
                FrenchCard currentCard = cardsWithoutJolly.get(i);
                FrenchCard nextCard = cardsWithoutJolly.get(i + 1);

                if (!(currentCard.getSuit().equals(nextCard.getSuit()))) {
                    allCardsWithTheSameSuit = false;
                    break;
                }
            }

            // check that there is no crossover (KING - ACE - TWO)
            for (int i = 0; i < cardsWithoutJolly.size() - 2; i++) {
                FrenchCard currentCard = cardsWithoutJolly.get(i);
                FrenchCard nextCard = cardsWithoutJolly.get(i + 1);
                FrenchCard cardAfterNextCard = cardsWithoutJolly.get(i + 2);

                if (currentCard.getRank().equals(Rank.KING) &&
                        nextCard.getRank().equals(Rank.ACE) &&
                        cardAfterNextCard.getRank().equals(Rank.TWO)) {
                    noSequenceCrossover = false;
                    break;
                }
            }
        }

        boolean checkPassed = betweenThreeAndFourteenCards &&
                noMoreThanOneJolly &&
                allCardsInTheRightOrder &&
                allCardsWithTheSameSuit &&
                noSequenceCrossover;

        if (!checkPassed) {
            throw new InvalidGroupException("Invalid sequence");
        }
    }
}
