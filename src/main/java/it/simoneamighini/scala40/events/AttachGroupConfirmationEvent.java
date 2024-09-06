package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class AttachGroupConfirmationEvent extends Event {
    public AttachGroupConfirmationEvent() {
        super("ATTACH_GROUP_CONFIRMATION");
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
