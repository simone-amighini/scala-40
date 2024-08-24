package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;

import java.util.List;

public class WaitingRoomUpdateEvent extends Event {
    private final List<String> usernames;

    public WaitingRoomUpdateEvent(List<String> usernames) {
        super("WAITING_ROOM_UPDATE");
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    @Override
    public void callHandler() {}
}
