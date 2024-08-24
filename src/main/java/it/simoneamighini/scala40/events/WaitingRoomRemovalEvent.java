package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;

public class WaitingRoomRemovalEvent extends Event {
    public WaitingRoomRemovalEvent() {
        super("WAITING_ROOM_REMOVAL");
    }

    @Override
    public void callHandler() {}
}
