package it.simoneamighini.scala40.servercontroller.connectionsmanagement;

import it.simoneamighini.scala40.events.FirstPlayerChoicesEvent;
import it.simoneamighini.scala40.events.GameEnterEvent;

public interface ConnectionsManagerState {
    void handle(GameEnterEvent event);
    void handle(FirstPlayerChoicesEvent event);
    void handleClientDisconnection(String remoteAddress);
}
