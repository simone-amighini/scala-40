package it.simoneamighini.scala40.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable {
    private final Game game;
    private final List<Player> players;
    private boolean justStarted;
    private boolean finished;
    private int turnNumber;
    private Player currentPlayer;
    private final Deck deck;
    private DiscardedCardsStack discardedCardsStack;
    private final List<Group> groups;
    private final SingleCardAttacher singleCardAttacher;
    private final GroupPlacer groupPlacer;
    private final GroupAttacher groupAttacher;
    private final OpeningPlacer openingPlacer;
    private final AttachmentChecker attachmentChecker;
    private final JollyReplacer jollyReplacer;

    Match(Game game, List<Player> players) {
        this.game = game;

        this.players = players;
        for (Player player : players) {
            player.linkToMatch(this);
        }

        this.justStarted = true;
        this.finished = false;
        this.turnNumber = 1;
        this.currentPlayer = null;
        this.deck = new Deck();
        this.discardedCardsStack = new DiscardedCardsStack();
        this.groups = new ArrayList<>();
        this.singleCardAttacher = new SingleCardAttacher(this);
        this.groupPlacer = new GroupPlacer(this);
        this.groupAttacher = new GroupAttacher(this);
        this.openingPlacer = new OpeningPlacer(this);
        this.attachmentChecker = new AttachmentChecker(this);
        this.jollyReplacer = new JollyReplacer(this);

        // start the match
        distributeCards();
    }

    public boolean isJustStarted() {
        return justStarted;
    }

    public boolean isFinished() {
        return finished;
    }

    private void distributeCards() {
        for (Player player : players) {
            for (int i = 0; i < 13; i++) {
                player.addToHand(deck.draw());
            }
        }
        discardedCardsStack.put(deck.draw());
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void goToNextPlayer() {
        justStarted = false;
        fillDeckIfEmpty();

        if (currentPlayerWins()) {
            addPointsToPlayers();
            finished = true;
            game.postMatchProcedure();
            return;
        }

        int currentPlayerIndex = players.indexOf(currentPlayer);
        if (currentPlayerIndex != players.size() - 1) {
            currentPlayer = players.get(currentPlayerIndex + 1);
        } else {
            currentPlayer = players.getFirst();
            turnNumber++;
        }
    }

    private void fillDeckIfEmpty() {
        if (deck.isEmpty()) {
            Card stockTopCard = discardedCardsStack.pickTopCard();

            List<Card> otherDiscardedCards = new ArrayList<>();
            while (true) {
                Card pickedCard = discardedCardsStack.pickTopCard();
                if (pickedCard != null) {
                    otherDiscardedCards.add(pickedCard);
                } else {
                    break;
                }
            }
            deck.fillWith(otherDiscardedCards);

            discardedCardsStack = new DiscardedCardsStack();
            discardedCardsStack.put(stockTopCard);
        }
    }

    private boolean currentPlayerWins() {
        return currentPlayer.getHand().isEmpty();
    }

    private void addPointsToPlayers() {
        // add the points to the players
        for (Player player : players) {
            if (!player.hasOpened()) {
                player.addPoints(100);
            } else {
                for (Card card : player.getHand()) {
                    player.addPoints(card.getDefaultPoints());
                }
            }
        }
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public Deck getDeck() {
        return deck;
    }

    public DiscardedCardsStack getDiscardedCardsStack() {
        return discardedCardsStack;
    }

    public List<Group> getGroups() {
        return new ArrayList<>(groups);
    }

    void updateGroup(Group oldGroup, Group newGroup) {
        int oldGroupIndex = groups.indexOf(oldGroup);
        groups.set(oldGroupIndex, newGroup);
    }

    void addGroup(Group group) {
        groups.add(group);
    }

    SingleCardAttacher getSingleCardAttacher() {
        return singleCardAttacher;
    }

    GroupPlacer getGroupPlacer() {
        return groupPlacer;
    }

    GroupAttacher getGroupAttacher() {
        return groupAttacher;
    }

    OpeningPlacer getOpeningPlacer() {
        return openingPlacer;
    }

    AttachmentChecker getAttachmentChecker() {
        return attachmentChecker;
    }

    JollyReplacer getJollyReplacer() {
        return jollyReplacer;
    }

    void requestGameSaving() {
        game.saveOnDisk();
    }
}
