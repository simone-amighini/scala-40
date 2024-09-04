package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;

public class DiscardCardEvent extends Event {
    private final String cardId;

    public DiscardCardEvent(String cardId) {
        super("DISCARD_CARD");
        this.cardId = cardId;
    }

    public String getCardId() {
        return cardId;
    }

    @Override
    public void callHandler() {
        ConnectionsManager.getInstance().handle(this);
    }
}
