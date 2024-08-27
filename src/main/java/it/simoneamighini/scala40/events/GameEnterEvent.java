package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;

public class GameEnterEvent extends Event {
    private final String username;

    public GameEnterEvent(String username) {
        super("GAME_ENTER");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void callHandler() {
        ConnectionsManager.getInstance().handle(this);
    }
}
