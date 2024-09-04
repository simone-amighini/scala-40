package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class DiscardCardDenialEvent extends Event {
    public DiscardCardDenialEvent() {
        super("DISCARD_CARD_RESPONSE");
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
