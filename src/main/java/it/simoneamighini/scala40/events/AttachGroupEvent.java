package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;
import it.simoneamighini.scala40.view.gui.guicontroller.MatchController;

import java.util.List;

public class AttachGroupEvent extends Event {
    private final List<String> group;
    private final int groupNumber;
    private final MatchController.WildCard.Position position;

    public AttachGroupEvent(List<String> group, int groupNumber, MatchController.WildCard.Position position) {
        super("ATTACH_GROUP");
        this.group = group;
        this.groupNumber = groupNumber;
        this.position = position;
    }

    public List<String> getGroup() {
        return group;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public MatchController.WildCard.Position getPosition() {
        return position;
    }

    @Override
    public void callHandler() {
        ConnectionsManager.getInstance().handle(this);
    }
}
