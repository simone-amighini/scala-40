package it.simoneamighini.scala40.servercontroller.connectionsmanagement;

import it.simoneamighini.scala40.events.FirstPlayerChoicesEvent;
import it.simoneamighini.scala40.events.GameEnterEvent;
import it.simoneamighini.scala40.events.GameEnterResponseEvent;
import it.simoneamighini.scala40.events.WaitingRoomUpdateEvent;
import it.simoneamighini.scala40.model.PersistenceUtility;

public class WaitingPlayersForGameResumeState implements ConnectionsManagerState {
    private final ConnectionsManager connectionsManager;
    private final int numberOfPlayersToWaitFor;

    public WaitingPlayersForGameResumeState(
            ConnectionsManager connectionsManager,
            int numberOfPlayersToWaitFor
    ) {
        this.connectionsManager = connectionsManager;
        this.numberOfPlayersToWaitFor = numberOfPlayersToWaitFor;
    }

    @Override
    public void handle(GameEnterEvent event) {
        String username = event.getUsername();
        String remoteAddress = event.getRemoteAddress();

        // check on username
        if (!connectionsManager.passesUsernameCheck(username)) {
            connectionsManager.sendEvent(
                    new GameEnterResponseEvent(GameEnterResponseEvent.Response.USERNAME_REFUSED),
                    remoteAddress
            );
            return;
        }

        if (!PersistenceUtility.belongsToSavedGame(username)) {
            connectionsManager.sendEvent(
                    new GameEnterResponseEvent(GameEnterResponseEvent.Response.NOT_A_SAVED_GAME_PLAYER),
                    remoteAddress
            );
            return;
        }

        // if all the checks are passed
        connectionsManager.addToUsernameConnectionMap(username, remoteAddress);
        connectionsManager.sendEvent(
                new GameEnterResponseEvent(GameEnterResponseEvent.Response.ACCEPTED),
                remoteAddress
        );
        connectionsManager.sendEventBroadcast(
                new WaitingRoomUpdateEvent(connectionsManager.getUsernamesInOrder())
        );

        // check if now there all the needed players
        if (connectionsManager.getNumberOfConnections() == numberOfPlayersToWaitFor) {
            connectionsManager.changeState(new BlockedConnectionsState(connectionsManager));
            // there are all needed players
            connectionsManager.resumeGame();
        }
    }

    @Override
    public void handle(FirstPlayerChoicesEvent event) {
        // in any case disconnect the client responsible for it
        connectionsManager.closeConnection(event.getRemoteAddress());
    }

    @Override
    public void handleClientDisconnection(String remoteAddress) {
        if (connectionsManager.isConnected(remoteAddress)) {
            // remove the connection
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
        // else: it is a foreign client, ignore it
    }
}
