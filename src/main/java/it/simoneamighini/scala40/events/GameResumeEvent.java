package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class GameResumeEvent extends Event {
    public GameResumeEvent() {
        super("GAME_RESUME");
    }

    @Override
    public void callHandler() {
        Data.getInstance().setGameStartType(Data.GameStartType.RESUMED_GAME);
        SceneLoader.getCurrentController().handle(this);
    }
}
