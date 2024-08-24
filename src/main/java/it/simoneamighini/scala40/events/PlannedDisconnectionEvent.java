package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;

public class PlannedDisconnectionEvent extends Event {
    public enum Cause {
        CLIENT_ERROR,
        CLIENT_DISCONNECTION,
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
    public void callHandler() {}
}
