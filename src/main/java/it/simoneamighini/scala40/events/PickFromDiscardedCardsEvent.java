package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;

public class PickFromDiscardedCardsEvent extends Event {
    public PickFromDiscardedCardsEvent() {
        super("PICK_FROM_DISCARDED_CARDS");
    }

    @Override
    public void callHandler() {
        ConnectionsManager.getInstance().handle(this);
    }
}
