package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class ReplaceJollyConfirmationEvent extends Event {
    public ReplaceJollyConfirmationEvent() {
        super("REPLACE_JOLLY_CONFIRMATION");
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
