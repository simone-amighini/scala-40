package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;

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
    public void callHandler() {
        Data.getInstance().setExistsSavedGame(existsSavedGame);
        SceneLoader.getCurrentController().handle(this);
    }
}
