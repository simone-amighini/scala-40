package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;

public class FirstPlayerResponseEvent extends Event {
    private final boolean existsSavedGame;

    public FirstPlayerResponseEvent(boolean existsSavedGame) {
        super("FIRST_PLAYER_RESPONSE");
        this.existsSavedGame = existsSavedGame;
    }

    public boolean existsSavedGame() {
        return existsSavedGame;
    }

    @Override
    public void callHandler() {}
}
