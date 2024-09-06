package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class PlaceGroupConfirmationEvent extends Event {
    public PlaceGroupConfirmationEvent() {
        super("PLACE_GROUP_CONFIRMATION");
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
