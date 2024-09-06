package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class ReplaceJollyDenialEvent extends Event {
    public ReplaceJollyDenialEvent() {
        super("REPLACE_JOLLY_DENIAL");
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
