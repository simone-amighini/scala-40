package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class TurnStartEvent extends Event {
    private final String username;

    public TurnStartEvent(String username) {
        super("TURN_START");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
