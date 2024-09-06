package it.simoneamighini.scala40.model;

import java.util.List;

public class Combination extends Group {
    Combination(List<Card> cards) throws InvalidGroupException {
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

        boolean threeOrFourCards = cards.size() == 3 || cards.size() == 4;

        boolean noMoreThanOneJolly = jollyCards.size() <= 1;

        boolean allFrenchCardsWithSameRank = frenchCards.stream()
                .map(FrenchCard::getRank)
                .distinct()
                .count() == 1;

        boolean allFrenchCardsWithDifferentSuit = frenchCards.stream()
                .map(FrenchCard::getSuit)
                .distinct()
                .count() == frenchCards.size();

        boolean checkPassed = threeOrFourCards &&
                noMoreThanOneJolly &&
                allFrenchCardsWithSameRank &&
                allFrenchCardsWithDifferentSuit;

        if (!checkPassed) {
            throw new InvalidGroupException("Invalid combination");
        }
    }

    @Override
    int getPoints() {
        List<Card> cards = super.getCards();
        Card firstCard = cards.getFirst();

        return cards.size() * firstCard.getDefaultPoints();
    }

    @Override
    void accept(SingleCardAttacher visitor) {
        visitor.executeAttachmentOn(this);
    }

    @Override
    void accept(GroupAttacher visitor) {
        visitor.executeAttachmentOn(this);
    }
}
