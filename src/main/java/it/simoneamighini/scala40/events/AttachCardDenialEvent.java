package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class AttachCardDenialEvent extends Event {
    public AttachCardDenialEvent() {
        super("ATTACH_CARD_DENIAL");
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
