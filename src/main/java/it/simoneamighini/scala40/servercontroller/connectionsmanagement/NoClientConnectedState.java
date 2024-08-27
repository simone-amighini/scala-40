package it.simoneamighini.scala40.servercontroller.connectionsmanagement;

import it.simoneamighini.scala40.events.FirstPlayerChoicesEvent;
import it.simoneamighini.scala40.events.FirstPlayerResponseEvent;
import it.simoneamighini.scala40.events.GameEnterEvent;

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

        // TODO: check if a saved game exists and if the player belongs to it, then send the right response

        connectionsManager.sendEvent(
                new FirstPlayerResponseEvent(false),
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
