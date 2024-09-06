package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;

public class ReplaceJollyEvent extends Event {
    private final String cardID;
    private final int groupNumber;
    private final int jollyIndex;

    public ReplaceJollyEvent(String cardID, int groupNumber, int jollyIndex) {
        super("REPLACE_JOLLY");
        this.cardID = cardID;
        this.groupNumber = groupNumber;
        this.jollyIndex = jollyIndex;
    }

    public String getCardID() {
        return cardID;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public int getJollyIndex() {
        return jollyIndex;
    }

    @Override
    public void callHandler() {
        ConnectionsManager.getInstance().handle(this);
    }
}
