package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class AttachCardConfirmationEvent extends Event {
    public AttachCardConfirmationEvent() {
        super("ATTACH_CARD_CONFIRMATION");
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
