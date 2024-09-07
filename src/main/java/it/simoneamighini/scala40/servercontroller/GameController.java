package it.simoneamighini.scala40.servercontroller;

import it.simoneamighini.scala40.events.*;
import it.simoneamighini.scala40.model.*;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;

import java.util.*;

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
            PersistenceUtility.saveGameOnDisk(game);
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
                            currentMatch.getPlayers().stream().map(Player::getUsername).toList(),
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
        try {
            boolean success = game.getCurrentMatch().getCurrentPlayer().discardCard(event.getCardId());
            if (success) {
                ConnectionsManager.getInstance().sendEvent(
                        new TurnEndEvent(),
                        event.getRemoteAddress()
                );

                game.getCurrentMatch().goToNextPlayer();
                sendMatchAndInfoUpdateEvent();
                sendTurnStartEvent();

            } else {
                ConnectionsManager.getInstance().sendEvent(
                        new DiscardCardDenialEvent(),
                        event.getRemoteAddress()
                );
            }
        } catch (EndOfMatch signal) {
            List<String> eliminatedPLayersUsernames = game.getCurrentMatch().getPlayers().stream()
                    .filter(player -> !(game.getActivePlayers().contains(player)))
                    .map(Player::getUsername)
                    .toList();

            // alert eliminated players
            Player[] ranking = game.getRanking();
            Map<String, Integer> usernamePointsMap = new HashMap<>();
            for (Player player : ranking) {
                usernamePointsMap.put(player.getUsername(), player.getPoints());
            }
            for (String username : eliminatedPLayersUsernames) {
                ConnectionsManager.getInstance().sendEvent(
                        new EndGameEvent(
                                Arrays.stream(ranking).map(Player::getUsername).toList(),
                                usernamePointsMap,
                                false
                        ),
                        ConnectionsManager.getInstance().getAssociatedRemoteAddress(username)
                );
                ConnectionsManager.getInstance().removeUsernameFromUsernameConnectionMap(username);
            }

            // start a new match with the remaining players
            ConnectionsManager.getInstance().sendEventBroadcast(new EndMatchEvent());
            start(false);

        } catch (EndOfGame signal) {
            Player[] ranking = game.getRanking();
            Map<String, Integer> usernamePointsMap = new HashMap<>();
            for (Player player : ranking) {
                usernamePointsMap.put(player.getUsername(), player.getPoints());
            }

            ConnectionsManager.getInstance().sendEventBroadcast(
                    new EndGameEvent(
                            Arrays.stream(ranking).map(Player::getUsername).toList(),
                            usernamePointsMap,
                            true
                    )
            );

        } catch (IllegalStateException exception) {
            ConnectionsManager.getInstance().sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public void handle(OpeningEvent event) {
        try {
            boolean success = game.getCurrentMatch().getCurrentPlayer().placeOpeningCards(event.getGroups());
            if (success) {
                ConnectionsManager.getInstance().sendEvent(
                        new OpeningConfirmationEvent(),
                        event.getRemoteAddress()
                );
            } else {
                ConnectionsManager.getInstance().sendEvent(
                        new OpeningDenialEvent(),
                        event.getRemoteAddress()
                );
            }
        } catch (IllegalStateException exception) {
            ConnectionsManager.getInstance().sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public void handle(PlaceGroupEvent event) {
        try {
            boolean success = game.getCurrentMatch().getCurrentPlayer().placeGroup(event.getGroup());
            if (success) {
                ConnectionsManager.getInstance().sendEvent(
                        new PlaceGroupConfirmationEvent(),
                        event.getRemoteAddress()
                );
            } else {
                ConnectionsManager.getInstance().sendEvent(
                        new PlaceGroupDenialEvent(),
                        event.getRemoteAddress()
                );
            }
        } catch (IllegalStateException exception) {
            ConnectionsManager.getInstance().sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public void handle(AttachGroupEvent event) {
        try {
            Position attachPosition = switch (event.getPosition()) {
                case START -> Position.START;
                case END -> Position.END;
            };

            boolean success = game.getCurrentMatch().getCurrentPlayer().attachGroup(
                    event.getGroup(),
                    event.getGroupNumber(),
                    attachPosition
            );
            if (success) {
                ConnectionsManager.getInstance().sendEvent(
                        new AttachGroupConfirmationEvent(),
                        event.getRemoteAddress()
                );
            } else {
                ConnectionsManager.getInstance().sendEvent(
                        new AttachGroupDenialEvent(),
                        event.getRemoteAddress()
                );
            }
        } catch (IllegalStateException exception) {
            ConnectionsManager.getInstance().sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public void handle(AttachCardEvent event) {
        try {
            Position attachPosition = switch (event.getPosition()) {
                case START -> Position.START;
                case END -> Position.END;
            };

            boolean success = game.getCurrentMatch().getCurrentPlayer().attachSingleCard(
                    event.getCardID(),
                    event.getGroupNumber(),
                    attachPosition
            );
            if (success) {
                ConnectionsManager.getInstance().sendEvent(
                        new AttachCardConfirmationEvent(),
                        event.getRemoteAddress()
                );
            } else {
                ConnectionsManager.getInstance().sendEvent(
                        new AttachCardDenialEvent(),
                        event.getRemoteAddress()
                );
            }
        } catch (IllegalStateException exception) {
            ConnectionsManager.getInstance().sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public void handle(ReplaceJollyEvent event) {
        try {
            boolean success = game.getCurrentMatch().getCurrentPlayer().replaceJolly(
                    event.getCardID(),
                    event.getGroupNumber(),
                    event.getJollyIndex()
            );
            if (success) {
                ConnectionsManager.getInstance().sendEvent(
                        new ReplaceJollyConfirmationEvent(),
                        event.getRemoteAddress()
                );
            } else {
                ConnectionsManager.getInstance().sendEvent(
                        new ReplaceJollyDenialEvent(),
                        event.getRemoteAddress()
                );
            }
        } catch (IllegalStateException exception) {
            ConnectionsManager.getInstance().sendEvent(
                    new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                    event.getRemoteAddress()
            );
        }
    }

    public void handle(CancelTurnEvent event) {
        game = game.getCurrentMatch().getCurrentPlayer().cancelTurn();
        sendMatchAndInfoUpdateEvent();
        ConnectionsManager.getInstance().sendEvent(
                new CancelTurnConfirmationEvent(),
                event.getRemoteAddress()
        );
    }
}
