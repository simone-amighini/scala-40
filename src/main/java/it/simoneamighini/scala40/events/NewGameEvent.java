package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class NewGameEvent extends Event {
    public NewGameEvent() {
        super("NEW_GAME");
    }

    @Override
    public void callHandler() {
        Data.getInstance().setGameStartType(Data.GameStartType.NEW_GAME);
        SceneLoader.getCurrentController().handle(this);
    }
}
