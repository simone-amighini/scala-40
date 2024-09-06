package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.Data;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class OpeningConfirmationEvent extends Event {
    public OpeningConfirmationEvent() {
        super("OPENING_CONFIRMATION");
    }

    @Override
    public void callHandler() {
        Data.getInstance().setPlayerHasOpened(true);
        SceneLoader.getCurrentController().handle(this);
    }
}
