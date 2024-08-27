package it.simoneamighini.scala40.servercontroller.connectionsmanagement;

import it.simoneamighini.scala40.events.FirstPlayerChoicesEvent;
import it.simoneamighini.scala40.events.GameEnterEvent;
import it.simoneamighini.scala40.events.WaitingRoomUpdateEvent;

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
        // TODO
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
