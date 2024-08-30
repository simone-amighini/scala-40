package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class PlannedDisconnectionEvent extends Event {
    public enum Cause {
        CLIENT_ERROR,
        CLIENT_DISCONNECTION,
        FIRST_PLAYER_DISCONNECTION,
        REDUNDANT_PLAYER,
        NOT_A_SAVED_GAME_PLAYER
    }

    private final Cause cause;

    public PlannedDisconnectionEvent(Cause cause) {
        super("PLANNED_DISCONNECTION");
        this.cause = cause;
    }

    public Cause getCause() {
        return cause;
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
