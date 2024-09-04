package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;

import java.util.List;

public class OpeningEvent extends Event {
    private final List<List<String>> groups;

    public OpeningEvent(List<List<String>> groups) {
        super("OPENING");
        this.groups = groups;
    }

    public List<List<String>> getGroups() {
        return groups;
    }

    @Override
    public void callHandler() {
        ConnectionsManager.getInstance().handle(this);
    }
}
