package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;

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
    public void callHandler() {}
}
