package it.simoneamighini.scala40.servercontroller.connectionsmanagement;

import it.simoneamighini.scala40.events.*;
import it.simoneamighini.scala40.model.PersistenceUtility;

import java.util.List;

public class FirstPlayerConnectedState implements ConnectionsManagerState {
    private final ConnectionsManager connectionsManager;

    public FirstPlayerConnectedState(ConnectionsManager connectionsManager) {
        this.connectionsManager = connectionsManager;
    }

    @Override
    public void handle(GameEnterEvent event) {
        String remoteAddress = event.getRemoteAddress();

        int numberOfConnectedPlayers = connectionsManager.getNumberOfConnections();
        if (numberOfConnectedPlayers == 6) {
            // no more connections accepted
            connectionsManager.sendEvent(
                    new GameEnterResponseEvent(GameEnterResponseEvent.Response.REFUSED),
                    remoteAddress
            );
            return;
        }

        if (!connectionsManager.passesUsernameCheck(event.getUsername())) {
            connectionsManager.sendEvent(
                    new GameEnterResponseEvent(GameEnterResponseEvent.Response.USERNAME_REFUSED),
                    remoteAddress
            );
            return;
        }

        // all checks passed
        connectionsManager.addToUsernameConnectionMap(
                event.getUsername(),
                remoteAddress
        );
        connectionsManager.sendEvent(
                new GameEnterResponseEvent(GameEnterResponseEvent.Response.ACCEPTED),
                remoteAddress
        );
        connectionsManager.sendEventBroadcast(
                new WaitingRoomUpdateEvent(connectionsManager.getUsernamesInOrder())
        );
    }

    @Override
    public void handle(FirstPlayerChoicesEvent event) {
        if (event.requestsGameResume()) {
            // resume game scenario
            if (!PersistenceUtility.existsSavedGame()) {
                connectionsManager.sendEvent(
                        new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR),
                        event.getRemoteAddress()
                );
                return;
            }

            // remove players that do not belong to saved game
            for (String username : connectionsManager.getUsernamesInOrder()) {
                if (!PersistenceUtility.belongsToSavedGame(username)) {
                    connectionsManager.sendEvent(
                            new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.NOT_A_SAVED_GAME_PLAYER),
                            connectionsManager.getAssociatedRemoteAddress(username)
                    );
                    connectionsManager.removeUsernameFromUsernameConnectionMap(username);
                }
            }

            connectionsManager.sendEventBroadcast(
                    new WaitingRoomUpdateEvent(connectionsManager.getUsernamesInOrder())
            );

            int requestedNumberOfPlayers = PersistenceUtility.getSavedGamePlayersNumber();
            if (requestedNumberOfPlayers == connectionsManager.getNumberOfConnections()) {
                // there are all the players that belongs to saved game
                connectionsManager.changeState(new BlockedConnectionsState(connectionsManager));
                connectionsManager.resumeGame();
            } else {
                // more players needed: change state and wait for them
                connectionsManager.changeState(
                        new WaitingPlayersForGameResumeState(connectionsManager, requestedNumberOfPlayers)
                );
            }
        } else {
            // new game scenario
            int requestedNumberOfPlayers = event.getNumberOfPlayers();

            if (requestedNumberOfPlayers < 2 || requestedNumberOfPlayers > 6) {
                // invalid number of players
                connectionsManager.sendEventBroadcast(
                        new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_ERROR)
                );
                connectionsManager.reset();
                return;
            }

            int connectionsNumber = connectionsManager.getNumberOfConnections();
            if (requestedNumberOfPlayers <= connectionsNumber) {
                // there are enough players connected to start the game

                // remove the unneeded players
                List<String> usernamesInOrder = connectionsManager.getUsernamesInOrder();
                for (int i = requestedNumberOfPlayers; i < connectionsNumber; i++) {
                    String currentUsername = usernamesInOrder.get(i);
                    connectionsManager.sendEvent(
                            new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.REDUNDANT_PLAYER),
                            connectionsManager.getAssociatedRemoteAddress(currentUsername)
                    );
                    connectionsManager.removeUsernameFromUsernameConnectionMap(currentUsername);
                }

                connectionsManager.sendEventBroadcast(
                        new WaitingRoomUpdateEvent(connectionsManager.getUsernamesInOrder())
                );
                connectionsManager.changeState(new BlockedConnectionsState(connectionsManager));

                connectionsManager.startNewGame();
            } else {
                // more players needed: change state and wait for them
                connectionsManager.sendEvent(
                        new WaitingRoomUpdateEvent(connectionsManager.getUsernamesInOrder()),
                        event.getRemoteAddress()
                );
                connectionsManager.changeState(
                        new WaitingPlayersForNewGameState(connectionsManager, requestedNumberOfPlayers)
                );
            }
        }
    }

    @Override
    public void handleClientDisconnection(String remoteAddress) {
        if (connectionsManager.isConnected(remoteAddress)) {
            if (connectionsManager.isFirstPlayerConnection(remoteAddress)) {
                // disconnection of the first player before receiving its choices
                connectionsManager.removeRemoteAddressFromUsernameConnectionMap(remoteAddress);
                connectionsManager.sendEventBroadcast(
                        new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.FIRST_PLAYER_DISCONNECTION)
                );
                connectionsManager.reset();
            } else {
                connectionsManager.removeRemoteAddressFromUsernameConnectionMap(remoteAddress);
                // check the remaining connections
                if (connectionsManager.getNumberOfConnections() > 0) {
                    // there is at least 1 connection still active: notify the others
                    connectionsManager.sendEventBroadcast(
                            new WaitingRoomUpdateEvent(connectionsManager.getUsernamesInOrder())
                    );
                } else {
                    // no active connections after this disconnection: reset
                    connectionsManager.reset();
                }
            }
        }
        // else: it is a foreign client, ignore it
    }
}
