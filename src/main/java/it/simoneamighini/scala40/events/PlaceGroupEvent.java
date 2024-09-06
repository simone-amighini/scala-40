package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;

import java.util.List;

public class PlaceGroupEvent extends Event {
    private final List<String> group;

    public PlaceGroupEvent(List<String> group) {
        super("PLACE_GROUP");
        this.group = group;
    }

    public List<String> getGroup() {
        return group;
    }

    @Override
    public void callHandler() {
        ConnectionsManager.getInstance().handle(this);
    }
}
