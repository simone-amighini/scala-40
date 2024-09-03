package it.simoneamighini.scala40.servercontroller.connectionsmanagement;

import it.simoneamighini.scala40.events.FirstPlayerChoicesEvent;
import it.simoneamighini.scala40.events.GameEnterEvent;
import it.simoneamighini.scala40.events.GameEnterResponseEvent;
import it.simoneamighini.scala40.events.PlannedDisconnectionEvent;
import it.simoneamighini.scala40.servercontroller.GameController;

public class BlockedConnectionsState implements ConnectionsManagerState {
    private final ConnectionsManager connectionsManager;

    public BlockedConnectionsState(ConnectionsManager connectionsManager) {
        this.connectionsManager = connectionsManager;
    }

    @Override
    public void handle(GameEnterEvent event) {
        String remoteAddress = event.getRemoteAddress();

        if (connectionsManager.isConnected(remoteAddress)) {
            // disconnect the client responsible for it
            connectionsManager.closeConnection(remoteAddress);
        } else {
            // notify foreign client that the game is full
            connectionsManager.sendEvent(
                    new GameEnterResponseEvent(GameEnterResponseEvent.Response.REFUSED),
                    event.getRemoteAddress()
            );
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
            // disconnection of player
            boolean isPlayerActive = GameController.getInstance()
                    .isPlayerActive(connectionsManager.getAssociatedUsername(remoteAddress));

            connectionsManager.removeRemoteAddressFromUsernameConnectionMap(remoteAddress);

            if (isPlayerActive) {
                connectionsManager.sendEventBroadcast(
                        new PlannedDisconnectionEvent(PlannedDisconnectionEvent.Cause.CLIENT_DISCONNECTION)
                );
                connectionsManager.reset();
                GameController.getInstance().reset();
            }
        }
        // else: it is a foreign client, ignore it
    }
}
