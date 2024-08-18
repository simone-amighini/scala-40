package it.simoneamighini.scala40.model;

import it.simoneamighini.scala40.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String username;
    private int points;
    private Match match;
    private final List<Card> hand;
    private boolean turnStarted;
    private boolean openingCompleted;
    private boolean openedInCurrentTurn;
    private final List<JollyCard> jollyReuseObligations;
    private Card cardReuseObligation;

    public Player(String username) {
        this.username = username;
        this.points = 0;
        this.match = null;
        this.hand = new ArrayList<>();
        this.turnStarted = false;
        this.openingCompleted = false;
        this.openedInCurrentTurn = false;
        this.jollyReuseObligations = new ArrayList<>();
        this.cardReuseObligation = null;
    }

    public String getUsername() {
        return username;
    }

    public int getPoints() {
        return points;
    }

    void addPoints(int points) {
        this.points += points;
    }

    void linkToMatch(Match match) {
        resetMatchRelatedData();
        this.match = match;
    }

    private void resetMatchRelatedData() {
        match = null;
        hand.clear();
        turnStarted = false;
        openingCompleted = false;
        openedInCurrentTurn = false;
        jollyReuseObligations.clear();
        cardReuseObligation = null;
    }

    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    private Card getCardFromHand(String cardName) {
        return hand.stream()
                .filter(card -> cardName.equals(card.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player " + username + " expected to have " +
                        cardName + " in its hand"));
    }

    void addToHand(Card card) {
        hand.add(card);
    }

    public void drawCardFromDeck() throws IllegalStateException {
        if (turnStarted) {
            throw new IllegalStateException("Player " + username + " has already picked a card");
        }

        Card card = match.getDeck().draw();
        hand.add(card);
        turnStarted = true;
    }

    public void pickCardFromDiscardedCards() throws IllegalStateException {
        if (turnStarted) {
            throw new IllegalStateException("Player " + username + " has already picked a card");
        }

        if (!openingCompleted) {
            throw new IllegalStateException("Player " + username + "tried to pick a card from discarded cards " +
                    "before opening");
        }

        Card card = match.getDiscardedCardsStack().pickTopCard();
        hand.add(card);
        setCardReuseObligation(card);
        turnStarted = true;
    }

    public boolean discardCard(String cardName) {
        Card card = getCardFromHand(cardName);
        List<Card> remainingCards = getHand();
        remainingCards.remove(card);

        boolean is2vs2Match = match.getPlayers().size() == 2;
        boolean attachmentCheckPassed = !match.getAttachmentChecker().isAttachable(card) ||
                (is2vs2Match && openingCompleted) ||
                remainingCards.isEmpty();

        if (attachmentCheckPassed &&
                !hasJollyReuseObligations() &&
                !hasCardReuseObligation()) {
            // discard the card
            hand.remove(card);
            match.getDiscardedCardsStack().put(card);

            // reset the flags used during the turn
            turnStarted = false;
            openedInCurrentTurn = false;

            return true;
        } else {
            return false;
        }
    }

    public boolean hasOpened() {
        return openingCompleted;
    }

    public boolean hasJollyReuseObligations() {
        return !jollyReuseObligations.isEmpty();
    }

    private void addJollyReuseObligation(JollyCard jollyReuseObligation) {
        jollyReuseObligations.add(jollyReuseObligation);
    }

    private void cancelJollyReuseObligation(JollyCard jollyReuseObligation) {
        jollyReuseObligations.remove(jollyReuseObligation);
    }

    public boolean hasCardReuseObligation() {
        return cardReuseObligation != null;
    }

    private void setCardReuseObligation(Card cardReuseObligation) {
        this.cardReuseObligation = cardReuseObligation;
    }

    private void cancelCardReuseObligation() {
        cardReuseObligation = null;
    }

    private boolean prePlacementCheckPassed(Card card) {
        List<Card> remainingCards = getHand();
        remainingCards.remove(card);
        return !remainingCards.isEmpty();
    }

    private boolean prePlacementCheckPassed(List<Card> cards) {
        List<Card> remainingCards = getHand();
        remainingCards.removeAll(cards);
        return !remainingCards.isEmpty();
    }

    public boolean attachSingleCard(String cardName, int groupNumber, Position position) throws IllegalStateException {
        if (!turnStarted) {
            throw new IllegalStateException("Player " + username + " tried to attach a single card before " +
                    "picking a card");
        }

        if (!openingCompleted) {
            throw new IllegalStateException("Player " + username + " tried to attach a card before opening");
        }

        if (openedInCurrentTurn) {
            return false;
        }

        Card card = getCardFromHand(cardName);
        if (!prePlacementCheckPassed(card)) {
            return false;
        }

        boolean success;
        success = match.getSingleCardAttacher().attach(card, groupNumber, position);
        if (success) {
            hand.remove(card);
            for (JollyCard jollyReuseObligation : jollyReuseObligations) {
                if (card.equals(jollyReuseObligation)) {
                    cancelJollyReuseObligation(jollyReuseObligation);
                }
            }
            if (card.equals(cardReuseObligation)) {
                cancelCardReuseObligation();
            }
        }
        return success;
    }

    public boolean placeGroup(String[] cardNames) throws IllegalStateException {
        if (!turnStarted) {
            throw new IllegalStateException("Player " + username + " tried to place a group before " +
                    "picking a card");
        }

        if (!openingCompleted) {
            throw new IllegalStateException("Player " + username + " tried to place a group of cards " +
                    "before opening");
        }

        if (openedInCurrentTurn) {
            return false;
        }

        List<Card> group = new ArrayList<>();
        for (String cardName : cardNames) {
            Card card = getCardFromHand(cardName);
        }
        if (!prePlacementCheckPassed(group)) {
            return false;
        }

        boolean success;
        success = match.getGroupPlacer().place(group);
        if (success) {
            hand.removeAll(group);
            for (JollyCard jollyReuseObligation : jollyReuseObligations) {
                if (group.contains(jollyReuseObligation)) {
                    cancelJollyReuseObligation(jollyReuseObligation);
                }
            }
            if (group.contains(cardReuseObligation)) {
                cancelCardReuseObligation();
            }
        }
        return success;
    }

    public boolean attachGroup(String[] cardNames, int groupNumber, Position position) throws IllegalStateException {
        if (!turnStarted) {
            throw new IllegalStateException("Player " + username + " tried to attach a group before " +
                    "picking a card");
        }

        if (!openingCompleted) {
            throw new IllegalStateException("Player " + username + " tried to attach a group to another before " +
                    "opening");
        }

        if (openedInCurrentTurn) {
            return false;
        }

        List<Card> group = new ArrayList<>();
        for (String cardName : cardNames) {
            Card card = getCardFromHand(cardName);
        }

        if (!prePlacementCheckPassed(group)) {
            return false;
        }

        boolean success;
        success = match.getGroupAttacher().attach(group, groupNumber, position);
        if (success) {
            hand.removeAll(group);
            for (JollyCard jollyReuseObligation : jollyReuseObligations) {
                if (group.contains(jollyReuseObligation)) {
                    cancelJollyReuseObligation(jollyReuseObligation);
                }
            }
            if (group.contains(cardReuseObligation)) {
                cancelCardReuseObligation();
            }
        }
        return success;
    }

    public boolean placeOpeningCards(String[][] groups) throws IllegalStateException {
        if (!turnStarted) {
            throw new IllegalStateException("Player " + username + " tried to open before " +
                    "picking a card");
        }

        // cannot open twice
        if (openingCompleted) {
            throw new IllegalStateException("Player " + username + " tried to open after it has already done once");
        }

        List<List<Card>> cardGroups = new ArrayList<>();
        List<Card> allOpeningCards = new ArrayList<>();
        for (String[] group : groups) {
            List<Card> cards = new ArrayList<>();
            for (String cardName : group) {
                Card card = getCardFromHand(cardName);
                cards.add(card);
                allOpeningCards.add(card);
            }
            cardGroups.add(cards);
        }

        // it is not allowed to place all the cards in the first turn
        int cardCount = 0;
        for (List<Card> group : cardGroups) {
            cardCount += group.size();
        }
        if (cardCount == 12 && match.getTurnNumber() == 1) {
            return false;
        }

        if (!prePlacementCheckPassed(allOpeningCards)) {
            return false;
        }

        boolean success;
        success = match.getOpeningPlacer().place(cardGroups);
        if (success) {
            hand.removeAll(cardGroups);
            openingCompleted = true;
            openedInCurrentTurn = true;
        }
        return success;
    }

    public boolean replaceJolly(int groupNumber, String cardName) throws IllegalStateException {
        if (!turnStarted) {
            throw new IllegalStateException("Player " + username + " tried to replace a jolly before " +
                    "picking a card");
        }

        if (!openingCompleted) {
            throw new IllegalStateException("Player " + username + " tried to replace a jolly before opening");
        }

        if (openedInCurrentTurn) {
            return false;
        }

        Card card = getCardFromHand(cardName);
        JollyCard jollyCard = match.getJollyReplacer().replace(groupNumber, card);
        if (jollyCard != null) {
            hand.remove(card);
            hand.add(jollyCard);
            addJollyReuseObligation(jollyCard);
            return true;
        } else {
            return false;
        }
    }

    // TODO: add support for turn cancellation
}
