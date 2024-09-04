package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.servercontroller.connectionsmanagement.ConnectionsManager;
import it.simoneamighini.scala40.view.gui.guicontroller.MatchController;

public class AttachCardEvent extends Event {
    private final String cardID;
    private final int groupNumber;
    private final MatchController.WildCard.Position position;

    public AttachCardEvent(String cardID, int groupNumber, MatchController.WildCard.Position position) {
        super("ATTACH_CARD");
        this.cardID = cardID;
        this.groupNumber = groupNumber;
        this.position = position;
    }

    public String getCardID() {
        return cardID;
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
