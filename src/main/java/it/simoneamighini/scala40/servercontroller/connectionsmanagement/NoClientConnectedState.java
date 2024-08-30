package it.simoneamighini.scala40.servercontroller.connectionsmanagement;

import it.simoneamighini.scala40.events.FirstPlayerChoicesEvent;
import it.simoneamighini.scala40.events.FirstPlayerResponseEvent;
import it.simoneamighini.scala40.events.GameEnterEvent;
import it.simoneamighini.scala40.model.PersistenceUtility;

public class NoClientConnectedState implements ConnectionsManagerState {
    private final ConnectionsManager connectionsManager;

    public NoClientConnectedState(ConnectionsManager connectionsManager) {
        this.connectionsManager = connectionsManager;
    }

    @Override
    public void handle(GameEnterEvent event) {
        // first player enters
        connectionsManager.addToUsernameConnectionMap(
                event.getUsername(),
                event.getRemoteAddress()
        );
        connectionsManager.sendEvent(
                new FirstPlayerResponseEvent(PersistenceUtility.belongsToSavedGame(event.getUsername())),
                event.getRemoteAddress()
        );
        connectionsManager.changeState(new FirstPlayerConnectedState(connectionsManager));
    }

    @Override
    public void handle(FirstPlayerChoicesEvent event) {
        // corrupted foreign client sent an unexpected event: close its connection
        connectionsManager.closeConnection(event.getRemoteAddress());
    }

    @Override
    public void handleClientDisconnection(String remoteAddress) {}
}
