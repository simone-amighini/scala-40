package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class EndMatchEvent extends Event {
    public EndMatchEvent() {
        super("END_MATCH");
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
