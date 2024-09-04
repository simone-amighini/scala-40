package it.simoneamighini.scala40.servercontroller;

import it.simoneamighini.scala40.events.*;
import it.simoneamighini.scala40.model.*;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    private static GameController instance;

    private Game game;

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    private GameController() {}

    public void reset() {
        game = null;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isPlayerActive(String username) {
        try {
            return game.getActivePlayers().stream()
                    .map(Player::getUsername)
                    .anyMatch(username::equals);
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public void start(boolean resumeGame) {
        if (!resumeGame) {
            game.startNewMatch();
        }
        sendMatchAndInfoUpdateEvent();
        sendTurnStartEvent();
    }

    private void sendMatchAndInfoUpdateEvent() {
        Match currentMatch = game.getCurrentMatch();

        int matchNumber = game.getMatchNumber();
        int turnNumber = currentMatch.getTurnNumber();

        Map<String, Integer> usernamePointsMap = new HashMap<>();
        for (Player player : currentMatch.getPlayers()) {
            usernamePointsMap.put(player.getUsername(), player.getPoints());
        }

        Map<String, Boolean> usernameOpeningCompletedMap = new HashMap<>();
        for (Player player : currentMatch.getPlayers()) {
            usernameOpeningCompletedMap.put(player.getUsername(), player.hasOpened());
        }

        Map<String, Integer> usernameRemainingCardsMap = new HashMap<>();
        for (Player player : currentMatch.getPlayers()) {
            usernameRemainingCardsMap.put(player.getUsername(), player.getHand().size());
        }

        String deckTopCardID = currentMatch.getDeck().viewTopCard().getName();
        String visibleDiscardedCardID = currentMatch.getDiscardedCardsStack().viewTopCard().getName();

        List<List<String>> groups = new ArrayList<>();
        for (Group group : currentMatch.getGroups()) {
            groups.add(group.getCards().stream().map(Card::getName).toList());
        }

        for (Player player : game.getCurrentMatch().getPlayers()) {
            ConnectionsManager.getInstance().sendEvent(
                    new MatchInfoUpdateEvent(
                            player.getUsername(),
                            matchNumber,
                            turnNumber,
                            usernamePointsMap,
                            usernameOpeningCompletedMap,
                            usernameRemainingCardsMap,
                            deckTopCardID,
                            visibleDiscardedCardID,
                            groups,
                            player.getHand().stream().map(Card::getName).toList()
                    ),
                    ConnectionsManager.getInstance().getAssociatedRemoteAddress(player.getUsername())
            );
        }
    }

    private void sendTurnStartEvent() {
        ConnectionsManager.getInstance().sendEventBroadcast(
                new TurnStartEvent(game.getCurrentMatch().getCurrentPlayer().getUsername())
        );
    }

    public void handle(DrawFromDeckEvent event) {
        try {
            game.getCurrentMatch().getCurrentPlayer().drawCardFromDeck();
        } catch (IllegalStateException exception) {
            ConnectionsManager.getInstance().sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public void handle(PickFromDiscardedCardsEvent event) {
        try {
            game.getCurrentMatch().getCurrentPlayer().pickCardFromDiscardedCards();
        } catch (IllegalStateException exception) {
            ConnectionsManager.getInstance().sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public void handle(DiscardCardEvent event) {
        boolean success = game.getCurrentMatch().getCurrentPlayer().discardCard(event.getCardId());
        if (success) {
            ConnectionsManager.getInstance().sendEvent(
                    new TurnEndEvent(),
                    event.getRemoteAddress()
            );
        } else {
            ConnectionsManager.getInstance().sendEvent(
                    new DiscardCardDenialEvent(),
                    event.getRemoteAddress()
            );
        }
    }
}
